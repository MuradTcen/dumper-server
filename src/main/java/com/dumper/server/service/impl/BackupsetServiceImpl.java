package com.dumper.server.service.impl;

import com.dumper.server.entity.Backupset;
import com.dumper.server.entity.BackupsetTest;
import com.dumper.server.repository.BackupsetRepository;
import com.dumper.server.service.BackupsetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackupsetServiceImpl implements BackupsetService {

    private final BackupsetRepository repository;

    @Value(value = "${api.backupsets.url}")
    private String url;

    private final ObjectMapper mapper;

    @Override
    public List<Backupset> getAllByDatabaseNameAndBackupStartDate(String databaseName, LocalDateTime backupStartDate) {
        return repository.getAllByDatabaseNameAndBackupStartDateAfter(databaseName, backupStartDate);
    }

    @Override
    public void saveBackupsByDatabaseNameAndDateTime(String databaseName, String backupStartDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("stringDatetime", backupStartDate)
                .queryParam("databaseName", databaseName);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        List<BackupsetTest> backupsets = new RestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<BackupsetTest>>() {
                }).getBody();

        log.info("Received backupsets " + backupsets);

        repository.saveAll(backupsets);
    }

}
