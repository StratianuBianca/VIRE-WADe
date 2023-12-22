package info.wade.users.controller;

import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UpdateUserDTO;
import info.wade.users.service.auth.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthServiceImpl authService;

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDTO updateUserDTO) {
        RegisterDTO registerDTO = authService.updateUser(updateUserDTO);
        return new ResponseEntity<>(registerDTO, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId){
        UpdateUserDTO getUser = authService.getUser(userId);
        return new ResponseEntity<>(getUser, HttpStatus.OK);
    }

}
