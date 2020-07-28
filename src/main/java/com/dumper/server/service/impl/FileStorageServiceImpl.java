package com.dumper.server.service.impl;

import com.dumper.server.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value(value = "${directory:directory}")
    private String directory;

    @Override
    public InputStreamResource getDump(String name) throws IOException {
        ClassPathResource dumpFile = new ClassPathResource(directory + name);

        return new InputStreamResource(dumpFile.getInputStream());
    }
}
