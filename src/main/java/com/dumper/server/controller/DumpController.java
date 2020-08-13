package com.dumper.server.controller;

import com.dumper.server.service.FileStorageService;
import com.dumper.server.service.impl.DumpServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/dump")
@RequiredArgsConstructor
public class DumpController {

    private final DumpServiceImpl dumpService;
    private final FileStorageService fileStorageService;

    private final static String FULL_POSTFIX = "_full.bck";
    private final static String DIFFERENTIAL_POSTFIX = "_differential.bck";

    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getFullDump() throws IOException {
        String filename = LocalDate.now() + FULL_POSTFIX;
        dumpService.executeFullDump(filename);

        return ResponseEntity
                .ok()
                .body(fileStorageService.getDump(filename));
    }

    @GetMapping(path = "diff", produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getDifferentialDump() throws IOException {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH")) + DIFFERENTIAL_POSTFIX;
        dumpService.executeDifferentialDump(filename);

        return ResponseEntity
                .ok()
                .body(fileStorageService.getDump(filename));
    }

    @GetMapping(path = "restore")
    public ResponseEntity<String> restoreFullDump() {
        String filename = LocalDate.now() + FULL_POSTFIX;
        dumpService.executeRestoreFullDump(filename);

        return ResponseEntity
                .ok().body("Full dump restored");
    }

    @GetMapping(path = "restore-diff")
    public ResponseEntity<String> restoreDifferentialDump() {
        String filename = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH")) + DIFFERENTIAL_POSTFIX;
        dumpService.executeRestoreDifferentialDump(filename);

        return ResponseEntity
                .ok().body("Differential dump restored");
    }
}
