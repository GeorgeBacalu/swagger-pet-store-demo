package com.endava.petstore.unit.service;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.PetRepository;
import com.endava.petstore.service.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.PetMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @InjectMocks
    private PetServiceImpl petService;

    @Mock
    private PetRepository petRepository;

    @Captor
    private ArgumentCaptor<Pet> petCaptor;

    private Pet pet1;
    private Pet pet2;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        pet1 = getMockedPet1();
        pet2 = getMockedPet2();
        pets = getMockedPets();
    }

    @Test
    void findAll_test() {
        given(petRepository.findAll()).willReturn(pets);
        List<Pet> result = petService.findAll();
        then(result).isEqualTo(pets);
    }

    @Test
    void findById_test() {
        given(petRepository.findById(VALID_ID)).willReturn(pet1);
        Pet result = petService.findById(VALID_ID);
        then(result).isEqualTo(pet1);
    }

    @Test
    void save_test() {
        given(petRepository.save(any(Pet.class))).willReturn(pet1);
        Pet result = petService.save(pet1);
        verify(petRepository).save(petCaptor.capture());
        then(result).isEqualTo(petCaptor.getValue());
    }

    @Test
    void update_test() {
        given(petRepository.update(any(Pet.class))).willReturn(pet2);
        Pet result = petService.update(pet1);
        then(result).isEqualTo(pet2);
    }

    @Test
    void deleteById_test() {
        petService.deleteById(VALID_ID);
        verify(petRepository).deleteById(VALID_ID);
    }

    @Test
    void findByStatuses_test() {
        PetStatus[] statuses = {PetStatus.AVAILABLE, PetStatus.PENDING};
        given(petRepository.findByStatuses(statuses)).willReturn(List.of(pet1, pet2));
        List<Pet> result = petService.findByStatuses(statuses);
        then(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_test() {
        List<String> tagNames = List.of("Tag1", "Tag2", "Tag3");
        given(petRepository.findByTags(tagNames)).willReturn(List.of(pet1, pet2));
        List<Pet> result = petService.findByTags(tagNames);
        then(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_emptyList_test() {
        List<String> tagNames = Collections.emptyList();
        thenThrownBy(() -> petService.findByTags(tagNames))
              .isInstanceOf(ResourceNotFoundException.class)
              .hasMessage(TAGS_NOT_FOUND);
    }

    @Test
    void updateWithFormData_test() {
        String name = "Test pet";
        String status = "AVAILABLE";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, VALID_ID));
        given(petRepository.updateWithFormData(VALID_ID, name, status)).willReturn(httpResponse);
        HttpResponse result = petService.updateWithFormData(VALID_ID, name, status);
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void uploadImage_test() {
        String additionalMetadata = "Test image";
        MultipartFile file = new MockMultipartFile("file", "test_image.png", IMAGE_JPEG_VALUE, "content".getBytes());
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPLOADED_IMAGE, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petRepository.uploadImage(VALID_ID, additionalMetadata, file)).willReturn(httpResponse);
        HttpResponse result = petService.uploadImage(VALID_ID, additionalMetadata, file);
        then(result).isEqualTo(httpResponse);
    }
}
