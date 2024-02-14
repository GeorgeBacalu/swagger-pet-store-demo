package com.endava.petstore.controller;

import com.endava.petstore.model.HttpResponse;
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

    @ApiOperation(value = "Create list of users with given input array", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    ResponseEntity<List<User>> saveAll(@ApiParam(value = "List of user objects", required = true) User[] users);

    @ApiOperation(value = "Create list of users with given input list", response = List.class)
    @ApiResponses(value = {
          @ApiResponse(code = 201, message = "Successful operation"),
          @ApiResponse(code = 405, message = "Invalid input")})
    ResponseEntity<List<User>> saveAll(@ApiParam(value = "List of user objects", required = true) List<User> users);

    @ApiOperation(value = "Get user by username", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid username supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<User> findByUsername(@ApiParam(value = "Username that needs to be fetched. Use Username1 for testing", required = true) String username);

    @ApiOperation(value = "Update user by username", notes = "This can only be done by the logged in user", response = User.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid username supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<User> updateByUsername(@ApiParam(value = "Updated user object", required = true) User user,
                                          @ApiParam(value = "Username that needs to be updated", required = true) String username);

    @ApiOperation(value = "Delete user by username", notes = "This can only be done by the logged in user")
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid username supplied"),
          @ApiResponse(code = 404, message = "User not found")})
    ResponseEntity<Void> deleteByUsername(@ApiParam(value = "Username that needs to be deleted", required = true) String username);

    @ApiOperation(value = "Logs user into the system", response = HttpResponse.class)
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 400, message = "Invalid username/password supplied")})
    ResponseEntity<HttpResponse> login(@ApiParam(value = "The username for login", required = true) String username,
                                       @ApiParam(value = "The password for login", required = true) String password);

    @ApiOperation(value = "Logs out current logged in user session")
    @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successful operation"),
          @ApiResponse(code = 401, message = "Unauthorized")})
    ResponseEntity<HttpResponse> logout(@ApiParam(value = "The username to logout", required = true) String username);
}
