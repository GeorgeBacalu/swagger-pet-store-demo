package com.endava.petstore.service;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetService {

    List<Pet> findAll();

    Pet findById(Long id);

    Pet save(Pet pet);

    Pet update(Pet pet);

    void deleteById(Long id);

    List<Pet> findByStatuses(PetStatus[] statuses);

    List<Pet> findByTags(List<String> tagNames);

    HttpResponse updateWithFormData(Long id, String name, String status);

    HttpResponse uploadImage(Long id, String additionalMetadata, MultipartFile file);
}
