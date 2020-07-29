package com.dumper.server.service.impl;

import com.dumper.server.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value(value = "${directory:directory}")
    private String directory;

    @Override
    public InputStreamResource getDump(String name) throws IOException {
        try (InputStream dumpFile = new FileInputStream(directory + name)) {
            log.info("Getting file: " + directory + name);
            return new InputStreamResource(dumpFile);
        }
    }
}
