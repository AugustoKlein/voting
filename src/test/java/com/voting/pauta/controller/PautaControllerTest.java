package com.voting.pauta.controller;

import com.voting.pauta.service.PautaService;
import com.voting.pauta.stub.PautaStub;
import com.voting.voter.dto.VoterDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Pauta Controller Test")
@WebMvcTest(PautaController.class)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PautaService pautaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class Create {
        @Test
        @DisplayName("Should create pauta and return 201")
        public void createSuccess() throws Exception {
            when(pautaService.create(any())).thenReturn(1L);

            mockMvc.perform(post("/pauta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.pautaRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/pauta/1"));
        }

        @Test
        @DisplayName("Should not create pauta and return 500")
        public void createFail() throws Exception {
            when(pautaService.create(any())).thenThrow(new IllegalArgumentException());

            mockMvc.perform(post("/pauta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.pautaRequest())))
                    .andExpect(status().isInternalServerError())
                    .andExpect(header().doesNotExist("Location"));
        }
    }

    @Nested
    class Vote {
        @Test
        @DisplayName("Should vote pauta and return 200")
        public void voteSuccess() throws Exception {
            doNothing().when(pautaService).vote(anyLong(), any(VoterDto.class));

            mockMvc.perform(put("/pauta/{id}/vote", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.pautaVoteRequest())))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 500 when exception is thrown")
        public void findFail() throws Exception {
            doThrow(new IllegalArgumentException()).when(pautaService).vote(anyLong(), any(VoterDto.class));

            mockMvc.perform(put("/pauta/{id}/vote", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.pautaVoteRequest())))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class OpenSession {
        @Test
        @DisplayName("Should open pauta for voting and return 200")
        public void openSuccess() throws Exception {
            doNothing().when(pautaService).openById(anyLong(), any(LocalDateTime.class));

            mockMvc.perform(put("/pauta/{id}/open-session", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.openPautaRequest())))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 500 when exception is thrown")
        public void openFail() throws Exception {
            doThrow(new IllegalArgumentException()).when(pautaService).openById(anyLong(), any(LocalDateTime.class));

            mockMvc.perform(put("/pauta/{id}/open-session", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(PautaStub.openPautaRequest())))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class CloseSession {
        @Test
        @DisplayName("Should close pauta for voting and return 200")
        public void closeSuccess() throws Exception {
            when(pautaService.closeById(anyLong())).thenReturn(PautaStub.pautaDto());

            mockMvc.perform(put("/pauta/{id}/close-session", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 500 when exception is thrown")
        public void closeFail() throws Exception {
            doThrow(new IllegalArgumentException()).when(pautaService).closeById(anyLong());

            mockMvc.perform(put("/pauta/{id}/close-session", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class Find {
        @Test
        @DisplayName("Should find pauta and return 200")
        public void findSuccess() throws Exception {
            when(pautaService.find(any())).thenReturn(PautaStub.pautaDto());

            mockMvc.perform(get("/pauta/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 500 when exception is thrown")
        public void findFail() throws Exception {
            when(pautaService.find(any())).thenThrow(new IllegalArgumentException());

            mockMvc.perform(get("/pauta/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }
}
