package com.voting.voter.service;

import com.voting.client.service.service.DocumentValidationClientService;
import com.voting.client.stub.DocumentValidationStub;
import com.voting.infra.exception.MemberUnableToVoteException;
import com.voting.voter.dto.VoterDto;
import com.voting.voter.enums.VoterStatusEnum;
import com.voting.voter.repository.VoterRepository;
import com.voting.voter.service.impl.VoterServiceImpl;
import com.voting.voter.stub.VoterStub;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Voter Service Test")
@ExtendWith(MockitoExtension.class)
class VoterServiceTest {
    public static final String DEFAULT_ERROR_MESSAGE = "something went wrong";
    private static final Long PAUTA_ID = 1L;

    @InjectMocks
    private VoterServiceImpl voterService;

    @Mock
    private DocumentValidationClientService documentValidationService;

    @Mock
    private VoterRepository voterRepository;

    @Nested
    @DisplayName("Create")
    class Create {
        @Test
        @DisplayName("Should create voter")
        public void createSuccess() {
            when(voterRepository.existsByCpfAndPautaId(anyString(), anyLong())).thenReturn(false);
            when(documentValidationService.validateDocumentId(anyString()))
                    .thenReturn(DocumentValidationStub.documentValidationResponse(VoterStatusEnum.ABLE_TO_VOTE));
            when(voterRepository.save(any())).thenReturn(VoterStub.voter());

            voterService.create(VoterStub.voterDto());

            verify(voterRepository).existsByCpfAndPautaId(anyString(), anyLong());
            verify(documentValidationService).validateDocumentId(anyString());
            verify(voterRepository).save(any());
        }

        @Test
        @DisplayName("Should not be able to create voter because the member already vote")
        public void createAlreadyVoted() {
            when(voterRepository.existsByCpfAndPautaId(anyString(), anyLong())).thenReturn(true);

            assertThrows(MemberUnableToVoteException.class, () -> voterService.create(VoterStub.voterDto()));

            verify(voterRepository).existsByCpfAndPautaId(anyString(), anyLong());
            verify(documentValidationService, never()).validateDocumentId(anyString());
            verify(voterRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should not be able to create voter because the member is unable to vote")
        public void createUnableToVote() {
            when(voterRepository.existsByCpfAndPautaId(anyString(), anyLong())).thenReturn(false);
            when(documentValidationService.validateDocumentId(anyString()))
                    .thenReturn(DocumentValidationStub.documentValidationResponse(VoterStatusEnum.UNABLE_TO_VOTE));

            assertThrows(MemberUnableToVoteException.class, () -> voterService.create(VoterStub.voterDto()));

            verify(voterRepository).existsByCpfAndPautaId(anyString(), anyLong());
            verify(documentValidationService).validateDocumentId(anyString());
            verify(voterRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("FindAllByPautaId")
    class FindAllByPautaId {
        @Test
        @DisplayName("Should find all voters by pautaId")
        public void findAllByPautaIdSuccess() {
            when(voterRepository.findAllByPautaId(anyLong())).thenReturn(List.of(VoterStub.voter()));

            List<VoterDto> voters = voterService.findAllByPautaId(PAUTA_ID);

            assertNotNull(voters);
            verify(voterRepository).findAllByPautaId(anyLong());
        }

        @Test
        @DisplayName("Should not find pauta when EntityNotFoundException is thrown")
        public void findAllByPautaIdFail() {
            when(voterRepository.findAllByPautaId(any())).thenThrow(new EntityNotFoundException(DEFAULT_ERROR_MESSAGE));

            String message = assertThrows(EntityNotFoundException.class, () -> voterService.findAllByPautaId(PAUTA_ID)).getMessage();

            assertEquals(DEFAULT_ERROR_MESSAGE, message);
            verify(voterRepository).findAllByPautaId(any());
        }
    }
}
