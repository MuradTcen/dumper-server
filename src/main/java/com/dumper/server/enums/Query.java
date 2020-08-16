package com.dumper.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Query {

    BACKUP("BACKUP DATABASE [database] TO DISK = N'directory' WITH COPY_ONLY, STATS = 10"),
    DIFFERENTIAL_BACKUP("BACKUP DATABASE [database] TO DISK = N'directory' WITH DIFFERENTIAL, STATS = 10"),
    RESTORE("RESTORE DATABASE [database] FROM DISK = N'directory' WITH REPLACE"),
    RESTORE_DIFFERENTIAL("RESTORE DATABASE [database] FROM DISK = N'directory' WITH NORECOVERY");

    // sqlcmd -S localhost -P Postgres1 -U SA -Q "RESTORE HEADERONLY FROM DISK = N'/home/tcen/dumps/full.bck'"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "RESTORE HEADERONLY FROM DISK = N'/home/tcen/dumps/diff_0.bck'"

    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP DATABASE [TestDB] TO DISK = N'/home/tcen/dumps/full.bck' WITH COPY_ONLY, STATS = 10"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP DATABASE [TestDB] TO DISK = N'/home/tcen/dumps/diff_0.bck' WITH DIFFERENTIAL, STATS = 10"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP DATABASE [TestDB] TO DISK = N'/home/tcen/dumps/diff_1.bck' WITH DIFFERENTIAL, STATS = 10"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP DATABASE [TestDB] TO DISK = N'/home/tcen/dumps/diff_2.bck' WITH DIFFERENTIAL, STATS = 10"

    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP LOG [TestDB] TO DISK = N'/home/tcen/dumps/log_0.trn' WITH NORECOVERY, INIT, STATS = 10"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "BACKUP LOG [TestDB] TO DISK = N'/home/tcen/dumps/log_1.trn' WITH NOFORMAT, INIT, STATS = 10"

    // sqlcmd -S localhost -P Postgres1 -U SA -Q "RESTORE LOG [TestDB] FROM DISK = N'/home/tcen/dumps/log_0.trn' WITH STATS = 10"
    // sqlcmd -S localhost -P Postgres1 -U SA -Q "RESTORE LOG [TestDB] FROM DISK = N'/home/tcen/dumps/log_1.trn'"

    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/full.bck' WITH REPLACE, NORECOVERY"
    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/diff_0.bck' WITH RECOVERY"
    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/diff_1.bck' WITH RECOVERY"
    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "RESTORE DATABASE [TestDB] FROM DISK = N'/home/tcen/dumps/diff_2.bck' WITH RECOVERY"

    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "DROP DATABASE [TestDB]"
    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "CREATE DATABASE [TestDB]"

    /// sqlcmd -P Postgres1 -S localhost -U SA -Q "USE [TestDB]; CREATE TABLE inventory (id INT IDENTITY(1,1) PRIMARY KEY, product VARCHAR(50) UNIQUE, quantity INT, price DECIMAL(18,2) );"

    private final String query;
}
