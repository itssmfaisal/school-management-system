package com.school.management.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AssignmentEntity assignment;

    @ManyToOne
    private User student;

    private String fileUrl;

    private Instant submittedAt = Instant.now();

    private Double marks;

    public Submission() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AssignmentEntity getAssignment() { return assignment; }
    public void setAssignment(AssignmentEntity assignment) { this.assignment = assignment; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }
}
