package com.dumper.server.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.dumper.server.service.impl.DumpServiceImpl.Command;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

@SpringBootTest
@DirtiesContext
class DumpServiceImplTest {

    // examples:
    // sqlcmd -S localhost -U SA -P Postgres1 -Q "BACKUP DATABASE [TestDB] TO DISK = N'/home/tcen/dumps/backup.bck' WITH NOFORMAT, NOINIT, NAME = 'demodb-full', SKIP, NOREWIND, NOUNLOAD, STATS = 10"
    // sqlcmd -S localhost -U SA -P Postgres1
    // sqlcmd -S localhost -U SA -P Postgres1 -Q "RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/backup.bck'"

    @Autowired
    private DumpServiceImpl dumpService;

    @BeforeEach
    void init() {
        dumpService = new DumpServiceImpl();
    }

    private static Command getCommand() {
        Command command = new Command();
        command.setParams(new HashMap<String, String>() {{
            put("directory", "/dumps/");
            put("database", "db");
            put("filename", "backup.bck");
            put("-P", "pass");
            put("-U", "user");
            put("-S", "localhost");
        }});

        return command;
    }

    @Test
    void getDumpCommand_whenParams_thenValidCommand() {
        Command command = getCommand();

        String actualCommand = dumpService.getDumpCommand.handle(command).getCommand();

        String expectedCommand = "sqlcmd -S localhost -P pass -U user -Q \"BACKUP DATABASE [db] TO DISK = N'/dumps/backup.bck'" +
                " WITH NOFORMAT, NOINIT, SKIP, NOREWIND, NOUNLOAD, STATS = 10\"";

        assertThat(actualCommand).isEqualTo(expectedCommand);
    }

    @Test
    void getRestoreCommand_whenParams_thenValidCommand() {
        Command command = getCommand();

        String actualCommand = dumpService.getRestoreCommand.handle(command).getCommand();

        String expectedCommand = "sqlcmd -S localhost -P pass -U user -Q \"RESTORE DATABASE [db] FROM DISK = N'/dumps/backup.bck'\"";

        assertThat(actualCommand).isEqualTo(expectedCommand);
    }
}