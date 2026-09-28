package org.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.example.dto.BoardGameResponse;
import org.example.dto.PublisherResponse;
import org.example.service.BoardGameService;
import org.example.service.PublisherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest({PublisherController.class, BoardGameController.class})
class ControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublisherService publisherService;

    @MockitoBean
    private BoardGameService boardGameService;

    @Test
    void getsPublishers() throws Exception {
        PublisherResponse publisher = publisher();
        given(publisherService.findAll()).willReturn(List.of(publisher));

        mockMvc.perform(get("/api/publishers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("CGE"));
    }

    @Test
    void createsPublisher() throws Exception {
        PublisherResponse publisher = publisher();
        given(publisherService.create(any())).willReturn(publisher);

        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"CGE","email":"hello@cge.example","country":"Czechia"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("hello@cge.example"));
    }

    @Test
    void rejectsInvalidPublisher() throws Exception {
        mockMvc.perform(post("/api/publishers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"","email":"invalid","country":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletesPublisher() throws Exception {
        willDoNothing().given(publisherService).delete(1L);

        mockMvc.perform(delete("/api/publishers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getsGames() throws Exception {
        BoardGameResponse game = game("catan", "Catan");
        given(boardGameService.findAll()).willReturn(List.of(game));

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].slug").value("catan"))
                .andExpect(jsonPath("$[0].publisherName").value("CGE"));
    }

    @Test
    void createsGame() throws Exception {
        BoardGameResponse game = game("catan", "Catan");
        given(boardGameService.create(any())).willReturn(game);

        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"publisherId":1,"slug":"catan","name":"Catan","description":"Trade and build","minPlayers":3,"maxPlayers":4,"playTimeMinutes":75}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("catan"))
                .andExpect(jsonPath("$.minPlayers").value(3));
    }

    @Test
    void updatesGame() throws Exception {
        BoardGameResponse game = game("catan-updated", "Updated Catan");
        given(boardGameService.update(any(Long.class), any())).willReturn(game);

        mockMvc.perform(put("/api/games/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"publisherId":1,"slug":"catan-updated","name":"Updated Catan","description":"Trade and build","minPlayers":3,"maxPlayers":4,"playTimeMinutes":75}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Catan"));
    }

    @Test
    void rejectsInvalidGame() throws Exception {
        mockMvc.perform(post("/api/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"publisherId":1,"slug":"","name":"","description":"","minPlayers":0,"maxPlayers":21,"playTimeMinutes":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletesGame() throws Exception {
        willDoNothing().given(boardGameService).delete(1L);

        mockMvc.perform(delete("/api/games/1"))
                .andExpect(status().isNoContent());
    }

    private PublisherResponse publisher() {
        PublisherResponse response = new PublisherResponse();
        response.setId(1L);
        response.setName("CGE");
        response.setEmail("hello@cge.example");
        response.setCountry("Czechia");
        return response;
    }

    private BoardGameResponse game(String slug, String name) {
        BoardGameResponse response = new BoardGameResponse();
        response.setId(1L);
        response.setPublisherId(1L);
        response.setPublisherName("CGE");
        response.setSlug(slug);
        response.setName(name);
        response.setDescription("Trade and build");
        response.setMinPlayers(3);
        response.setMaxPlayers(4);
        response.setPlayTimeMinutes(75);
        return response;
    }
}
