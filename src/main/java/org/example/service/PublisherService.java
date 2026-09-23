package org.example.service;

import java.util.List;

import org.example.dto.PublisherRequest;
import org.example.dto.PublisherResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.EntityMapper;
import org.example.model.Publisher;
import org.example.repository.PublisherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final EntityMapper entityMapper;

    public PublisherService(PublisherRepository publisherRepository, EntityMapper entityMapper) {
        this.publisherRepository = publisherRepository;
        this.entityMapper = entityMapper;
    }

    @Transactional(readOnly = true)
    public List<PublisherResponse> findAll() {
        return publisherRepository.findAll().stream()
                .map(entityMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PublisherResponse findById(Long id) {
        return entityMapper.toResponse(getPublisher(id));
    }

    @Transactional
    public PublisherResponse create(PublisherRequest request) {
        Publisher saved = publisherRepository.save(entityMapper.toPublisher(request));
        return entityMapper.toResponse(saved);
    }

    @Transactional
    public PublisherResponse update(Long id, PublisherRequest request) {
        Publisher publisher = getPublisher(id);
        entityMapper.apply(request, publisher);
        return entityMapper.toResponse(publisherRepository.save(publisher));
    }

    @Transactional
    public void delete(Long id) {
        if (!publisherRepository.existsById(id)) {
            throw new ResourceNotFoundException("Издатель не найден: " + id);
        }
        publisherRepository.deleteById(id);
    }

    public Publisher getPublisher(Long id) {
        return publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Издатель не найден: " + id));
    }
}
