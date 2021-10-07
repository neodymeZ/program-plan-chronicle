package com.iza.ppc.repository;

import com.iza.ppc.model.ChronicleSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA repository interface for persisting chronicle records ({@link ChronicleSnapshot})
 *
 * @author Zakhar Izverov
 * created on 05.10.2021
 */

public interface ChronicleRecordRepository extends JpaRepository<ChronicleRecord, Long> {

    List<ChronicleRecord> findByPlanDateAndDomain(LocalDate planDate, String domain);

    List<ChronicleRecord> findBySnapshotDate(LocalDate snapshotDate);

}
