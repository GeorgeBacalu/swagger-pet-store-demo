package com.endava.petstore.controller;

import com.endava.petstore.model.User;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api(value = "User REST Controller", description = "Operations about user", tags = "user")
public interface UserApi {

    @ApiOperation(value = "Get all users", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 404, message = "No users found"),
          @ApiResponse(code = 500, message = "Internal server error")})
    ResponseEntity<List<User>> findAll();

    @ApiOperation(value = "Find user by ID", notes = "Returns a single user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<User> findById(@ApiParam(value = "ID of user to return", example = "1", required = true) Long id);

    @ApiOperation(value = "Create user", notes = "This can only be done by the logged in user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    ResponseEntity<User> save(@ApiParam(value = "Created user object", required = true) User user);

    @ApiOperation(value = "Update an existing user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found"),
          @ApiResponse(code = 405, message = "Validation exception")})
    ResponseEntity<User> update(@ApiParam(value = "User object that needs to be updated", required = true) User user);

    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
          @ApiResponse(code = 204, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid ID supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<Void> deleteById(@ApiParam(value = "User ID to delete", example = "1", required = true) Long id);
}
