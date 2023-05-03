package com.booking.service.impl;
import com.booking.entities.User;
import com.booking.payload.UserDTO;
import com.booking.repository.UserRepository;
import com.booking.service.UserService;
import com.booking.util.CsvExplore;
import com.booking.util.ExcelExporter;
import com.booking.util.PdfExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private  String uploadDirectory = "src/main/resources/static/user_profile_image/";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = dtoToUser(userDTO);
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // Encode the password
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

        MultipartFile profileImage = userDTO.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = saveProfileImage(profileImage);
            user.setProfilePicture(fileName);
        }

        User savedUser = userRepository.save(user);
        return userToDto(savedUser);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOS = userPage.stream().map(user -> userToDto(user)).collect(Collectors.toList());
        return new PageImpl<>(userDTOS,pageable,userPage.getTotalElements());
    }

    @Override
    public void deleteUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        userRepository.delete(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        // Update the fields of the user object
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        // If a new password is provided, encode and set it
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        }
        // If a new profile image is provided, save it and set the filename
        MultipartFile profileImage = userDTO.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = saveProfileImage(profileImage);
            user.setProfilePicture(fileName);
        }
        // Set the update time
        user.setUpdatedAt(new Date());
        User updatedUser = userRepository.save(user);
        return userToDto(updatedUser);
    }

    //This Is Ensure that Every Upload file has a unique name
    private String saveProfileImage(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String baseFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            String uniqueFileName = baseFileName + "_" + System.currentTimeMillis() + fileExtension;
            Path path = Paths.get(uploadDirectory + uniqueFileName);
            Files.write(path, bytes);
            return uniqueFileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save profile image", e);
        }
    }

    @Override
    public InputStreamResource getUserAsExcel() {
        List<User> users = userRepository.findAll();
        try {
            return ExcelExporter.exportUsersToExcel(users);
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel file", e);
        }
    }

    @Override
    public InputStreamResource getUserAsPdf() throws Exception{
        List<UserDTO> userDTOS = userRepository.findAll().stream().map(e ->
                userToDto(e)).collect(Collectors.toList());
        ByteArrayInputStream pdfInputStream=
                PdfExplorer.exportUsersToPdf(userDTOS);
        return new InputStreamResource(pdfInputStream);
    }

    @Override
    public InputStreamResource getUserCsv() {
        List<UserDTO> userDTOS =
                userRepository.findAll().stream().map(e ->
                        userToDto(e)).collect(Collectors.toList());
        ByteArrayInputStream pdfInputStream=
                CsvExplore.exportUsersToCsv(userDTOS);
        return new InputStreamResource(pdfInputStream);
    }


    private User dtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPassword());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        return user;
    }

    private UserDTO userToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setProfilePicture(user.getProfilePicture());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }
}
