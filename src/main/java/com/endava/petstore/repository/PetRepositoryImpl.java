package com.endava.petstore.repository;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.Category;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.Tag;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.endava.petstore.constants.Constants.PET_NOT_FOUND;

@Repository
public class PetRepositoryImpl implements PetRepository {
    private final Map<Long, Pet> pets = new HashMap<>();

    @PostConstruct
    private void initializePets() {
        Pet pet1 = Pet.builder()
              .id(1L)
              .name("Pet1")
              .category(Category.builder().id(1L).name("Category1").build())
              .photoUrls(List.of("https://www.petstore.com/image1.png", "https://www.petstore.com/image2.png"))
              .tags(List.of(
                    Tag.builder().id(1L).name("Tag1").build(),
                    Tag.builder().id(2L).name("Tag2").build()))
              .status(PetStatus.AVAILABLE)
              .build();
        pets.put(pet1.getId(), pet1);
        Pet pet2 = Pet.builder()
              .id(2L)
              .name("Pet2")
              .category(Category.builder().id(2L).name("Category2").build())
              .photoUrls(List.of("https://www.petstore.com/image3.png", "https://www.petstore.com/image4.png"))
              .tags(List.of(
                    Tag.builder().id(3L).name("Tag3").build(),
                    Tag.builder().id(4L).name("Tag4").build()))
              .status(PetStatus.PENDING)
              .build();
        pets.put(pet2.getId(), pet2);
        Pet pet3 = Pet.builder()
              .id(3L)
              .name("Pet3")
              .category(Category.builder().id(3L).name("Category3").build())
              .photoUrls(List.of("https://www.petstore.com/image5.png", "https://www.petstore.com/image6.png"))
              .tags(List.of(
                    Tag.builder().id(5L).name("Tag5").build(),
                    Tag.builder().id(6L).name("Tag6").build()))
              .status(PetStatus.SOLD)
              .build();
        pets.put(pet3.getId(), pet3);
    }

    @Override
    public List<Pet> findAll() {
        return new ArrayList<>(pets.values());
    }

    @Override
    public Pet findById(Long id) {
        return pets.values().stream().filter(pet -> pet.getId().equals(id)).findFirst().orElseThrow(() -> new ResourceNotFoundException(String.format(PET_NOT_FOUND, id)));
    }

    @Override
    public Pet save(Pet pet) {
        return pets.compute(pet.getId(), (key, value) -> pet);
    }

    @Override
    public Pet update(Pet pet) {
        Pet petToUpdate = findById(pet.getId());
        petToUpdate.setName(pet.getName());
        petToUpdate.setCategory(pet.getCategory());
        petToUpdate.setPhotoUrls(pet.getPhotoUrls());
        petToUpdate.setTags(pet.getTags());
        petToUpdate.setStatus(pet.getStatus());
        return petToUpdate;
    }

    @Override
    public void deleteById(Long id) {
        Pet petToDelete = findById(id);
        pets.remove(petToDelete.getId());
    }

    @Override
    public List<Pet> findByStatuses(PetStatus[] statuses) {
        List<String> statusList = Arrays.stream(statuses).map(Enum::name).toList();
        return findAll().stream().filter(pet -> statusList.contains(pet.getStatus().name())).toList();
    }

    @Override
    public List<Pet> findByTags(List<String> tagNames) {
        return findAll().stream().filter(pet -> pet.getTags().stream().map(Tag::getName).anyMatch(tagNames::contains)).toList();
    }
}
