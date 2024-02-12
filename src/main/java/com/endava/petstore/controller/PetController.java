package com.endava.petstore.controller;

import com.endava.petstore.model.Pet;
import com.endava.petstore.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> findAll() {
        return ResponseEntity.ok(petService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> findById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(petService.save(pet));
    }

    @PutMapping
    public ResponseEntity<Pet> update(@RequestBody Pet pet) {
        return ResponseEntity.ok(petService.update(pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        petService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
