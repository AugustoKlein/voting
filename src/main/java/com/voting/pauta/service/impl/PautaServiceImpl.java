package com.voting.pauta.service.impl;

import com.voting.infra.exception.ClosedPautaException;
import com.voting.infra.exception.CreatedPautaException;
import com.voting.infra.exception.OpenedPautaException;
import com.voting.pauta.dto.PautaDto;
import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.mapper.PautaMapper;
import com.voting.pauta.repository.PautaRepository;
import com.voting.pauta.repository.entity.Pauta;
import com.voting.pauta.service.PautaService;
import com.voting.voter.dto.VoterDto;
import com.voting.voter.service.VoterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PautaServiceImpl implements PautaService {

    private VoterService voterService;
    private PautaRepository pautaRepository;

    @Override
    public Long create(PautaDto pautaDto) {
        log.info("Creating pauta: name - {}", pautaDto.name());
        Pauta pauta = pautaRepository.save(PautaMapper.toEntity(pautaDto));
        return pauta.getId();
    }

    @Override
    public void openById(Long id, LocalDateTime endsAt) {
        log.info("Opening pauta: id - {}", id);
        Pauta pauta = pautaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (pauta.getStatus().equals(PautaStatusEnum.OPEN)) {
            log.error("Pauta voting already opened");
            throw new OpenedPautaException();
        }

        if (pauta.getStatus().equals(PautaStatusEnum.CLOSED)) {
            log.error("Pauta voting already closed");
            throw new ClosedPautaException();
        }

        pauta.setStatus(PautaStatusEnum.OPEN);
        pauta.setEndsAt(endsAt == null ? LocalDateTime.now().plusMinutes(1L) : endsAt);
        pautaRepository.save(pauta);
    }

    @Override
    public void vote(Long id, VoterDto voterDto) {
        log.info("Voting pauta: pautaId - {}", id);
        PautaDto pauta = find(id);

        if (pauta.status().equals(PautaStatusEnum.CREATED)) {
            log.error("Unable to vote because pauta voting is not open yet");
            throw new CreatedPautaException();
        }

        if (pauta.status().equals(PautaStatusEnum.CLOSED)) {
            log.error("Unable to vote because pauta voting has been closed");
            throw new ClosedPautaException();
        }

        voterService.create(voterDto);
    }

    @Override
    public PautaDto closeById(Long id) {
        Pauta pauta = pautaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        if (pauta.getStatus().equals(PautaStatusEnum.CREATED)) {
            log.error("Unable to close because pauta voting is not open yet");
            throw new CreatedPautaException();
        }

        if (pauta.getStatus().equals(PautaStatusEnum.CLOSED)) {
            log.debug("Pauta is already closed");
            return PautaMapper.toDto(pauta);
        }

        return close(pauta);
    }

    @Override
    public PautaDto find(Long id) {
        log.info("Find pauta: id - {}", id);
        Pauta pauta = pautaRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return PautaMapper.toDto(pauta);
    }

    @Override
    public void checkOpenPautas() {
        log.info("Checking for open pautas");
        List<Pauta> pautas = pautaRepository.findAllByTimeAndStatus(LocalDateTime.now(), PautaStatusEnum.OPEN);

        for (Pauta pauta: pautas) {
            close(pauta);
        }
    }

    private PautaDto close(Pauta pauta) {
        log.info("Closing pauta: id - {}", pauta.getId());
        List<VoterDto> voters = voterService.findAllByPautaId(pauta.getId());

        long yesVoteCount = voters.stream().filter(VoterDto::votedYes).count();

        long noVoteCount = voters.size() - yesVoteCount;

        pauta.setTotalVoteCount((long) voters.size());
        pauta.setYesVoteCount(yesVoteCount);
        pauta.setNoVoteCount(noVoteCount);
        pauta.setStatus(PautaStatusEnum.CLOSED);

        Pauta updatedPauta = pautaRepository.save(pauta);
        log.info("Pauta id: {} has been closed", pauta.getId());

        return PautaMapper.toDto(updatedPauta);
    }

}
