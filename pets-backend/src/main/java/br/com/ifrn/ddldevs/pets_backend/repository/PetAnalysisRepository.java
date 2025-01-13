package br.com.ifrn.ddldevs.pets_backend.repository;

import br.com.ifrn.ddldevs.pets_backend.domain.PetAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetAnalysisRepository extends JpaRepository<PetAnalysis, Long> {
    List<PetAnalysis> findAllByPetId(Long petId);
}
