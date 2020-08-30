package com.dumper.server.dao;

import java.time.LocalDate;
import java.util.List;

public interface DumpDao {
    List<Object[]> getDumpsByDatabaseAndDate(String databaseName, LocalDate date);

    String getVersion();
}
