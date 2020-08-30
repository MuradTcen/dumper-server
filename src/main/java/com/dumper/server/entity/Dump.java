package com.dumper.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Dump {

    private BigDecimal firstLsn;
    private BigDecimal lastLsn;
    private BigDecimal checkpointLsn;
    private BigDecimal databaseBackupLsn;
    private String filename;
    private char type;
    private BigDecimal backupSize;
    private String physicalDrive;
    private String physicalName;
    private BigDecimal fileSize;
    private LocalDate date;

    public int compareByFirstLsn(Dump another) {
        return firstLsn.compareTo(another.getFirstLsn());
    }

    public boolean equalsLastLsnToFirst(Dump next) {
        return lastLsn.compareTo(next.getFirstLsn()) == 0;
    }

    public static Dump of(Object[] object) {
        return new Dump(
                (BigDecimal) object[0],
                (BigDecimal) object[1],
                (BigDecimal) object[2],
                (BigDecimal) object[3],
                (String) object[4],
                (char) object[5],
                (BigDecimal) object[6],
                (String) object[7],
                (String) object[8],
                (BigDecimal) object[9],
                ((Timestamp) object[10]).toLocalDateTime().toLocalDate());
    }

}
