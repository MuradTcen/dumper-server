package com.dumper.server.service.impl;

import com.dumper.server.enums.Query;
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

    @Override
    public void executeCommand(String[] command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            log.info("Start executing command: " + Arrays.toString(command));

            Process proc = runtime.exec(command);
            proc.waitFor();
            InputStream errorStream = proc.getErrorStream();
            String errors = IOUtils.toString(errorStream, StandardCharsets.UTF_8);
            if(!errors.isEmpty()) {
                log.error("errors: " + errors);
            }
        } catch (Exception e) {
            log.error("Error during process: " + e.getLocalizedMessage());
        }
    }

    public void executeFullDump(String filename) {
        Command command = getBaseCommand(filename);

        setQuery(command, Query.BACKUP);

        executeCommand(getCommands(command));
    }

    private String[] getCommands(Command command) {
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

    private static void setQuery(Command command, Query query) {
        String result = query.getQuery().replace(DATABASE_KEY.getKey(), command.getParams().get(DATABASE_KEY.getKey()))
                .replace(DIRECTORY_KEY.getKey(), command.getParams().get(DIRECTORY_KEY.getKey()) +
                        command.getParams().get(FILENAME_KEY.getKey()));

        command.getParams().put(QUERY_KEY.getKey(), result);
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
}
