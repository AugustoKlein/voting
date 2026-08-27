package com.voting.pauta.service;

import com.voting.infra.exception.ClosedPautaException;
import com.voting.infra.exception.CreatedPautaException;
import com.voting.infra.exception.OpenedPautaException;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.repository.PautaRepository;
import com.voting.pauta.service.impl.PautaServiceImpl;
import com.voting.pauta.stub.PautaStub;
import com.voting.voter.dto.VoterDto;
import com.voting.voter.service.VoterService;
import com.voting.voter.stub.VoterStub;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Account Service Test")
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {
    public static final String DEFAULT_ERROR_MESSAGE = "something went wrong";
    private static final Long PAUTA_ID = 1L;
    private static final LocalDateTime END_AT = LocalDateTime.now().plusMinutes(10);
    
    @InjectMocks
    private PautaServiceImpl pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private VoterService voterService;

    @Nested
    @DisplayName("Create")
    class Create {

        @Test
        @DisplayName("Should create pauta")
        public void createSuccess() {
            when(pautaRepository.save(any())).thenReturn(PautaStub.pauta());

            Long id = pautaService.create(PautaStub.pautaDto());

            assertEquals(PAUTA_ID, id);
            verify(pautaRepository).save(any());
        }

        @Test
        @DisplayName("Should not create pauta when IllegalArgumentException is thrown")
        public void createFail() {
            when(pautaRepository.save(any())).thenThrow(new IllegalArgumentException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(IllegalArgumentException.class, () -> pautaService.create(PautaStub.pautaDto())).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(pautaRepository).save(any());
        }
    }

    @Nested
    @DisplayName("OpenById")
    class OpenById {

        @Test
        @DisplayName("Should open by pauta id")
        public void openSuccess() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta()));
            when(pautaRepository.save(any())).thenReturn(PautaStub.pauta(PautaStatusEnum.OPEN));

            pautaService.openById(PAUTA_ID, END_AT);

            verify(pautaRepository).findById(anyLong());
            verify(pautaRepository).save(any());
        }

        @Test
        @DisplayName("Should throw OpenedPautaException when pauta voting is already open")
        public void openOpenedPautaException() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.OPEN)));

           assertThrows(OpenedPautaException.class, () -> pautaService.openById(PAUTA_ID, END_AT));

            verify(pautaRepository).findById(anyLong());
            verify(pautaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw ClosedPautaException when pauta voting is already closed")
        public void openClosedPautaException() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.CLOSED)));

            assertThrows(ClosedPautaException.class, () -> pautaService.openById(PAUTA_ID, END_AT));

            verify(pautaRepository).findById(anyLong());
            verify(pautaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should not open pauta when IllegalArgumentException is thrown")
        public void openFindByIdFail() {
            when(pautaRepository.findById(anyLong())).thenThrow(new IllegalArgumentException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(IllegalArgumentException.class, () ->
                            pautaService.openById(PAUTA_ID, END_AT)).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(pautaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Vote")
    class Vote {
        @Test
        @DisplayName("Should vote by pauta id and voter")
        public void voteSuccess() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.OPEN)));

            pautaService.vote(PAUTA_ID, VoterStub.voterDto());

            verify(pautaRepository).findById(anyLong());
            verify(voterService).create(any());
        }

        @Test
        @DisplayName("Should not vote when pauta has not been opened")
        void voteWhenPautaIsCreated() {
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.CREATED)));

            assertThrows(CreatedPautaException.class, () -> pautaService.vote(PAUTA_ID, VoterStub.voterDto()));

            verify(pautaRepository).findById(anyLong());
            verify(voterService, never()).create(any());
        }

        @Test
        @DisplayName("Should not vote when pauta is closed")
        void voteWhenPautaIsClosed() {
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.CLOSED)));

            assertThrows(ClosedPautaException.class, () -> pautaService.vote(PAUTA_ID, VoterStub.voterDto()));

            verify(pautaRepository).findById(anyLong());
            verify(voterService, never()).create(any());
        }

        @Test
        @DisplayName("Should not vote pauta when IllegalArgumentException is thrown")
        public void voteFail() {
            when(pautaRepository.findById(anyLong())).thenThrow(new IllegalArgumentException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(IllegalArgumentException.class, () ->
                    pautaService.vote(PAUTA_ID, VoterStub.voterDto())).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(pautaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("CloseById")
    class CloseById {
        @Test
        @DisplayName("Should close by pauta id")
        public void closeSuccess() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.OPEN)));
            when(voterService.findAllByPautaId(anyLong())).thenReturn(List.of(VoterStub.voterDto()));
            when(pautaRepository.save(any())).thenReturn(PautaStub.pauta(PautaStatusEnum.CLOSED));

            PautaDto pautaDto = pautaService.closeById(PAUTA_ID);

            assertEquals(PautaStatusEnum.CLOSED, pautaDto.status());
            assertNotNull(pautaDto.totalVoteCount());
            assertNotNull(pautaDto.yesVoteCount());
            assertNotNull(pautaDto.noVoteCount());

            verify(pautaRepository).findById(anyLong());
            verify(voterService).findAllByPautaId(anyLong());
            verify(pautaRepository).save(any());
        }

        @Test
        @DisplayName("Should not close when pauta has not been opened")
        void closeWhenPautaIsCreated() {
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.CREATED)));

            assertThrows(CreatedPautaException.class, () -> pautaService.closeById(PAUTA_ID));

            verify(pautaRepository).findById(anyLong());
            verify(voterService, never()).create(any());
        }

        @Test
        @DisplayName("Should do nothing when pauta is already closed")
        void closeWhenPautaIsClosed() {
            when(pautaRepository.findById(anyLong()))
                    .thenReturn(Optional.of(PautaStub.pauta(PautaStatusEnum.CLOSED)));

            PautaDto pautaDto = pautaService.closeById(PAUTA_ID);
            assertEquals(PautaStatusEnum.CLOSED, pautaDto.status());
            assertNotNull(pautaDto.totalVoteCount());
            assertNotNull(pautaDto.yesVoteCount());
            assertNotNull(pautaDto.noVoteCount());

            verify(pautaRepository).findById(anyLong());
            verify(voterService, never()).create(any());
        }

        @Test
        @DisplayName("Should not close pauta when IllegalArgumentException is thrown")
        public void voteFail() {
            when(pautaRepository.findById(anyLong())).thenThrow(new IllegalArgumentException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(IllegalArgumentException.class, () ->
                    pautaService.closeById(PAUTA_ID)).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(pautaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find")
    class Find {
        @Test
        @DisplayName("Should find pauta by id")
        public void findSuccess() {
            when(pautaRepository.findById(anyLong())).thenReturn(Optional.of(PautaStub.pauta()));

            PautaDto pautaDto = pautaService.find(PAUTA_ID);

            assertEquals(PAUTA_ID, pautaDto.id());
            verify(pautaRepository).findById(anyLong());
        }

        @Test
        @DisplayName("Should not find pauta when EntityNotFoundException is thrown")
        public void findFail() {
            when(pautaRepository.findById(any())).thenThrow(new EntityNotFoundException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(EntityNotFoundException.class, () -> pautaService.find(PAUTA_ID)).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(pautaRepository).findById(any());
        }
    }

    @Nested
    @DisplayName("CheckOpenPautas")
    class CheckOpenPautas {
        @Test
        @DisplayName("Should check for open pautas")
        public void checkOpenPautasSuccess() {
            when(pautaRepository.findAllByTimeAndStatus(any(LocalDateTime.class), any(PautaStatusEnum.class)))
                    .thenReturn(List.of(PautaStub.pauta()));
            when(voterService.findAllByPautaId(anyLong())).thenReturn(List.of(VoterStub.voterDto()));
            when(pautaRepository.save(any())).thenReturn(PautaStub.pauta(PautaStatusEnum.CLOSED));

            pautaService.checkOpenPautas();

            verify(pautaRepository).findAllByTimeAndStatus(any(LocalDateTime.class), any(PautaStatusEnum.class));
            verify(voterService).findAllByPautaId(anyLong());
            verify(pautaRepository).save(any());
        }
    }
}
