package com.voting.voter.repository;

import com.voting.voter.repository.entity.Voter;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoterRepository extends CrudRepository<Voter, Long> {

    boolean existsByCpfAndPautaId(String cpf, Long pautaId);

    List<Voter> findAllByPautaId(Long pautaId);
}
