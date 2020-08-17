package com.dumper.server.repository;

import com.dumper.server.entity.Backupset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupsetRepository extends JpaRepository<Backupset, Long> {

    @Query(
            value = "with full_dump (lsn, backup_lsn, date)\n" +
                    "    as (select top 1 b.first_lsn, b.database_backup_lsn, b.backup_start_date from backupset as b where type='D' order by backup_start_date desc)\n" +
                    "\n" +
                    "    select bs.first_lsn, bs.last_lsn,bs.checkpoint_lsn, bs.database_backup_lsn, bm.physical_device_name, bs.type\n" +
                    "    from backupset as bs\n" +
                    "    inner join backupmediafamily as bm on bs.media_set_id = bm.media_set_id\n" +
                    "    where bs.database_name = ?1 and (bs.database_backup_lsn = (select lsn from full_dump) or" +
                    " bs.database_backup_lsn = (select backup_lsn from full_dump) or" +
                    " bs.first_lsn = (select lsn from full_dump)) and bs.backup_start_date >= (select date from full_dump)",
            nativeQuery = true)
    List<Object[]> getDumpsByDatabase(String databaseName);

    @Query(
            value = "select @@version",
            nativeQuery = true)
    String getVersion();
}
