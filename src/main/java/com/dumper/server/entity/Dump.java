package com.dumper.server.entity;

import lombok.Data;

@Data
public class Dump {

    private String filename;
    private int databaseBackupLsn;
}
