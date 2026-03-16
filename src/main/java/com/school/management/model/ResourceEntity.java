package com.school.management.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String url;

    @ManyToOne
    private Course course;

    private Instant createdAt = Instant.now();

    public ResourceEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
