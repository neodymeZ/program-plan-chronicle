package com.iza.ppc.repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class representing a chronicle record to store program plan information to the database
 *
 * @author Zakhar Izverov
 * created on 05.10.2021
 */

@SuppressWarnings("unused")
@Entity
@Table(name = "CHRONICLE_RECORDS",
        indexes = {
            @Index(name = "IDX1", columnList = "planDate, domain"),
            @Index(name = "IDX2", columnList = "snapshotDate")
        },
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"domain", "planDate", "snapshotDate", "programId", "showId", "showTitle"})
        }
)
public class ChronicleRecord {

    private @Id @GeneratedValue Long id;

    private LocalDate planDate;
    private LocalDate snapshotDate;
    private String domain;

    private String programId;
    private String programTitle;
    private String showId;
    private String showTitle;

    public ChronicleRecord() {
    }

    public ChronicleRecord(LocalDate planDate,
                           LocalDate snapshotDate,
                           String domain,
                           String programId,
                           String programTitle,
                           String showId,
                           String showTitle) {
        this.planDate = planDate;
        this.snapshotDate = snapshotDate;
        this.domain = domain;
        this.programId = programId;
        this.programTitle = programTitle;
        this.showId = showId;
        this.showTitle = showTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDate planDate) {
        this.planDate = planDate;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChronicleRecord that = (ChronicleRecord) o;
        return planDate.equals(that.planDate) && snapshotDate.equals(that.snapshotDate) && domain.equals(that.domain)
                && Objects.equals(programId, that.programId) && Objects.equals(showId, that.showId)
                && Objects.equals(showTitle, that.showTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planDate, snapshotDate, domain, programId, showId, showTitle);
    }

    @Override
    public String toString() {
        return "ChronicleRecord{" +
                "id=" + id +
                ", planDate=" + planDate +
                ", snapshotDate=" + snapshotDate +
                ", domain='" + domain + '\'' +
                ", programId='" + programId + '\'' +
                ", programTitle='" + programTitle + '\'' +
                ", showId='" + showId + '\'' +
                ", showTitle='" + showTitle + '\'' +
                '}';
    }
}
