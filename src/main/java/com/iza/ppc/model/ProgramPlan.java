package com.iza.ppc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramPlan {

    private LocalDateTime planDate;
    private LocalDateTime viewDate;

    private List<Program> programs;

    public ProgramPlan() {
    }

    public LocalDateTime getPlanDate() {
        return planDate;
    }

    public void setPlanDate(LocalDateTime planDate) {
        this.planDate = planDate;
    }

    public LocalDateTime getViewDate() {
        return viewDate;
    }

    public void setViewDate(LocalDateTime viewDate) {
        this.viewDate = viewDate;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    @Override
    public String toString() {
        return "ProgramPlan{" +
                "planDate=" + planDate +
                ", viewDate=" + viewDate +
                ", programs=" + programs +
                '}';
    }
}
