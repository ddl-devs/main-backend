package br.com.ifrn.ddldevs.pets_backend.mapper;

import br.com.ifrn.ddldevs.pets_backend.domain.Recommendation;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Recomendation.RecommendationResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper()
public interface RecommendationMapper {

    RecommendationResponseDTO toRecommendationResponseDTO(Recommendation recommendation);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "id", ignore = true)
    Recommendation toEntity(RecommendationRequestDTO recommendationRequestDTO);

    List<RecommendationResponseDTO> toDTOList(List<Recommendation> recommendations);
}