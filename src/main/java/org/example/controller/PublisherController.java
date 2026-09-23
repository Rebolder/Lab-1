package org.example.controller;

import java.util.List;

import org.example.dto.PublisherRequest;
import org.example.dto.PublisherResponse;
import org.example.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public List<PublisherResponse> index() {
        return publisherService.findAll();
    }

    @GetMapping("/{id}")
    public PublisherResponse show(@PathVariable Long id) {
        return publisherService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherResponse create(@Valid @RequestBody PublisherRequest request) {
        return publisherService.create(request);
    }

    @PutMapping("/{id}")
    public PublisherResponse update(@PathVariable Long id, @Valid @RequestBody PublisherRequest request) {
        return publisherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        publisherService.delete(id);
    }
}
