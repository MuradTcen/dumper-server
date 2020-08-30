package com.dumper.server.service.impl;

import com.dumper.server.dao.DumpDaoImpl;
import com.dumper.server.entity.Dump;
import com.dumper.server.entity.ShortDump;
import com.dumper.server.enums.DumpType;
import com.dumper.server.service.DumpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class DumpServiceImpl implements DumpService {

    private final DumpDaoImpl dao;

    /**
     * ПОлучение года версии ms sql
     * @return год версии
     */
    @Override
    public int getVersion() {
        String version = dao.getVersion();

        Pattern pattern = Pattern.compile("Server \\d{4}");
        Matcher matcher = pattern.matcher(version);

        return matcher.find() ? Integer.parseInt(matcher.group().split(" ")[1]) : -1;
    }

    /**
     * Получение списка актуальных дампов
     * @param databaseName название БД
     * @param dateString дата вида ГГГГ-ММ-ДД актуальности дампов
     * @return отфильтрованный список дампов относительно даты актуальности
     */
    @Override
    public List<Dump> getActualDumpsByDatabaseNameAndDate(String databaseName, String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        List<Dump> dumps = getDumps(databaseName, date);
        if (dumps.isEmpty()) return dumps;
        log.info("Got dumps: ");
        dumps.stream().map(d -> "" + ShortDump.of(d)).forEach(x -> log.info("" + x));

        return getFilteredDumps(dumps);
    }

    /**
     * Получения списка дампов
     * @param databaseName название БД
     * @param date дата актуальности
     * @return список дампов относительно даты актуальности
     */
    @Override
    public List<Dump> getDumps(String databaseName, LocalDate date) {
        log.info(String.format("Getting dumps for database %s and date %s", databaseName, date.toString()));
        return dao.getDumpsByDatabaseAndDate(databaseName, date)
                .stream()
                .map(d -> Dump.of(d))
                .sorted(Dump::compareByFirstLsn)
                .collect(Collectors.toList());
    }

    private List<Dump> getFilteredDumps(List<Dump> dumps) {
        List<Dump> result = new ArrayList<>();

        Dump fullDump = getFullDump(dumps);
        result.add(fullDump);

        List<Dump> logs = dumps.stream()
                .filter(d -> d.getType() == DumpType.L.getName())
                .collect(Collectors.toList());

        Dump differentialDump = getDifferentialDump(dumps);
        if (differentialDump != null) {
            result.add(differentialDump);
            if (logs.size() > 0) {
                result.addAll(getFilteredTransactionalLog(logs));
            }
        }

        log.info(String.format("Received %s filtered dumps: ", result.size()));
        result.stream().map(d -> "" + ShortDump.of(d)).forEach(x -> log.info("" + x));
        return result;
    }

    private Dump getFullDump(List<Dump> dumps) {
        return dumps.stream()
                .filter(d -> d.getType() == DumpType.D.getName())
                .max(Dump::compareByFirstLsn)
                .orElse(null);
    }

    private Dump getDifferentialDump(List<Dump> dumps) {
        return dumps.stream()
                .filter(d -> d.getType() == DumpType.I.getName())
                .max(Dump::compareByFirstLsn)
                .orElse(null);
    }

    private List<Dump> getFilteredTransactionalLog(List<Dump> logs) {
        List<Dump> result = new ArrayList<>();

        if (logs.isEmpty()) {
            return result;
        }

        result.add(logs.get(0));

        for (Dump dump : logs) {
            if (result.get(result.size() - 1).equalsLastLsnToFirst(dump)) {
                result.add(dump);
            }
        }

        return result;
    }
}
