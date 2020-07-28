package com.dumper.server.service;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

public interface FileStorageService {
    InputStreamResource getDump(String name) throws IOException;
}
