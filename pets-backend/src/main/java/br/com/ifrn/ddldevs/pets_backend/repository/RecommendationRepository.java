package br.com.ifrn.ddldevs.pets_backend.repository;

import br.com.ifrn.ddldevs.pets_backend.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findAllByPetId(Long petId);

    List<Recommendation> findAllByUserId(Long id);
}