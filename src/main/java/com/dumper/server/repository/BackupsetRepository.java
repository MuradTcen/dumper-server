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
                    "         as (select top 1 b.first_lsn, b.database_backup_lsn, b.backup_start_date\n" +
                    "             from backupset as b\n" +
                    "             where type = 'D'\n" +
                    "             order by backup_start_date desc)\n" +
                    "\n" +
                    "select bs.first_lsn,\n" +
                    "       bs.last_lsn,\n" +
                    "       bs.checkpoint_lsn,\n" +
                    "       bs.database_backup_lsn,\n" +
                    "       bm.physical_device_name,\n" +
                    "       bs.type,\n" +
                    "       bs.backup_size,\n" +
                    "       bf.physical_drive,\n" +
                    "       bf.physical_name,\n" +
                    "       bf.file_size\n" +
                    "from backupmediafamily as bm\n" +
                    "         inner join backupset as bs on bm.media_set_id = bs.media_set_id\n" +
                    "         inner join backupfile as bf on bs.backup_set_id = bf.backup_set_id\n" +
                    "where bs.database_name = ?1 and bf.filegroup_name = 'PRIMARY'\n" +
                    "  and (bs.database_backup_lsn = (select lsn from full_dump) or\n" +
                    "       bs.first_lsn = (select lsn from full_dump) or\n" +
                    "       bs.database_backup_lsn = (select backup_lsn from full_dump)\n" +
                    "    )\n" +
                    "  and bs.backup_start_date >= (select date from full_dump)",
            nativeQuery = true)
    List<Object[]> getDumpsByDatabase(String databaseName);

    @Query(
            value = "select @@version",
            nativeQuery = true)
    String getVersion();
}
