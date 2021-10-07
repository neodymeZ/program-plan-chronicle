package com.iza.ppc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iza.ppc.util.ChronicleSnapshotDeserializer;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class representing a program plan for the specific domain and date
 * on a particular date of retrieving (snapshotDate)
 *
 * @author Zakhar Izverov
 * created on 04.10.2021
 */

@SuppressWarnings("unused")
@JsonDeserialize(using = ChronicleSnapshotDeserializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChronicleSnapshot {

    private LocalDate planDate;
    private String domain;

    private LocalDate snapshotDate;

    private int numOfPrograms;
    private ArrayList<Program> programs;

    public ChronicleSnapshot() {
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

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(ArrayList<Program> programs) {
        this.programs = programs;
    }

    public int getNumOfPrograms() {
        if (programs != null) {
            this.numOfPrograms = programs.size();
            return this.numOfPrograms;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "ChronicleSnapshot{" +
                "planDate=" + planDate +
                ", snapshotDate=" + snapshotDate +
                ", domain='" + domain + '\'' +
                ", programs=" + programs +
                ", numOfPrograms=" + numOfPrograms +
                '}';
    }
}
