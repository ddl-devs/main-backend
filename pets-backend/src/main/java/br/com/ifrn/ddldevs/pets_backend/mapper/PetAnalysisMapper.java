package br.com.ifrn.ddldevs.pets_backend.mapper;

import br.com.ifrn.ddldevs.pets_backend.domain.PetAnalysis;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.PetAnalysis.PetAnalysisResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper()
public interface PetAnalysisMapper {

    PetAnalysisResponseDTO toResponse(PetAnalysis petAnalysis);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "id", ignore = true)
    PetAnalysis toEntity(PetAnalysisRequestDTO petAnalysisRequestDTO);

    List<PetAnalysisResponseDTO> toResponseList(List<PetAnalysis> petAnalyses);

}