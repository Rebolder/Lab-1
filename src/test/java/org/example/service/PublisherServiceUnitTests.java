package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.example.dto.PublisherRequest;
import org.example.dto.PublisherResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.EntityMapper;
import org.example.model.Publisher;
import org.example.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PublisherServiceUnitTests {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private PublisherService publisherService;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisher = publisher();
    }

    @Test
    void returnsAllPublishers() {
        given(entityMapper.toResponse(publisher)).willAnswer(_ -> response(publisher));
        given(publisherRepository.findAll()).willReturn(List.of(publisher));

        List<PublisherResponse> result = publisherService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getEmail()).isEqualTo("hello@cge.example");
    }

    @Test
    void createsPublisher() {
        PublisherRequest request = request("CGE", "hello@cge.example");
        given(entityMapper.toPublisher(request)).willReturn(publisher);
        given(entityMapper.toResponse(publisher)).willAnswer(_ -> response(publisher));
        given(publisherRepository.save(org.mockito.ArgumentMatchers.any(Publisher.class))).willReturn(publisher);

        PublisherResponse result = publisherService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("CGE");
    }

    @Test
    void updatesPublisher() {
        PublisherRequest request = request("Updated CGE", "updated@cge.example");
        given(publisherRepository.findById(1L)).willReturn(Optional.of(publisher));
        given(publisherRepository.save(publisher)).willReturn(publisher);
        given(entityMapper.toResponse(publisher)).willAnswer(_ -> response(publisher));
        org.mockito.Mockito.doAnswer(invocation -> {
            PublisherRequest value = invocation.getArgument(0);
            Publisher target = invocation.getArgument(1);
            target.setName(value.getName());
            target.setEmail(value.getEmail());
            target.setCountry(value.getCountry());
            return null;
        }).when(entityMapper).apply(request, publisher);

        PublisherResponse result = publisherService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Updated CGE");
        assertThat(publisher.getEmail()).isEqualTo("updated@cge.example");
    }

    @Test
    void deletesExistingPublisher() {
        given(publisherRepository.existsById(1L)).willReturn(true);

        publisherService.delete(1L);

        verify(publisherRepository).deleteById(1L);
    }

    @Test
    void throwsWhenPublisherDoesNotExist() {
        given(publisherRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> publisherService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Publisher publisher() {
        Publisher value = new Publisher();
        value.setId(1L);
        value.setName("CGE");
        value.setEmail("hello@cge.example");
        value.setCountry("Czechia");
        return value;
    }

    private PublisherRequest request(String name, String email) {
        PublisherRequest request = new PublisherRequest();
        request.setName(name);
        request.setEmail(email);
        request.setCountry("Czechia");
        return request;
    }

    private PublisherResponse response(Publisher value) {
        PublisherResponse response = new PublisherResponse();
        response.setId(value.getId());
        response.setName(value.getName());
        response.setEmail(value.getEmail());
        response.setCountry(value.getCountry());
        return response;
    }
}
