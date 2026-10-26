package org.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.model.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
class PublisherRepositoryUnitTests {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void savesAndFindsPublisherById() {
        Publisher publisher = publisher("CGE", "hello@cge.example", "Czechia");

        Publisher saved = publisherRepository.save(publisher);

        assertThat(saved.getId()).isNotNull();
        assertThat(publisherRepository.findById(saved.getId())).contains(saved);
    }

    @Test
    void findsPublisherByEmailExistence() {
        publisherRepository.save(publisher("Stonemaier", "info@stonemaier.example", "USA"));

        assertThat(publisherRepository.existsByEmail("info@stonemaier.example")).isTrue();
        assertThat(publisherRepository.existsByEmail("missing@example.com")).isFalse();
    }

    @Test
    void deletesPublisher() {
        Publisher saved = publisherRepository.save(publisher("A", "a@example.com", "Germany"));

        publisherRepository.deleteById(saved.getId());

        assertThat(publisherRepository.findById(saved.getId())).isEmpty();
    }

    private Publisher publisher(String name, String email, String country) {
        Publisher publisher = new Publisher();
        publisher.setName(name);
        publisher.setEmail(email);
        publisher.setCountry(country);
        return publisher;
    }
}
