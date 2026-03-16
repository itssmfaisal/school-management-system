package com.school.management.controller;

import com.school.management.model.Notice;
import com.school.management.repository.NoticeRepository;
import com.school.management.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeRepository noticeRepository;
    private final MailService mailService;

    public NoticeController(NoticeRepository noticeRepository, MailService mailService) {
        this.noticeRepository = noticeRepository;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Notice n) {
        Notice saved = noticeRepository.save(n);
        // TODO: Notify students in the course via mail
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Notice>> listForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(noticeRepository.findByCourseId(courseId));
    }
}
