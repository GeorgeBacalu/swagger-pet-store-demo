package com.endava.petstore.integration.controller;

import com.endava.petstore.controller.PetController;
import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.exception.ResourceNotFoundException;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.service.PetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.endava.petstore.constants.Constants.*;
import static com.endava.petstore.mock.PetMock.*;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@ExtendWith(MockitoExtension.class)
class PetControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    void findAll_test() throws Exception {
        given(petService.findAll()).willReturn(pets);
        ResultActions actions = mockMvc.perform(get(API_PETS)).andExpect(status().isOk());
        for (int i = 0; i < pets.size(); ++i) {
            assertPet(actions, "$[" + i + "]", pets.get(i));
        }
        List<Pet> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});
        then(result).isEqualTo(pets);
    }

    @Test
    void findById_validId_test() throws Exception {
        given(petService.findById(VALID_ID)).willReturn(pet1);
        ResultActions actions = mockMvc.perform(get(API_PETS + "/{id}", VALID_ID)).andExpect(status().isOk());
        assertPetJson(actions, pet1);
        Pet result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Pet.class);
        then(result).isEqualTo(pet1);
    }

    @Test
    void findById_invalidId_test() throws Exception {
        String message = String.format(PET_NOT_FOUND, INVALID_ID);
        given(petService.findById(INVALID_ID)).willThrow(new ResourceNotFoundException(message));
        mockMvc.perform(get(API_PETS + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void save_test() throws Exception {
        given(petService.save(any(Pet.class))).willReturn(pet1);
        ResultActions actions = mockMvc.perform(post(API_PETS)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(pet1)))
              .andExpect(status().isCreated());
        assertPetJson(actions, pet1);
        Pet result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Pet.class);
        then(result).isEqualTo(pet1);
    }

    @Test
    void update_test() throws Exception {
        given(petService.update(any(Pet.class))).willReturn(pet2);
        ResultActions actions = mockMvc.perform(put(API_PETS)
                    .contentType(APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(pet2)))
              .andExpect(status().isOk());
        assertPetJson(actions, pet2);
        Pet result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), Pet.class);
        then(result).isEqualTo(pet2);
    }

    @Test
    void deleteById_validId_test() throws Exception {
        mockMvc.perform(delete(API_PETS + "/{id}", VALID_ID)).andExpect(status().isNoContent()).andReturn();
        verify(petService).deleteById(VALID_ID);
    }

    @Test
    void deleteById_invalidId_test() throws Exception {
        String message = String.format(PET_NOT_FOUND, INVALID_ID);
        doThrow(new ResourceNotFoundException(message)).when(petService).deleteById(INVALID_ID);
        mockMvc.perform(delete(API_PETS + "/{id}", INVALID_ID))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(message));
    }

    @Test
    void findByStatuses_test() throws Exception {
        PetStatus[] statuses = {PetStatus.AVAILABLE, PetStatus.PENDING};
        List<Pet> resultPets = List.of(pet1, pet2);
        given(petService.findByStatuses(statuses)).willReturn(resultPets);
        ResultActions actions = mockMvc.perform(get(API_PETS + "/findByStatus")
                    .param("status", PetStatus.AVAILABLE.name())
                    .param("status", PetStatus.PENDING.name()))
              .andExpect(status().isOk());
        for (int i = 0; i < resultPets.size(); ++i) {
            assertPet(actions, "$[" + i + "]", resultPets.get(i));
        }
        List<Pet> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});
        then(result).isEqualTo(resultPets);
    }

    @Test
    void findByTags_test() throws Exception {
        List<String> tagNames = List.of("Tag1", "Tag2", "Tag3");
        List<Pet> resultPets = List.of(pet1, pet2);
        given(petService.findByTags(tagNames)).willReturn(resultPets);
        ResultActions actions = mockMvc.perform(get(API_PETS + "/findByTags")
                    .param("tags", "Tag1")
                    .param("tags", "Tag2")
                    .param("tags", "Tag3"))
              .andExpect(status().isOk());
        for (int i = 0; i < resultPets.size(); ++i) {
            assertPet(actions, "$[" + i + "]", resultPets.get(i));
        }
        List<Pet> result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});
        then(result).isEqualTo(resultPets);
    }

    @Test
    void findByTags_emptyList_test() throws Exception {
        List<String> tagNames = Collections.emptyList();
        given(petService.findByTags(tagNames)).willThrow(new ResourceNotFoundException(TAGS_NOT_FOUND));
        mockMvc.perform(get(API_PETS + "/findByTags").param("tags", ""))
              .andExpect(status().isNotFound())
              .andExpect(result -> then(result.getResolvedException() instanceof ResourceNotFoundException).isTrue())
              .andExpect(result -> then(Objects.requireNonNull(result.getResolvedException()).getMessage()).isEqualTo(TAGS_NOT_FOUND));
    }

    @Test
    void updateWithFormData_test() throws Exception {
        String name = "Test pet";
        String status = "AVAILABLE";
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPDATED, VALID_ID));
        given(petService.updateWithFormData(VALID_ID, name, status)).willReturn(httpResponse);
        ResultActions actions = mockMvc.perform(multipart(API_PETS + "/{id}", VALID_ID)
                    .param("name", name)
                    .param("status", status))
              .andExpect(status().isOk());
        HttpResponse result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), HttpResponse.class);
        then(result).isEqualTo(httpResponse);
    }

    @Test
    void uploadImage_test() throws Exception {
        String additionalMetadata = "Test image";
        MockMultipartFile file = new MockMultipartFile("file", "test_image.png", IMAGE_JPEG_VALUE, "content".getBytes());
        HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "unknown", String.format(PET_UPLOADED_IMAGE, additionalMetadata, file.getOriginalFilename(), file.getSize()));
        given(petService.uploadImage(VALID_ID, additionalMetadata, file)).willReturn(httpResponse);
        ResultActions actions = mockMvc.perform(multipart(API_PETS + "/{id}/uploadImage", VALID_ID)
                    .file(file)
                    .param("additionalMetadata", additionalMetadata))
              .andExpect(status().isOk());
        HttpResponse result = objectMapper.readValue(actions.andReturn().getResponse().getContentAsString(), HttpResponse.class);
        then(result).isEqualTo(httpResponse);
    }

    private void assertPet(ResultActions actions, String prefix, Pet pet) throws Exception {
        actions.andExpect(jsonPath(prefix + ".id").value(pet.getId()))
              .andExpect(jsonPath(prefix + ".name").value(pet.getName()))
              .andExpect(jsonPath(prefix + ".category.id").value(pet.getCategory().getId()))
              .andExpect(jsonPath(prefix + ".category.name").value(pet.getCategory().getName()))
              .andExpect(jsonPath(prefix + ".status").value(pet.getStatus().name()));
        for (int j = 0; j < pet.getPhotoUrls().size(); ++j) {
            actions.andExpect(jsonPath(prefix + ".photoUrls[" + j + "]").value(pet.getPhotoUrls().get(j)));
        }
        for (int j = 0; j < pet.getTags().size(); ++j) {
            actions.andExpect(jsonPath(prefix + ".tags[" + j + "].id").value(pet.getTags().get(j).getId()))
                  .andExpect(jsonPath(prefix + ".tags[" + j + "].name").value(pet.getTags().get(j).getName()));
        }
    }

    private void assertPetJson(ResultActions actions, Pet pet) throws Exception {
        actions.andExpect(jsonPath("$.id").value(pet.getId()))
              .andExpect(jsonPath("$.name").value(pet.getName()))
              .andExpect(jsonPath("$.category.id").value(pet.getCategory().getId()))
              .andExpect(jsonPath("$.category.name").value(pet.getCategory().getName()))
              .andExpect(jsonPath("$.status").value(pet.getStatus().name()));
        for (int j = 0; j < pet.getPhotoUrls().size(); ++j) {
            actions.andExpect(jsonPath("$.photoUrls[" + j + "]").value(pet.getPhotoUrls().get(j)));
        }
        for (int j = 0; j < pet.getTags().size(); ++j) {
            actions.andExpect(jsonPath("$.tags[" + j + "].id").value(pet.getTags().get(j).getId()))
                  .andExpect(jsonPath("$.tags[" + j + "].name").value(pet.getTags().get(j).getName()));
        }
    }
}
