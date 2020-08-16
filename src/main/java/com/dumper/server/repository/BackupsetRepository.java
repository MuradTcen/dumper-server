package com.dumper.server.repository;

import com.dumper.server.entity.Backupset;
import com.dumper.server.entity.BackupsetTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BackupsetRepository extends JpaRepository<BackupsetTest, Long> {
    List<Backupset> getAllByDatabaseNameAndBackupStartDateAfter(String databaseName, LocalDateTime backupStartDate);

    @Query(
            value = "WITH full_dump (lsn, date)\n" +
                    "    AS (select top 1 b.first_lsn, b.backup_start_date from backupset as b where type='D' order by backup_start_date desc)\n" +
                    "\n" +
                    "    select bs.first_lsn, bs.last_lsn,bs.checkpoint_lsn, bs.database_backup_lsn, bm.physical_device_name, bs.type\n" +
                    "    from backupset as bs\n" +
                    "    inner join backupmediafamily as bm on bs.media_set_id = bm.media_set_id\n" +
                    "    where bs.database_name = ?1 and (bs.database_backup_lsn = (select lsn from full_dump) or" +
                    " bs.first_lsn = (select lsn from full_dump)) and bs.backup_start_date >= (select date from full_dump)",
            nativeQuery = true)
    List<Object[]> getDumpsByDatabaseAndDate(String databaseName);
}
