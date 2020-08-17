package com.dumper.server.service;

import com.dumper.server.entity.Dump;

import java.util.List;

public interface DumpService {
    void executeCommand(String[] command);

    List<Dump> getActualDumpsByDatabaseName(String databaseName);

    List<Dump> getDumps(String databaseName);

    List<Dump> getFilteredDumps(List<Dump> dumps);

    Dump getFullDump(List<Dump> dumps);

    Dump getDifferentialDump(List<Dump> dumps);

    List<Dump> getFilteredTransactionalLog(List<Dump> logs, Dump differentialDump);
}
