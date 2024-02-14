package com.endava.petstore.integration.repository;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.PetRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.PetMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@SpringBootTest
class PetRepositoryIntegrationTest {

    @Autowired
    private PetRepositoryImpl petRepository;

    private Pet pet1;
    private Pet pet2;
    private Pet pet3;
    private Pet pet4;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pet1 = getMockedPet1();
        pet2 = getMockedPet2();
        pet3 = getMockedPet3();
        pet4 = getMockedPet4();
        pets = getMockedPets();
        petRepository.deleteAll();
        pets.forEach(pet -> petRepository.save(pet));
    }

    @Test
    void findAll_test() {
        then(petRepository.findAll()).isEqualTo(pets);
    }

    @Test
    void findById_validId_test() {
        then(petRepository.findById(VALID_ID)).isEqualTo(pet1);
    }

    @Test
    void findById_invalidId_test() {
        thenThrownBy(() -> petRepository.findById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, INVALID_ID));
    }

    @Test
    void save_test() {
        then(petRepository.save(pet4)).isEqualTo(pet4);
        then(petRepository.findAll()).isEqualTo(List.of(pet1, pet2, pet3, pet4));
    }

    @Test
    void update_validPet_test() {
        then(petRepository.update(pet1)).isEqualTo(pet1);
    }

    @Test
    void update_invalidPet_test() {
        thenThrownBy(() -> petRepository.update(pet4))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, pet4.getId()));
    }

    @Test
    void deleteById_validId_test() {
        petRepository.deleteById(VALID_ID);
        then(petRepository.findAll()).isEqualTo(List.of(pet2, pet3));
    }

    @Test
    void deleteById_invalidId_test() {
        thenThrownBy(() -> petRepository.deleteById(INVALID_ID))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(String.format(PET_NOT_FOUND, INVALID_ID));
    }

    @Test
    void findByStatuses_shouldReturnPetsWithGivenStatuses() {
        PetStatus[] statuses = {PetStatus.AVAILABLE, PetStatus.PENDING};
        then(petRepository.findByStatuses(statuses)).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_shouldReturnPetsWithGivenTagNames() {
        List<String> tagNames = List.of("Tag1", "Tag2", "Tag3");
        then(petRepository.findByTags(tagNames)).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void updateWithFormData_shouldModifyPetUsingFormData() {
        String name = "Test pet";
        String status = "AVAILABLE";
        then(petRepository.updateWithFormData(VALID_ID, name, status)).isEqualTo(new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, VALID_ID)));
    }

    @Test
    void uploadImage_shouldAppendToPetPhotoUrls() {
        String additionalMetadata = "Test image";
        MultipartFile file = new MockMultipartFile("file", "test_image.png", IMAGE_JPEG_VALUE, "content".getBytes());
        then(petRepository.uploadImage(VALID_ID, additionalMetadata, file)).isEqualTo(new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPLOADED_IMAGE, additionalMetadata, file.getOriginalFilename(), file.getSize())));
    }
}
