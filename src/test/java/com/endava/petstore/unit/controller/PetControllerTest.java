package com.endava.petstore.unit.controller;

import com.endava.petstore.controller.PetController;
import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.PetUpdateFormDataRequest;
import com.endava.petstore.model.PetUploadImageRequest;
import com.endava.petstore.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.PetMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @InjectMocks
    private PetController petController;

    @Mock
    private PetService petService;

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
        given(petService.findAll()).willReturn(pets);
        ResponseEntity<List<Pet>> response = petController.findAll();
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(pets);
    }

    @Test
    void findById_test() {
        given(petService.findById(VALID_ID)).willReturn(pet1);
        ResponseEntity<Pet> response = petController.findById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void save_test() {
        given(petService.save(any(Pet.class))).willReturn(pet1);
        ResponseEntity<Pet> response = petController.save(pet1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void update_test() {
        given(petService.update(any(Pet.class))).willReturn(pet2);
        ResponseEntity<Pet> response = petController.update(pet1);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(pet2);
    }

    @Test
    void deleteById_test() {
        ResponseEntity<Void> response = petController.deleteById(VALID_ID);
        verify(petService).deleteById(VALID_ID);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void findByStatuses_test() {
        PetStatus[] statuses = {PetStatus.AVAILABLE, PetStatus.PENDING};
        given(petService.findByStatuses(statuses)).willReturn(List.of(pet1, pet2));
        ResponseEntity<List<Pet>> response = petController.findByStatuses(statuses);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_test() {
        List<String> tagNames = List.of("Tag1", "Tag2", "Tag3");
        given(petService.findByTags(tagNames)).willReturn(List.of(pet1, pet2));
        ResponseEntity<List<Pet>> response = petController.findByTags(tagNames);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void updateWithFormData_test() {
        String name = "Test pet";
        String status = "AVAILABLE";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, VALID_ID));
        given(petService.updateWithFormData(VALID_ID, name, status)).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = petController.updateWithFormData(VALID_ID, new PetUpdateFormDataRequest(name, status));
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(httpResponse);
    }

    @Test
    void uploadImage_test() {
        String additionalMetadata = "Test image";
        MultipartFile file = new MockMultipartFile("file", "test_image.png", IMAGE_JPEG_VALUE, "content".getBytes());
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPLOADED_IMAGE, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petService.uploadImage(VALID_ID, additionalMetadata, file)).willReturn(httpResponse);
        ResponseEntity<HttpResponse> response = petController.uploadImage(VALID_ID, new PetUploadImageRequest(additionalMetadata), file);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(httpResponse);
    }
}
