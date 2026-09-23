package org.example.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.repository.BoardGameRepository;
import org.example.repository.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardGameRepository boardGameRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @BeforeEach
    void clean() {
        boardGameRepository.deleteAll();
        publisherRepository.deleteAll();
    }

    @Test
    void shouldCreatePublisherAndGame() throws Exception {
        String publisherJson = """
                {
                  "name": "Stonemaier Games",
                  "email": "info@stonemaier.example",
                  "country": "США"
                }
                """;

        MvcResult createdPublisher = mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(publisherJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Stonemaier Games"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andReturn();

        Integer publisherId = JsonPath.read(createdPublisher.getResponse().getContentAsString(), "$.id");

        String gameJson = """
                {
                  "publisherId": %d,
                  "slug": "wingspan",
                  "name": "Крылья",
                  "description": "Движок-строительство вокруг коллекции птиц.",
                  "minPlayers": 1,
                  "maxPlayers": 5,
                  "playTimeMinutes": 70
                }
                """.formatted(publisherId);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("wingspan"))
                .andExpect(jsonPath("$.publisherName").value("Stonemaier Games"));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Крылья"));
    }

    @Test
    void shouldReturn404ForMissingGame() throws Exception {
        mockMvc.perform(get("/api/games/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidPublisher() throws Exception {
        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"not-email\",\"country\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateAndDeletePublisher() throws Exception {
        MvcResult created = mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Iello",
                                  "email": "hello@iello.example",
                                  "country": "Франция"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = JsonPath.read(created.getResponse().getContentAsString(), "$.id");

        mockMvc.perform(put("/api/publishers/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "IELLO",
                                  "email": "hello@iello.example",
                                  "country": "Франция"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IELLO"));

        mockMvc.perform(delete("/api/publishers/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/publishers/" + id))
                .andExpect(status().isNotFound());
    }
}
