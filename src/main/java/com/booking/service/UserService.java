package com.booking.service;

import com.booking.payload.UserDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    public UserDTO createUser(UserDTO userDTO);

    Page<UserDTO> getAllUsers(Pageable pageable);
    void deleteUser(long userId);
    UserDTO updateUser(Long id, UserDTO userDTO);
    InputStreamResource getUserAsExcel();
    InputStreamResource getUserAsPdf() throws Exception;
    InputStreamResource getUserCsv();

}
