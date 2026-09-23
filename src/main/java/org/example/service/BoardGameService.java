package org.example.service;

import java.util.List;

import org.example.dto.BoardGameRequest;
import org.example.dto.BoardGameResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.EntityMapper;
import org.example.model.BoardGame;
import org.example.model.Publisher;
import org.example.repository.BoardGameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardGameService {

    private final BoardGameRepository boardGameRepository;
    private final PublisherService publisherService;
    private final EntityMapper entityMapper;

    public BoardGameService(BoardGameRepository boardGameRepository, PublisherService publisherService,
            EntityMapper entityMapper) {
        this.boardGameRepository = boardGameRepository;
        this.publisherService = publisherService;
        this.entityMapper = entityMapper;
    }

    @Transactional(readOnly = true)
    public List<BoardGameResponse> findAll() {
        return boardGameRepository.findAll().stream()
                .map(entityMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardGameResponse findById(Long id) {
        return entityMapper.toResponse(getGame(id));
    }

    @Transactional
    public BoardGameResponse create(BoardGameRequest request) {
        Publisher publisher = publisherService.getPublisher(request.getPublisherId());
        BoardGame saved = boardGameRepository.save(entityMapper.toBoardGame(request, publisher));
        return entityMapper.toResponse(saved);
    }

    @Transactional
    public BoardGameResponse update(Long id, BoardGameRequest request) {
        BoardGame game = getGame(id);
        Publisher publisher = publisherService.getPublisher(request.getPublisherId());
        entityMapper.apply(request, publisher, game);
        return entityMapper.toResponse(boardGameRepository.save(game));
    }

    @Transactional
    public void delete(Long id) {
        if (!boardGameRepository.existsById(id)) {
            throw new ResourceNotFoundException("Игра не найдена: " + id);
        }
        boardGameRepository.deleteById(id);
    }

    private BoardGame getGame(Long id) {
        return boardGameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Игра не найдена: " + id));
    }
}
