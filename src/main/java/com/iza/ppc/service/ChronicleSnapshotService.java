package com.iza.ppc.service;

import com.iza.ppc.model.Program;
import com.iza.ppc.model.TvShow;
import com.iza.ppc.repository.ChronicleRecord;
import com.iza.ppc.model.ChronicleSnapshot;
import com.iza.ppc.repository.ChronicleRecordRepository;
import com.iza.ppc.util.EpgUrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for operations with chronicle snapshots ({@link ChronicleSnapshot})
 *
 * @author Zakhar Izverov
 * created on 06.10.2021
 */

@Service
public class ChronicleSnapshotService {

    private static final Logger LOG = LoggerFactory.getLogger(ChronicleSnapshotService.class);

    private final RestTemplate restTemplate;
    private final EpgUrlBuilder urlBuilder;
    private final ChronicleRecordRepository chronicleRecordRepository;
    private final int snapshotDepth;

    public ChronicleSnapshotService(RestTemplateBuilder restTemplateBuilder,
                                    EpgUrlBuilder urlBuilder,
                                    ChronicleRecordRepository chronicleRecordRepository,
                                    @Value("${app.epg.connect.timeout.sec}") int connectTimeout,
                                    @Value("${app.epg.read.timeout.sec}") int readTimeout,
                                    @Value("${app.epg.chronicle.depth}") int snapshotDepth) {

        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(connectTimeout))
                .setReadTimeout(Duration.ofSeconds(readTimeout))
                .build();

        this.urlBuilder = urlBuilder;
        this.chronicleRecordRepository = chronicleRecordRepository;
        this.snapshotDepth = snapshotDepth;
    }

    /**
     * Gets all chronicle records ({@link ChronicleRecord}) from the database and
     * converts them to the list of chronicle snapshots ({@link ChronicleSnapshot})
     *
     * @return a list of ChronicleSnapshots for all chronicle records
     */
    public List<ChronicleSnapshot> getChronicle() {
        return mapRecordsToSnapshots(chronicleRecordRepository.findAll());
    }

    /**
     * Gets chronicle records ({@link ChronicleRecord}) matching the specified plan date
     * and domain from the database and converts them to the list of
     * chronicle snapshots ({@link ChronicleSnapshot})
     *
     * @param planDate date of program plan
     * @param domain domain of program plan
     * @return a list of ChronicleSnapshots for matching chronicle records
     */
    public List<ChronicleSnapshot> getChronicle(LocalDate planDate, String domain) {
        return mapRecordsToSnapshots(chronicleRecordRepository.findByPlanDateAndDomain(planDate, domain));
    }

    /**
     * Fetches chronicle snapshots ({@link ChronicleSnapshot}) for each of
     * the upcoming N days, starting from today and persists information
     * about the program plans into the database as chronicle records ({@link ChronicleRecord})
     * Number of days is configurable via app properties (app.epg.chronicle.depth)
     *
     * @param domain domain of program plan
     */
    public void fetchSnapshots(String domain) {

        // If the repository already contains data, obtained today, skip fetching
        if (chronicleRecordRepository.findBySnapshotDate(LocalDate.now()).size() > 0) {
            return;
        }

        LocalDate planDate = LocalDate.now();

        for (int i = 0; i < snapshotDepth ; i++) {
            saveSnapshot(createSnapshot(planDate, domain));

            planDate = planDate.plusDays(1);
        }
    }

    private static List<ChronicleSnapshot> mapRecordsToSnapshots(List<ChronicleRecord> records) {

        Map<String, Map<LocalDate, List<ChronicleSnapshot>>> groupedRecords = records.stream()
                .collect(Collectors.groupingBy(ChronicleRecord::getDomain,
                        Collectors.groupingBy(ChronicleRecord::getPlanDate,
                                Collectors.collectingAndThen(Collectors.toList(),
                                        ChronicleSnapshotService::mapRecordsToSnapshotsPerDate))));

        List<ChronicleSnapshot> mergedList = new ArrayList<>(20);

        groupedRecords.forEach((key, value) -> value.forEach((innerKey, innerValue) -> mergedList.addAll(innerValue)));

        return mergedList;
    }

    private static List<ChronicleSnapshot> mapRecordsToSnapshotsPerDate(List<ChronicleRecord> records) {

        Map<LocalDate, List<ChronicleRecord>> mapBySnapshotDate
                = records.stream().collect(Collectors.groupingBy(ChronicleRecord::getSnapshotDate));

        List<ChronicleSnapshot> result = new ArrayList<>();

        for (LocalDate snapshotDate : mapBySnapshotDate.keySet()) {
            ChronicleSnapshot snapshot = new ChronicleSnapshot();
            snapshot.setSnapshotDate(snapshotDate);
            snapshot.setPlanDate(mapBySnapshotDate.get(snapshotDate).get(0).getPlanDate());
            snapshot.setDomain(mapBySnapshotDate.get(snapshotDate).get(0).getDomain());

            snapshot.setPrograms(mapBySnapshotDate.get(snapshotDate).stream().map(record -> {
                Program program = new Program();
                program.setId(record.getProgramId());
                program.setTitle(record.getProgramTitle());
                program.setTvShow(new TvShow(record.getShowId(), record.getShowTitle()));

                return program;
            }).collect(Collectors.toCollection(ArrayList::new)));

            result.add(snapshot);
        }

        result.sort(Comparator.comparing(ChronicleSnapshot::getSnapshotDate).reversed());
        return result;
    }

    private ChronicleSnapshot createSnapshot(LocalDate planDate, String domain) {

        ChronicleSnapshot chronicleSnapshot = null;
        try {
            chronicleSnapshot = restTemplate
                    .getForObject(URI.create(urlBuilder.buildEpgUrl(planDate, domain)), ChronicleSnapshot.class);
        } catch (RestClientException e) {
            LOG.error("TVProgramPlanChronicle : error getting EPG snapshot for date: {}, domain: {}", planDate, domain);
        }

        if (chronicleSnapshot != null) {
            chronicleSnapshot.setSnapshotDate(LocalDate.now());
            chronicleSnapshot.setPlanDate(planDate);
            chronicleSnapshot.setDomain(domain);
        }

        return chronicleSnapshot;
    }

    private void saveSnapshot(ChronicleSnapshot chronicleSnapshot) {
        if (chronicleSnapshot == null) {
            return;
        }

        chronicleSnapshot.getPrograms().forEach(program -> chronicleRecordRepository
                .save(new ChronicleRecord(chronicleSnapshot.getPlanDate(), chronicleSnapshot.getSnapshotDate(),
                        chronicleSnapshot.getDomain(), program.getId(), program.getTitle(), program.getTvShow().getId(),
                        program.getTvShow().getTitle())));
    }
}
