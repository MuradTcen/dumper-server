package com.dumper.server.service;

import com.dumper.server.entity.Dump;
import com.dumper.server.enums.Query;

import java.util.List;

public interface DumpService {
    void executeCommand(String[] command);

    int getVersion();

    void executeQuery(String filename, Query query);

    List<Dump> getActualDumpsByDatabaseName(String databaseName);

    List<Dump> getDumps(String databaseName);

    List<Dump> getFilteredDumps(List<Dump> dumps);

    Dump getFullDump(List<Dump> dumps);

    Dump getDifferentialDump(List<Dump> dumps);

    List<Dump> getFilteredTransactionalLog(List<Dump> logs, Dump differentialDump);

    boolean isCorrectPrevChainToNextByI(List<Dump> logs, int i);
}
