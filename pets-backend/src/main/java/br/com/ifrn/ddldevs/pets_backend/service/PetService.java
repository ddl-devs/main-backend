package br.com.ifrn.ddldevs.pets_backend.service;

import br.com.ifrn.ddldevs.pets_backend.domain.Pet;
import br.com.ifrn.ddldevs.pets_backend.domain.User;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetResponseDTO;
import br.com.ifrn.ddldevs.pets_backend.dto.Pet.PetUpdateRequestDTO;
import br.com.ifrn.ddldevs.pets_backend.exception.ResourceNotFoundException;
import br.com.ifrn.ddldevs.pets_backend.mapper.PetMapper;
import br.com.ifrn.ddldevs.pets_backend.repository.PetRepository;
import br.com.ifrn.ddldevs.pets_backend.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetMapper petMapper;

    @Transactional
    public PetResponseDTO createPet(PetRequestDTO petRequestDTO) {
        Pet pet = petMapper.toEntity(petRequestDTO);
        User user = userRepository.findById(petRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não existe"));

        pet.setUser(user);
        user.getPets().add(pet);

        petRepository.save(pet);
        userRepository.save(user);

        return petMapper.toPetResponseDTO(pet);
    }

    public List<PetResponseDTO> listPets(){
        List<Pet> pets = petRepository.findAll();
        return petMapper.toDTOList(pets);
    }

    public PetResponseDTO updatePet(Long id, PetUpdateRequestDTO petRequestDTO) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado"));

        petMapper.updateEntityFromDTO(petRequestDTO, pet);
        Pet petUpdated = petRepository.save(pet);

        return petMapper.toPetResponseDTO(petUpdated);
    }

    public PetResponseDTO getPet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pet não encontrado"));

        return petMapper.toPetResponseDTO(pet);
    }

    public void deletePet(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        if (id < 0) {
            throw new IllegalArgumentException("ID não pode ser negativo");
        }
        if(!petRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pet não encontrado");
        }

        petRepository.deleteById(id);
    }


}
