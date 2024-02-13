package com.endava.petstore.integration.controller;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.repository.PetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.PetMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerIntegrationTest {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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
    void findAll_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_PETS, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(pets);
    }

    @Test
    void findById_validId_test() {
        ResponseEntity<Pet> response = testRestTemplate.getForEntity(API_PETS + "/" + VALID_ID, Pet.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(response.getBody()).isEqualTo(pet1);
    }

    @Test
    void findById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_PETS + "/" + INVALID_ID, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(PET_NOT_FOUND, INVALID_ID));
    }

    @Test
    void save_test() throws Exception {
        ResponseEntity<Pet> saveResponse = testRestTemplate.postForEntity(API_PETS, pet4, Pet.class);
        then(saveResponse).isNotNull();
        then(saveResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(saveResponse.getBody()).isEqualTo(pet4);
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_PETS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Pet> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(pet1, pet2, pet3, pet4));
    }

    @Test
    void update_test() {
        Pet updatedPet = pet1;
        updatedPet.setName("Updated Pet");
        ResponseEntity<Pet> updateResponse = testRestTemplate.exchange(API_PETS, HttpMethod.PUT, new HttpEntity<>(pet1), Pet.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(updatedPet);
        ResponseEntity<Pet> getResponse = testRestTemplate.getForEntity(API_PETS + "/" + VALID_ID, Pet.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(getResponse.getBody()).isEqualTo(updatedPet);
    }

    @Test
    void deleteById_validId_test() throws Exception {
        ResponseEntity<Pet> deleteResponse = testRestTemplate.exchange(API_PETS + "/" + VALID_ID, HttpMethod.DELETE, null, Pet.class);
        then(deleteResponse).isNotNull();
        then(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        ResponseEntity<String> getResponse = testRestTemplate.getForEntity(API_PETS + "/" + VALID_ID, String.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(getResponse.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(PET_NOT_FOUND, VALID_ID));
        ResponseEntity<String> findAllResponse = testRestTemplate.getForEntity(API_PETS, String.class);
        then(findAllResponse).isNotNull();
        then(findAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Pet> result = objectMapper.readValue(findAllResponse.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(pet2, pet3));
    }

    @Test
    void deleteById_invalidId_test() {
        ResponseEntity<String> response = testRestTemplate.exchange(API_PETS + "/" + INVALID_ID, HttpMethod.DELETE, null, String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + String.format(PET_NOT_FOUND, INVALID_ID));
    }

    @Test
    void findByStatuses_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_PETS + "/findByStatus?status=AVAILABLE&status=PENDING", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_test() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_PETS + "/findByTags?tags=Tag1&tags=Tag2&tags=Tag3", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Pet> result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        then(result).isEqualTo(List.of(pet1, pet2));
    }

    @Test
    void findByTags_emptyList_test() {
        ResponseEntity<String> response = testRestTemplate.getForEntity(API_PETS + "/findByTags?tags=", String.class);
        then(response).isNotNull();
        then(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        then(response.getBody()).isEqualTo(RESOURCE_NOT_FOUND + TAGS_NOT_FOUND);
    }

    @Test
    void updateWithFormData_test() {
        String name = "Test pet";
        String status = "AVAILABLE";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>(); // build request body as multipart/form-data
        body.add("name", name);
        body.add("status", status);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MULTIPART_FORM_DATA);
        ResponseEntity<HttpResponse> updateResponse = testRestTemplate.postForEntity(API_PETS + "/" + VALID_ID, new HttpEntity<>(body, httpHeaders), HttpResponse.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, VALID_ID)));
        ResponseEntity<Pet> getResponse = testRestTemplate.getForEntity(API_PETS + "/" + VALID_ID, Pet.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Pet updatedPet = pet1;
        updatedPet.setName(name);
        updatedPet.setStatus(PetStatus.valueOf(status));
        then(getResponse.getBody()).isEqualTo(updatedPet);
    }

    @Test
    void uploadImage_test() {
        String additionalMetadata = "Test image";
        MultipartFile file = new MockMultipartFile("file", "test_image.png", IMAGE_JPEG_VALUE, "content".getBytes());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>(); // build request body as multipart/form-data
        body.add("additionalMetadata", additionalMetadata);
        body.add("file", file.getResource());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MULTIPART_FORM_DATA);
        ResponseEntity<HttpResponse> updateResponse = testRestTemplate.postForEntity(API_PETS + "/" + VALID_ID + "/uploadImage", new HttpEntity<>(body, httpHeaders), HttpResponse.class);
        then(updateResponse).isNotNull();
        then(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(updateResponse.getBody()).isEqualTo(new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPLOADED_IMAGE, additionalMetadata, file.getOriginalFilename(), file.getSize())));
        ResponseEntity<Pet> getResponse = testRestTemplate.getForEntity(API_PETS + "/" + VALID_ID, Pet.class);
        then(getResponse).isNotNull();
        then(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Pet updatedPet = pet1;
        List<String> photoUrls = new ArrayList<>(updatedPet.getPhotoUrls());
        photoUrls.add(String.format("https://www.petstore.com/%s", file.getOriginalFilename()));
        updatedPet.setPhotoUrls(photoUrls);
        then(getResponse.getBody()).isEqualTo(updatedPet);
    }
}
