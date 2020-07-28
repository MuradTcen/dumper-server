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

@RestController
@RequestMapping("/api/dump")
@RequiredArgsConstructor
public class DumpController {

    private final DumpServiceImpl dumpService;
    private final FileStorageService fileStorageService;

    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> getFullDump() throws IOException {
        String filename = LocalDate.now() + "_full.bck";
        dumpService.executeFullDump(filename);

        return ResponseEntity
                .ok()
                .body(fileStorageService.getDump(filename));
    }
}
