package org.example.controller;

import java.util.List;

import org.example.dto.BoardGameRequest;
import org.example.dto.BoardGameResponse;
import org.example.service.BoardGameService;
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
@RequestMapping("/api/games")
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @GetMapping
    public List<BoardGameResponse> index() {
        return boardGameService.findAll();
    }

    @GetMapping("/{id}")
    public BoardGameResponse show(@PathVariable Long id) {
        return boardGameService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardGameResponse create(@Valid @RequestBody BoardGameRequest request) {
        return boardGameService.create(request);
    }

    @PutMapping("/{id}")
    public BoardGameResponse update(@PathVariable Long id, @Valid @RequestBody BoardGameRequest request) {
        return boardGameService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        boardGameService.delete(id);
    }
}
