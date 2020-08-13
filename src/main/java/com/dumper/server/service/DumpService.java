package com.dumper.server.service;

public interface DumpService {
    void executeCommand(String[] command);

    void executeRestoreFullDump(String filename);

    void executeRestoreDifferentialDump(String filename);

    void executeFullDump(String filename);

    void executeDifferentialDump(String filename);
}
