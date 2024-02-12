package com.endava.petstore.repository;

import com.endava.petstore.model.Pet;

import java.util.List;

public interface PetRepository {

    List<Pet> findAll();

    Pet findById(Long id);

    Pet save(Pet pet);

    Pet update(Pet pet);

    void deleteById(Long id);
}
