package org.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.model.BoardGame;
import org.example.model.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BoardGameRepositoryUnitTests {

    @Autowired
    private BoardGameRepository boardGameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void findsGameBySlug() {
        Publisher publisher = publisherRepository.save(publisher());
        BoardGame game = boardGameRepository.save(game(publisher, "catan"));

        assertThat(boardGameRepository.findBySlug("catan")).contains(game);
    }

    @Test
    void findsGamesByPublisherId() {
        Publisher publisher = publisherRepository.save(publisher());
        boardGameRepository.save(game(publisher, "catan"));
        boardGameRepository.save(game(publisher, "azul"));

        assertThat(boardGameRepository.findByPublisherId(publisher.getId())).hasSize(2);
    }

    @Test
    void checksSlugExistence() {
        Publisher publisher = publisherRepository.save(publisher());
        boardGameRepository.save(game(publisher, "wingspan"));

        assertThat(boardGameRepository.existsBySlug("wingspan")).isTrue();
        assertThat(boardGameRepository.existsBySlug("missing")).isFalse();
    }

    private Publisher publisher() {
        Publisher publisher = new Publisher();
        publisher.setName("BoardGame Publisher");
        publisher.setEmail("publisher@example.com");
        publisher.setCountry("Czechia");
        return publisher;
    }

    private BoardGame game(Publisher publisher, String slug) {
        BoardGame game = new BoardGame();
        game.setPublisher(publisher);
        game.setSlug(slug);
        game.setName("Board game " + slug);
        game.setDescription("A strategy game");
        game.setMinPlayers(2);
        game.setMaxPlayers(4);
        game.setPlayTimeMinutes(60);
        return game;
    }
}
