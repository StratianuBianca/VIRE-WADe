package info.wade.users.controller;

import info.wade.users.dto.LoginDTO;
import info.wade.users.dto.LoginReturn;
import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UserDTO;
import info.wade.users.service.auth.AuthService;
import info.wade.users.service.jwt.UserDetailsServiceImpl;
import info.wade.users.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDTO loginDTO) throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        LoginReturn loginReturn = userDetailsService.getUser(loginDTO.getEmail());
        loginReturn.setJwt(jwtUtil.generateToken(userDetails.getUsername()));

        return new ResponseEntity<>(loginReturn, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<?> signupUser(@RequestBody RegisterDTO registerDTO) {
        UserDTO createdUser = authService.createUser(registerDTO);
        if (createdUser == null){
            return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
