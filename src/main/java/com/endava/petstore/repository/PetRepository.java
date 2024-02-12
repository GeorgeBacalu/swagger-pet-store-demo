package com.endava.petstore.repository;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.Pet;

import java.util.List;

public interface PetRepository {

    List<Pet> findAll();

    Pet findById(Long id);

    Pet save(Pet pet);

    Pet update(Pet pet);

    void deleteById(Long id);

    List<Pet> findByStatuses(PetStatus[] statuses);

    List<Pet> findByTags(List<String> tagNames);
}
