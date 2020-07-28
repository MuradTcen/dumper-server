package com.dumper.server.service.impl;

import com.dumper.server.enums.Key;
import com.dumper.server.enums.Query;
import com.dumper.server.service.DumpService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

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

    private final static String BASE_COMMAND = "sqlcmd";

    @Override
    public void executeCommand(String command) {
        Runtime runtime = Runtime.getRuntime();
        try {
            log.info("Start executing command: " + command);
            runtime.exec(command);
        } catch (IOException e) {
            log.error("Error during process: " + e.getLocalizedMessage());
        }
    }

    public void executeFullDump(String filename) {
        Command command = getBaseCommand(filename);
        executeCommand(getDumpCommand.handle(command).getCommand());
    }

    @FunctionalInterface
    interface Commander {
        Command handle(Command command);

        default Commander andThen(Commander commander) {
            return (another) -> {
                return commander.handle(this.handle(another));
            };
        }
    }

    static Commander addDumpQuery = command -> {
        setQuery(command, Query.BACKUP.getQuery());
        return command;
    };

    static Commander addRestoreQuery = command -> {
        setQuery(command, Query.RESTORE.getQuery());
        return command;
    };

    static Commander addPasswordAndUserValues = command -> {
        setKey(command, Key.PASSWORD_KEY.getKey());
        setKey(command, Key.USER_KEY.getKey());
        return command;
    };

    static Commander addServerValue = command -> {
        setKey(command, Key.SERVER_KEY.getKey());
        return command;
    };

    static void setKey(Command command, String key) {
        if (isContainsKeyAndNotNull(command, key)) {
            command.setCommand(String.format("%s %s %s", command.getCommand(), key, command.getParams().get(key)));
        }
    }

    static void setQuery(Command command, String baseQuery) {
        if (isValidForSettingQuery(command)) {
            String query = replaceFilepathAndDatabase(baseQuery, command);
            setQueryValue(command, query);
        }
    }

    static boolean isValidForSettingQuery(Command command) {
        return isContainsKeyAndNotNull(command, Key.DIRECTORY_KEY.getKey()) &&
                isContainsKeyAndNotNull(command, Key.DATABASE_KEY.getKey()) &&
                isContainsKeyAndNotNull(command, Key.FILENAME_KEY.getKey());
    }

    static void setQueryValue(Command command, String query) {
        command.setCommand(String.format("%s %s %s", command.getCommand(), Key.QUERY_KEY.getKey(), query));
    }

    static String replaceFilepathAndDatabase(String query, Command command) {
        String filepath = command.getParams().get(Key.DIRECTORY_KEY.getKey()) + command.getParams().get(Key.FILENAME_KEY.getKey());
        String database = command.getParams().get(Key.DATABASE_KEY.getKey());

        return query.replace(Key.DIRECTORY_KEY.getKey(), filepath).replace(Key.DATABASE_KEY.getKey(), database);
    }

    static boolean isContainsKeyAndNotNull(Command command, String key) {
        return command.getParams().containsKey(key) && command.getParams().get(key) != null;
    }

    static Commander getDumpCommand = addServerValue.andThen(addPasswordAndUserValues).andThen(addDumpQuery);

    static Commander getRestoreCommand = addServerValue.andThen(addPasswordAndUserValues).andThen(addRestoreQuery);

    @Data
    static class Command {
        private String command = BASE_COMMAND;
        private HashMap<String, String> params;
    }

    private Command getBaseCommand(String filename) {
        Command command = new Command();
        command.setParams(new HashMap<String, String>() {{
            put("directory", directory);
            put("database", database);
            put("filename", filename);
            put("-P", password);
            put("-U", username);
            put("-S", server);
        }});

        log.info("Created command with params: ", command.getParams().get("directory"));

        return command;
    }
}
