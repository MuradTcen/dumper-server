package com.dumper.server.controller;

import com.dumper.server.entity.Dump;
import com.dumper.server.enums.Query;
import com.dumper.server.service.FileStorageService;
import com.dumper.server.service.impl.DumpServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/dump")
@RequiredArgsConstructor
@Slf4j
public class DumpController {

    private final DumpServiceImpl dumpService;
    private final FileStorageService fileStorageService;
    private final ObjectMapper mapper;

    private final static String FULL_POSTFIX = "_full.bck";
    private final static String DIFFERENTIAL_POSTFIX = "_differential.bck";

    @GetMapping(path = "version")
    public ResponseEntity<Integer> getVersion() throws IOException {
        return ResponseEntity
                .ok()
                .body(dumpService.getVersion());
    }

    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getFullDump() throws IOException {
        String filename = LocalDate.now() + FULL_POSTFIX;
        dumpService.executeQuery(filename, Query.FULL_BACKUP);

        return ResponseEntity
                .ok()
                .body(fileStorageService.getDump(filename));
    }

    @GetMapping(path = "diff", produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getDifferentialDump() throws IOException {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH")) + DIFFERENTIAL_POSTFIX;
        dumpService.executeQuery(filename, Query.DIFFERENTIAL_BACKUP);

        return ResponseEntity
                .ok()
                .body(fileStorageService.getDump(filename));
    }

    @GetMapping(path = "file", produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getDump(String filename) {
        try {
            return ResponseEntity
                    .ok()
                    .body(fileStorageService.getDump(filename));
        } catch (IOException e) {
            log.error("Error during downloading " + filename + e.getMessage());
            // todo: исправить!
            return null;
        }
    }

    @GetMapping(path = "restore")
    public ResponseEntity<String> restoreFullDump() {
        String filename = LocalDate.now() + FULL_POSTFIX;
        dumpService.executeQuery(filename, Query.RESTORE);

        return ResponseEntity
                .ok().body("Full dump restored");
    }

    @GetMapping(path = "restore-diff")
    public ResponseEntity<String> restoreDifferentialDump() {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH")) + DIFFERENTIAL_POSTFIX;
        dumpService.executeQuery(filename, Query.RESTORE);

        return ResponseEntity
                .ok().body("Differential dump restored");
    }

    @GetMapping(path = "list")
    public ResponseEntity<List<Dump>> getActualList(@RequestParam(defaultValue = "TestDB") String databaseName) {
        return ResponseEntity
                .ok()
                .body(dumpService.getActualDumpsByDatabaseName(databaseName));
    }

}
