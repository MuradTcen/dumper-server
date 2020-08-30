package com.dumper.server.controller;

import com.dumper.server.entity.Dump;
import com.dumper.server.service.FileStorageService;
import com.dumper.server.service.impl.DumpServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dump")
@RequiredArgsConstructor
@Slf4j
public class DumpController {

    private final DumpServiceImpl dumpService;
    private final FileStorageService fileStorageService;

    @GetMapping(path = "version")
    public ResponseEntity<Integer> getVersion() {
        return ResponseEntity
                .ok()
                .body(dumpService.getVersion());
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

    @GetMapping(path = "list")
    public ResponseEntity<List<Dump>> getActualList(@RequestParam(defaultValue = "TestDB") String databaseName,
                                                    @RequestParam(required = false) String date) {
        if (date == null) {
            date = LocalDate.now().toString();
        }
        return ResponseEntity
                .ok()
                .body(dumpService.getActualDumpsByDatabaseName(databaseName, date));
    }

}
