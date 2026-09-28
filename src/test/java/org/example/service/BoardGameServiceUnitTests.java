package org.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.example.dto.BoardGameRequest;
import org.example.dto.BoardGameResponse;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.EntityMapper;
import org.example.model.BoardGame;
import org.example.model.Publisher;
import org.example.repository.BoardGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardGameServiceUnitTests {

    @Mock
    private BoardGameRepository boardGameRepository;

    @Mock
    private PublisherService publisherService;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private BoardGameService boardGameService;

    private Publisher publisher;
    private BoardGame game;

    @BeforeEach
    void setUp() {
        publisher = publisher();
        game = game();
    }

    @Test
    void createsGameForExistingPublisher() {
        BoardGameRequest request = request("catan", "Catan");
        given(publisherService.getPublisher(1L)).willReturn(publisher);
        given(entityMapper.toBoardGame(request, publisher)).willReturn(game);
        given(entityMapper.toResponse(game)).willAnswer(_ -> response(game));
        given(boardGameRepository.save(any(BoardGame.class))).willReturn(game);

        BoardGameResponse result = boardGameService.create(request);

        assertThat(result.getSlug()).isEqualTo("catan");
        assertThat(result.getPublisherName()).isEqualTo("CGE");
    }

    @Test
    void returnsAllGames() {
        given(entityMapper.toResponse(game)).willAnswer(_ -> response(game));
        given(boardGameRepository.findAll()).willReturn(List.of(game));

        assertThat(boardGameService.findAll()).hasSize(1);
    }

    @Test
    void updatesGame() {
        BoardGameRequest request = request("catan-updated", "Updated Catan");
        given(boardGameRepository.findById(1L)).willReturn(Optional.of(game));
        given(publisherService.getPublisher(1L)).willReturn(publisher);
        given(boardGameRepository.save(game)).willReturn(game);
        given(entityMapper.toResponse(game)).willAnswer(_ -> response(game));
        org.mockito.Mockito.doAnswer(invocation -> {
            BoardGameRequest value = invocation.getArgument(0);
            BoardGame target = invocation.getArgument(2);
            target.setPublisher(invocation.getArgument(1));
            target.setSlug(value.getSlug());
            target.setName(value.getName());
            target.setDescription(value.getDescription());
            target.setMinPlayers(value.getMinPlayers());
            target.setMaxPlayers(value.getMaxPlayers());
            target.setPlayTimeMinutes(value.getPlayTimeMinutes());
            return null;
        }).when(entityMapper).apply(request, publisher, game);

        BoardGameResponse result = boardGameService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Updated Catan");
        assertThat(game.getSlug()).isEqualTo("catan-updated");
    }

    @Test
    void deletesExistingGame() {
        given(boardGameRepository.existsById(1L)).willReturn(true);

        boardGameService.delete(1L);

        verify(boardGameRepository).deleteById(1L);
    }

    @Test
    void throwsWhenGameDoesNotExist() {
        given(boardGameRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> boardGameService.findById(99L))
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

    private BoardGame game() {
        BoardGame value = new BoardGame();
        value.setId(1L);
        value.setPublisher(publisher);
        value.setSlug("catan");
        value.setName("Catan");
        value.setDescription("Trade and build");
        value.setMinPlayers(3);
        value.setMaxPlayers(4);
        value.setPlayTimeMinutes(75);
        return value;
    }

    private BoardGameRequest request(String slug, String name) {
        BoardGameRequest request = new BoardGameRequest();
        request.setPublisherId(1L);
        request.setSlug(slug);
        request.setName(name);
        request.setDescription("Trade and build");
        request.setMinPlayers(3);
        request.setMaxPlayers(4);
        request.setPlayTimeMinutes(75);
        return request;
    }

    private BoardGameResponse response(BoardGame value) {
        BoardGameResponse response = new BoardGameResponse();
        response.setId(value.getId());
        response.setPublisherId(value.getPublisher().getId());
        response.setPublisherName(value.getPublisher().getName());
        response.setSlug(value.getSlug());
        response.setName(value.getName());
        response.setDescription(value.getDescription());
        response.setMinPlayers(value.getMinPlayers());
        response.setMaxPlayers(value.getMaxPlayers());
        response.setPlayTimeMinutes(value.getPlayTimeMinutes());
        return response;
    }
}
