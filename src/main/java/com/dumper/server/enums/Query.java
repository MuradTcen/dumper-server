package com.dumper.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Query {

    BACKUP("BACKUP DATABASE [database] TO DISK = N'directory' WITH INIT, STATS = 10"),
    DIFFERENTIAL_BACKUP("BACKUP DATABASE [database] TO DISK = N'directory' WITH DIFFERENTIAL, STATS = 10"),
    RESTORE("RESTORE DATABASE [database] FROM DISK = N'directory' WITH REPLACE"),
    RESTORE_DIFFERENTIAL("RESTORE DATABASE [database] FROM DISK = N'directory' WITH NORECOVERY");
/// opt/mssql-tools/bin/sqlcmd -P Postgres1 -Q RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/2020-07-29_full.bck' -S localhost -U SA
    // /opt/mssql-tools/bin/sqlcmd -S localhost -P Postgres1 -U SA -Q "RESTORE HEADERONLY FROM DISK = N'/home/tcen/dumps/2020-08-03_17_differential.bck'"

    private final String query;
}
