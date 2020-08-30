package com.dumper.server.service;

import com.dumper.server.entity.Dump;

import java.time.LocalDate;
import java.util.List;

public interface DumpService {
    int getVersion();

    List<Dump> getActualDumpsByDatabaseNameAndDate(String databaseName, String dateString);

    List<Dump> getDumps(String databaseName, LocalDate date);
}
