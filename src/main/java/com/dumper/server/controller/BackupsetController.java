package com.dumper.server.controller;

import com.dumper.server.entity.Backupset;
import com.dumper.server.service.impl.BackupsetServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/backupset/")
@RequiredArgsConstructor
@Slf4j
public class BackupsetController {

    private final BackupsetServiceImpl backupsetService;


    @GetMapping
    public List<Backupset> getBackups(@RequestParam(defaultValue = "2020-08-03-00-00") String stringDatetime,
                                      @RequestParam(defaultValue = "TestDB") String databaseName) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        LocalDateTime dateTime = LocalDateTime.parse(stringDatetime, formatter);
        return backupsetService.getAllByDatabaseNameAndBackupStartDate(databaseName, dateTime);
    }

    @GetMapping(value = "write")
    public void writeBackups(@RequestParam(defaultValue = "2020-08-03-00-00") String stringDatetime,
                             @RequestParam(defaultValue = "TestDB") String databaseName) {

        backupsetService.saveBackupsByDatabaseNameAndDateTime(databaseName, stringDatetime);
    }
}
