package com.dumper.server.service.impl;

import com.dumper.server.entity.Dump;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
class DumpServiceImplTest {

    @Autowired
    private DumpServiceImpl service;

    @Test
    void getDifferentialDump_whenGetDiffDumps_thenEqualsDump() {
        List<Dump> dumps = getDifferentialDumps();
        Dump actual = service.getDifferentialDump(dumps);

        Dump expected = getDifferentialDump();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getDifferentialDump_whenGetDiffDumps_thenNotEqualsAnotherDump() {
        List<Dump> dumps = getDifferentialDumps();
        Dump actual = service.getDifferentialDump(dumps);

        Dump expected = getAnotherDifferentialDump();

        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void getFilteredTransactionalLog_whenGetLogs_thenEqualsFiltered() {
        List<Dump> dumps = getLogs();
        List<Dump> actual = service.getFilteredTransactionalLog(dumps);

        List<Dump> expected = getCorrectLogs();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFilteredTransactionalLog_whenGetLogs_thenNotEqualsUnfiltered() {
        List<Dump> dumps = getLogs();
        List<Dump> actual = service.getFilteredTransactionalLog(dumps);

        List<Dump> expected = getLogs();

        assertThat(actual).isNotEqualTo(expected);
    }

    @Test
    void getFilteredTransactionalLog_whenGetLog_thenEqualsLog() {
        List<Dump> dumps = new ArrayList() {{
            add(getLog());
        }};
        List<Dump> actual = service.getFilteredTransactionalLog(dumps);

        List<Dump> expected = new ArrayList() {{
            add(getLog());
        }};

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getFilteredTransactionalLog_whenEmptyLog_thenNotEqualsLog() {
        List<Dump> dumps = new ArrayList<>();
        List<Dump> actual = service.getFilteredTransactionalLog(dumps);

        List<Dump> expected = new ArrayList() {{
            add(getLog());
        }};

        assertThat(actual).isNotEqualTo(expected);
    }

    private static Dump getLog() {
        return new Dump(BigDecimal.valueOf(123), BigDecimal.valueOf(143), BigDecimal.ZERO, BigDecimal.ZERO, "A", 'A', BigDecimal.ZERO, "", "", BigDecimal.ZERO);
    }

    private static List<Dump> getLogs() {
        return new ArrayList() {{
            add(new Dump(BigDecimal.valueOf(123), BigDecimal.valueOf(143), BigDecimal.ZERO, BigDecimal.ZERO, "A", 'A', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(143), BigDecimal.valueOf(153), BigDecimal.ZERO, BigDecimal.ZERO, "B", 'B', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(170), BigDecimal.valueOf(190), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'C', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(191), BigDecimal.valueOf(195), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'C', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(153), BigDecimal.valueOf(200), BigDecimal.ZERO, BigDecimal.ZERO, "D", 'D', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
        }};
    }

    private static List<Dump> getCorrectLogs() {
        return new ArrayList() {{
            add(new Dump(BigDecimal.valueOf(123), BigDecimal.valueOf(143), BigDecimal.ZERO, BigDecimal.ZERO, "A", 'A', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(143), BigDecimal.valueOf(153), BigDecimal.ZERO, BigDecimal.ZERO, "B", 'B', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(153), BigDecimal.valueOf(200), BigDecimal.ZERO, BigDecimal.ZERO, "D", 'D', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
        }};
    }

    private static List<Dump> getDifferentialDumps() {
        return new ArrayList() {{
            add(new Dump(BigDecimal.valueOf(123), BigDecimal.valueOf(143), BigDecimal.ZERO, BigDecimal.ZERO, "A", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(143), BigDecimal.valueOf(153), BigDecimal.ZERO, BigDecimal.ZERO, "B", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(200), BigDecimal.valueOf(390), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(191), BigDecimal.valueOf(195), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
            add(new Dump(BigDecimal.valueOf(153), BigDecimal.valueOf(200), BigDecimal.ZERO, BigDecimal.ZERO, "D", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO));
        }};
    }

    private static Dump getDifferentialDump() {
        return new Dump(BigDecimal.valueOf(200), BigDecimal.valueOf(390), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO);
    }

    private static Dump getAnotherDifferentialDump() {
        return new Dump(BigDecimal.valueOf(204), BigDecimal.valueOf(390), BigDecimal.ZERO, BigDecimal.ZERO, "C", 'I', BigDecimal.ZERO, "", "", BigDecimal.ZERO);
    }
}