package com.dumper.server.service;

import com.dumper.server.entity.Backupset;

import java.time.LocalDateTime;
import java.util.List;

public interface BackupsetService {
    List<Backupset> getAllByDatabaseNameAndBackupStartDate(String databaseName, LocalDateTime backupStartDate);

    void saveBackupsByDatabaseNameAndDateTime(String databaseName, String backupStartDate);
}
