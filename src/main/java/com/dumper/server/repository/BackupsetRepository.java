package com.dumper.server.repository;

import com.dumper.server.entity.Backupset;
import com.dumper.server.entity.BackupsetTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BackupsetRepository extends JpaRepository<BackupsetTest, Long> {
    List<Backupset> getAllByDatabaseNameAndBackupStartDateAfter(String databaseName, LocalDateTime backupStartDate);

    List<Backupset> getAllByDatabaseNameAndBackupStartDateAfterAndOrderByDatabaseBackupLsnAsc(String databaseName, LocalDateTime backupStartDate);
}
