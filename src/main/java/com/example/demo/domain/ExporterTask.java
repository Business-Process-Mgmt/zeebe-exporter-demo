package com.example.demo.domain;

import lombok.Data;

import java.time.LocalDate;
@Data
public class ExporterTask {
    private Long taskId;
    private Long processInstanceKey;
    private String name;
    private String taskDefinitionId;
    private LocalDate creationDate;
    private LocalDate completedDate;
    private String taskState;
    private String assigneeUser;
    private String candidateGroups; // Assuming candidate groups can be multiple
    private LocalDate dueDate;
    private String assigneeUserFullName;
    private String completedByUser;
    private String completedByUserFullName;
    private Long childProcessInstanceKey;
    private Long processId;
    private String taskUniqueId;
    private String details;
    private String createdBy;
    private LocalDate createdDate;
    private String modifiedBy;
    private LocalDate modifiedDate;
}
