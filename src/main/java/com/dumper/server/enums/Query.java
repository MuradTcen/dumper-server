package com.dumper.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Query {

    BACKUP("\"BACKUP DATABASE [database] TO DISK = N'directory' WITH NOFORMAT, NOINIT, SKIP, NOREWIND, NOUNLOAD, STATS = 10\""),
    DIFFERENTIAL_BACKUP("\"BACKUP DATABASE [database] TO DISK = N'directory' WITH DIFFERENTIAL, NOFORMAT, NOINIT, SKIP, NOREWIND, NOUNLOAD, STATS = 10\""),
    RESTORE("\"RESTORE DATABASE [database] FROM DISK = N'directory'\"");

    private final String query;
}
