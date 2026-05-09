package com.tp.todolist.dto;

import java.time.LocalDate;

public class ActivitiesDTO {
    private Long acId;
    private LocalDate acDueDate;
    private String acDescription;
    private Long acTagId;
    private Boolean acCompleted;

    public ActivitiesDTO() {
    }

    public ActivitiesDTO(Long acId, LocalDate acDueDate, String acDescription, Long acTagId, Boolean acCompleted) {
        this.acId = acId;
        this.acDueDate = acDueDate;
        this.acDescription = acDescription;
        this.acTagId = acTagId;
        this.acCompleted = acCompleted;
    }

    public Long getAcId() {
        return acId;
    }

    public LocalDate getAcDueDate() {
        return acDueDate;
    }

    public void setAcDueDate(LocalDate acDueDate) {
        this.acDueDate = acDueDate;
    }

    public String getAcDescription() {
        return acDescription;
    }

    public void setAcDescription(String acDescription) {
        this.acDescription = acDescription;
    }

    public Long getAcTagId() {
        return acTagId;
    }

    public void setAcTagId(Long acTagId) {
        this.acTagId = acTagId;
    }

    public Boolean getAcCompleted() {
        return acCompleted;
    }

    public void setAcCompleted(Boolean acCompleted) {
        this.acCompleted = acCompleted;
    }

    public void setAcId(Long acId) {
        this.acId = acId;
    }
}
