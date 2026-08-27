package com.voting.pauta.repository;

import com.voting.pauta.enums.PautaStatusEnum;
import com.voting.pauta.repository.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

    @Query("""
                SELECT p FROM Pauta p
                WHERE p.endsAt <= :time
                AND p.status = :status
            """)
    List<Pauta> findAllByTimeAndStatus(
            @Param("time") LocalDateTime time,
            @Param("status") PautaStatusEnum status
    );
}
