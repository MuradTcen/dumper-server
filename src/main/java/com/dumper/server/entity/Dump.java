package com.dumper.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Dump {

    private BigDecimal firstLsn;
    private BigDecimal lastLsn;
    private BigDecimal checkpointLsn;
    private BigDecimal databaseBackupLsn;
    private String filename;
    private char type;

    public int compareByLastLsn(Dump another) {
        return lastLsn.compareTo(another.getLastLsn());
    }

    public static Dump of(Object[] object) {
        return new Dump((BigDecimal) object[0],
                (BigDecimal) object[1],
                (BigDecimal) object[2],
                (BigDecimal) object[3],
                (String) object[4],
                (char) object[5]);
    }

}
