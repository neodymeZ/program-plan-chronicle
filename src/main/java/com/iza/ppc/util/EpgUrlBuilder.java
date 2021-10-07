package com.iza.ppc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class for constructing URLs to fetch data from the
 * electronic program guide (EPG) service
 *
 * @author Zakhar Izverov
 * created on 05.10.2021
 */

@Component
public class EpgUrlBuilder {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static final AtomicLong queryHash = new AtomicLong();

    private final String baseUrl;
    private final String filters;
    private final String query;

    public EpgUrlBuilder(@Value("${app.epg.connection.url.basepath}") String baseUrl,
                         @Value("${app.epg.connection.url.filters}") String filters,
                         @Value("${app.epg.connection.url.query}") String query) {
        this.baseUrl = baseUrl;
        this.filters = filters;
        this.query = query;
    }

    /**
     * Builds URL for using EPG from the elements, specified in the application properties file -
     * base URL, variables and query.
     * Replaces placeholders for plan date and domain with the specified parameters.
     * Iterates a numeric query hash for each build and appends it to the URL
     *
     * @param planDate date of program plan
     * @param domain domain of program plan
     * @return built URL
     */

    public String buildEpgUrl(LocalDate planDate, String domain) {

        queryHash.getAndIncrement();

        return baseUrl + "&variables=" +
                URLEncoder.encode(filters
                                .replaceAll("%planDate%", LocalDateTime.of(planDate, LocalTime.MIDNIGHT).format(DATE_FORMAT))
                                .replaceAll("%planDomain%", domain),
                        StandardCharsets.UTF_8) +
                "&query=" +
                URLEncoder.encode(query, StandardCharsets.UTF_8).replace("+", "%20") +
                "&queryhash=" +
                queryHash;
    }
}
