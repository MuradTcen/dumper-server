package com.dumper.server.service;

import com.dumper.server.entity.Dump;

import java.util.List;

public interface DumpService {
    void executeCommand(String[] command);

    void executeRestoreFullDump(String filename);

    void executeRestoreDifferentialDump(String filename);

    void executeFullDump(String filename);

    void executeDifferentialDump(String filename);

    List<Dump> getActualDumpsByDatabaseName(String databaseName);

    List<Dump> getDumpsByDate(String databaseName);

    List<Dump> getFilteredDumps(List<Dump> dumps);

    Dump getFullDump(List<Dump> dumps);

    Dump getDifferentialDump(List<Dump> dumps);

    List<Dump> getFilteredTransactionalLog(List<Dump> logs, Dump differentialDump);
}
