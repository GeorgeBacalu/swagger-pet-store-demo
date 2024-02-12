package com.endava.petstore.controller;

import com.endava.petstore.enums.PetStatus;
import com.endava.petstore.model.Pet;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(value = "Pet REST Controller", description = "Everything about your pets", tags = "pet")
public interface PetApi {

    @ApiOperation(value = "Get all pets", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<Pet>> findAll();

    @ApiOperation(value = "Find pet by ID", notes = "Returns a single pet", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found")})
    ResponseEntity<Pet> findById(@ApiParam(value = "ID of pet to return", example = "1", required = true) Long id);

    @ApiOperation(value = "Add a new pet to the store", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    ResponseEntity<Pet> save(@ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet);

    @ApiOperation(value = "Update an existing pet", response = Pet.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    ResponseEntity<Pet> update(@ApiParam(value = "Pet object that needs to be added to the store", required = true) Pet pet);

    @ApiOperation(value = "Delete a pet")
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "Pet not found")})
    ResponseEntity<Void> deleteById(@ApiParam(value = "Pet ID to delete", example = "1", required = true) Long id);

    @ApiOperation(value = "Finds pets by status", notes = "Multiple status values can be provided with comma separated strings", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid status value")})
    ResponseEntity<List<Pet>> findByStatuses(@ApiParam(value = "Status values that need to be considered for filter", allowableValues = "available, pending, sold", allowMultiple = true, required = true) PetStatus[] status);

    @ApiOperation(value = "Finds pets by tags", notes = "Multiple tags can be provided with comma separated string. Use Tag1, Tag2, Tag3 for testing", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid tag value")})
    ResponseEntity<List<Pet>> findByTags(@ApiParam(value = "Tags to filter by", allowMultiple = true, required = true) List<String> tags);
}
