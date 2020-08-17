package com.dumper.server.service.impl;

import com.dumper.server.entity.Dump;
import com.dumper.server.enums.Type;
import com.dumper.server.enums.Query;
import com.dumper.server.repository.BackupsetRepository;
import com.dumper.server.service.DumpService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dumper.server.enums.Key.*;

// todo: убрать ссылку после ознакомления
// https://docs.microsoft.com/ru-ru/sql/t-sql/statements/backup-transact-sql?view=sql-server-ver15
@Slf4j
@RequiredArgsConstructor
@Service
public class DumpServiceImpl implements DumpService {

    @Value(value = "${spring.datasource.username:username}")
    private String username;

    @Value(value = "${spring.datasource.password:password}")
    private String password;

    @Value(value = "${server:localhost}")
    private String server;

    @Value(value = "${database.name:TestDB}")
    private String database;

    @Value(value = "${directory:directory}")
    private String directory;

    private final static String BASE_COMMAND = "/opt/mssql-tools/bin/sqlcmd";

    private final BackupsetRepository repository;

    @Override
    public void executeCommand(String[] command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            log.info("Start executing command: " + Arrays.toString(command));

            Process proc = runtime.exec(command);
            proc.waitFor();
            InputStream errorStream = proc.getErrorStream();
            InputStream outStream = proc.getInputStream();
            String errors = IOUtils.toString(errorStream, StandardCharsets.UTF_8);
            String out = IOUtils.toString(outStream, StandardCharsets.UTF_8);
            log.info("out: " + out);
            if (!errors.isEmpty()) {
                log.error("errors: " + errors);
            }
        } catch (Exception e) {
            log.error("Error during process: " + e.getLocalizedMessage());
        }
    }

    @Override
    public int getVersion() {
        String version = repository.getVersion();

        Pattern pattern = Pattern.compile("Server \\d{4}");
        Matcher matcher = pattern.matcher(version);

        return matcher.find() ? Integer.parseInt(matcher.group().split(" ")[1]) : -1;
    }

    @Override
    public void executeQuery(String filename, Query query) {
        Command command = getBaseCommand(filename);
        setQuery(command, query);
        executeCommand(getCommands(command));
    }

    public String[] getCommands(Command command) {
        List<String> result = new ArrayList<>();
        result.add(BASE_COMMAND);

        command.getParams().forEach((k, v) -> {
            if (k.contains("-")) {
                result.add(k);
                result.add(v);
            }
        });

        return result.toArray(new String[0]);
    }

    @Data
    static class Command {
        private HashMap<String, String> params;
    }

    private void setQuery(Command command, Query query) {
        command.getParams().put(QUERY_KEY.getKey(), getQueryWithParams(command, query));
    }

    private String getQueryWithParams(Command command, Query query) {
        return query.getQuery().replace(DATABASE_KEY.getKey(), command.getParams().get(DATABASE_KEY.getKey()))
                .replace(DIRECTORY_KEY.getKey(), command.getParams().get(DIRECTORY_KEY.getKey()) +
                        command.getParams().get(FILENAME_KEY.getKey()));
    }

    public Command getBaseCommand(String filename) {
        Command command = new Command();

        command.setParams(new HashMap<String, String>() {{
            put(DIRECTORY_KEY.getKey(), directory);
            put(DATABASE_KEY.getKey(), database);
            put(FILENAME_KEY.getKey(), filename);
            put(PASSWORD_KEY.getKey(), password);
            put(USER_KEY.getKey(), username);
            put(SERVER_KEY.getKey(), server);
        }});

        log.info("Created command with params: " + command.getParams());

        return command;
    }

    @Override
    public List<Dump> getActualDumpsByDatabaseName(String databaseName) {
        List<Dump> dumps = getDumps(databaseName);
        log.info("Got dumps: " + Arrays.toString(dumps.toArray()));
        return getFilteredDumps(dumps);
    }

    @Override
    public List<Dump> getDumps(String databaseName) {
        return repository.getDumpsByDatabase(databaseName)
                .stream()
                .map(d -> Dump.of(d))
                .sorted(Dump::compareByLastLsn)
                .collect(Collectors.toList());
    }

    // todo: сделать читаемым
    @Override
    public List<Dump> getFilteredDumps(List<Dump> dumps) {
        List<Dump> result = new ArrayList<>();

        Dump fullDump = getFullDump(dumps);
        if (fullDump != null) {
            result.add(fullDump);
        }
        Dump differentialDump = getDifferentialDump(dumps);

        if (differentialDump != null) {
            result.add(differentialDump);
        }

        List<Dump> logs = dumps.stream()
                .filter(d -> d.getType() == Type.L.getName())
                .collect(Collectors.toList());

        result.addAll(getFilteredTransactionalLog(logs, differentialDump));

        return result;
    }

    @Override
    public Dump getFullDump(List<Dump> dumps) {
        return dumps.stream()
                .filter(d -> d.getType() == Type.D.getName())
                .findAny()
                .orElse(null);
    }

    @Override
    public Dump getDifferentialDump(List<Dump> dumps) {
        return dumps.stream()
                .filter(d -> d.getType() == Type.I.getName())
                .max(Dump::compareByLastLsn)
                .orElse(null);
    }

    @Override
    public List<Dump> getFilteredTransactionalLog(List<Dump> logs, Dump differentialDump) {
        List<Dump> result = new ArrayList<>();

        if (differentialDump != null && differentialDump.getLastLsn().equals(logs.get(0).getFirstLsn())) {
            // todo: подумать можно ли сделать лучше и вопрос гарантии порядка после фильтрации и
            //  переписать так чтобы не требовалась проверка на null
            for (int i = 0; i < logs.size() - 1; i++) {
                if (logs.get(i).getLastLsn().equals(logs.get(i + 1).getFirstLsn())) {
                    result.add(logs.get(i));
                }
            }
        }

        return result;
    }

}
