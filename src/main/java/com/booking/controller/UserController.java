package com.booking.controller;

import com.booking.payload.UserDTO;
import com.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //   http://localhost:8080/api/users/create
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setProfileImage(profileImage);

        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //   http://localhost:8080/api/users?page=1&size=10&sort=id,asc
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUser(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        Page<UserDTO> allUsers = userService.getAllUsers(pageable);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    //   http://localhost:8080/api/users/1
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User Deleted Successful!!!", HttpStatus.OK);
    }

    //http://localhost:8080/api/users/1
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,
                                          @RequestParam("firstName") String firstName,
                                          @RequestParam("lastName") String lastName,
                                          @RequestParam("email") String email,
                                          @RequestParam("password") String password,
                                          @RequestParam("phoneNumber") String phoneNumber,
                                          @RequestParam(value = "profileImage", required = false)
                                          MultipartFile profileImage) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setPhoneNumber(phoneNumber);
        userDTO.setProfileImage(profileImage);
        UserDTO createdUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //http://localhost:8080/api/users/download/excel
    @GetMapping("/download/excel")
    public ResponseEntity<InputStreamResource> downloadUsersAsExcel() {
        try {
            InputStreamResource stream = userService.getUserAsExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=users.xlsx");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return ResponseEntity.ok().headers(headers).body(stream);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //http://localhost:8080/api/users/download/pdf
    @GetMapping(value = "/download/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getUserAsPdf() {
        try {
            InputStreamResource pdf = userService.getUserAsPdf();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=users.pdf");
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //http://localhost:8080/api/users/download/csv
    @GetMapping(value = "/download/csv", produces = "text/csv")
    public ResponseEntity<InputStreamResource> getUserCsv() {
        InputStreamResource csvStream = userService.getUserCsv();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=users.csv");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("text/csv")).body(csvStream);
    }
}


