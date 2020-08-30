package com.dumper.server.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DumpDaoImpl implements DumpDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param databaseName
     * @return
     */
    @Override
    public List<Object[]> getDumpsByDatabase(String databaseName, LocalDate date) {
        String query = "with full_dump (lsn, backup_lsn, date)\n" +
                "         as (select top 1 b.first_lsn, b.database_backup_lsn, b.backup_start_date\n" +
                "             from backupset as b\n" +
                "             where type = 'D' and\n" +
                "             backup_start_date <= DATEADD(millisecond, -1,DATEADD(hour, 24, CONVERT(DATETIME, :date, 102))) \n" +
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
                "       bf.file_size,\n" +
                "       bs.backup_start_date\n" +
                "from backupmediafamily as bm\n" +
                "         inner join backupset as bs on bm.media_set_id = bs.media_set_id\n" +
                "         inner join backupfile as bf on bs.backup_set_id = bf.backup_set_id\n" +
                "where bs.database_name = :database and bf.filegroup_name = 'PRIMARY'\n" +
                "  and ((bs.database_backup_lsn = (select lsn from full_dump) and bs.type <> 'D') or\n" +
                "       bs.first_lsn = (select lsn from full_dump) or\n" +
                "       bs.database_backup_lsn = (select backup_lsn from full_dump) \n" +
                "    )\n" +
                "  and bs.backup_start_date >= (select date from full_dump)";

        return entityManager.createNativeQuery(query)
                .setParameter("database", databaseName)
                .setParameter("date", date)
                .getResultList();
    }

    /**
     * @return
     */
    @Override
    public String getVersion() {
        return entityManager.createNativeQuery("select @@version").getSingleResult().toString();
    }
}
