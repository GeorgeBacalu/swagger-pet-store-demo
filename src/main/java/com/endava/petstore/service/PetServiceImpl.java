package com.endava.petstore.service;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    @Override
    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    @Override
    public Pet findById(Long id) {
        return petRepository.findById(id);
    }

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet update(Pet pet) {
        return petRepository.update(pet);
    }

    @Override
    public void deleteById(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public List<Pet> findByStatuses(PetStatus[] statuses) {
        return petRepository.findByStatuses(statuses);
    }

    @Override
    public List<Pet> findByTags(List<String> tagNames) {
        if (tagNames.isEmpty()) {
            throw new ResourceNotFoundException("No tags were provided");
        }
        return petRepository.findByTags(tagNames);
    }

    @Override
    public HttpResponse updateWithFormData(Long id, String name, String status) {
        return petRepository.updateWithFormData(id, name, status);
    }
}
