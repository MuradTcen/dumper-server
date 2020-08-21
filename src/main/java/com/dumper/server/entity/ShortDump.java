package com.dumper.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ShortDump {

    private BigDecimal fLsn;
    private BigDecimal lLsn;
    private BigDecimal cLsn;
    private BigDecimal dLsn;
    private String name;
    private char type;

    public static ShortDump of(Dump dump) {
        return new ShortDump(
                dump.getFirstLsn(),
                dump.getLastLsn(),
                dump.getDatabaseBackupLsn(),
                dump.getCheckpointLsn(),
                dump.getFilename(),
                dump.getType());
    }
}
