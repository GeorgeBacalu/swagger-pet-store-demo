package com.endava.petstore.controller;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.HttpResponse;
import com.endava.petstore.model.Pet;
import com.endava.petstore.model.PetUpdateFormDataRequest;
import com.endava.petstore.model.PetUploadImageRequest;
import com.endava.petstore.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping(value = "/pet", produces = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
@RequiredArgsConstructor
public class PetController implements PetApi {
    private final PetService petService;

    @Override @GetMapping
    public ResponseEntity<List<Pet>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @Override @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @Override @PostMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> save(@RequestBody @Valid Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.save(pet));
    }

    @Override @PutMapping(consumes = {APPLICATION_JSON_VALUE, APPLICATION_XML_VALUE})
    public ResponseEntity<Pet> update(@RequestBody @Valid Pet pet) {
        return ResponseEntity.ok(petService.update(pet));
    }

    @Override @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override @GetMapping("/findByStatus")
    public ResponseEntity<List<Pet>> findByStatuses(@RequestParam @Valid PetStatus[] status) {
        return ResponseEntity.ok(petService.findByStatuses(status));
    }

    @Override @GetMapping("/findByTags")
    public ResponseEntity<List<Pet>> findByTags(@RequestParam @Valid List<String> tags) {
        return ResponseEntity.ok(petService.findByTags(tags));
    }

    @Override @PostMapping(value = "/{id}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> updateWithFormData(@PathVariable Long id, PetUpdateFormDataRequest petUpdateRequest) {
        return ResponseEntity.ok(petService.updateWithFormData(id, petUpdateRequest.getName(), petUpdateRequest.getStatus()));
    }

    @Override @PostMapping(value = "/{id}/uploadImage", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> uploadImage(@PathVariable Long id, PetUploadImageRequest petUploadImageRequest, @RequestPart MultipartFile file) {
        return ResponseEntity.ok(petService.uploadImage(id, petUploadImageRequest.getAdditionalMetadata(), file));
    }
}
