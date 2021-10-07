package com.iza.ppc.controller;

import com.iza.ppc.model.ChronicleSnapshot;
import com.iza.ppc.service.ChronicleSnapshotService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for the TvProgramPlanChronicle app
 *
 * @author Zakhar Izverov
 * created on 04.10.2021
 */

@SuppressWarnings("unused")
@RestController
public class ChronicleController {

    private final ChronicleSnapshotService chronicleSnapshotService;

    ChronicleController(ChronicleSnapshotService chronicleSnapshotService) {
        this.chronicleSnapshotService = chronicleSnapshotService;
    }

    /**
     * GET mapping to obtain program plan chronicle information as a list of
     * plan snapshots for each of the previous N days
     * The maximum number of days to retrieve (N) is configured via
     * application property app.epg.chronicle.depth
     *
     * @param date date of program plan to retrieve chronicle about
     * @param domain domain of program plan
     * @return a list of ChronicleSnapshots containing program plan information
     */
    @GetMapping(value="/chronicle", produces="application/json;charset=UTF-8")
    List<ChronicleSnapshot> getChronicle(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) String domain) {

        List<ChronicleSnapshot> snapshots;

        if (date != null && domain != null
                && !date.isBefore(LocalDate.now())
                && !date.isAfter(LocalDate.now().plusDays(13))) {

            chronicleSnapshotService.fetchSnapshots(domain);
            snapshots = chronicleSnapshotService.getChronicle(date, domain);

            if (snapshots.size() == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No chronicle info found");
            }
        } else if (date == null && domain == null) {
            snapshots = chronicleSnapshotService.getChronicle();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect parameters");
        }

        return snapshots;
    }
}