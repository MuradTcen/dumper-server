package com.dumper.server.service.impl;

import com.dumper.server.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(@Value(value = "${directory:directory}") String directory) {
        this.fileStorageLocation = Paths.get(directory)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            log.error("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public InputStreamResource getDump(String name) throws IOException {
        ClassPathResource file = new ClassPathResource(fileStorageLocation + "/" + name);
        log.info("Getting file: " + file.getPath());

        return new InputStreamResource(file.getInputStream());
    }
}
