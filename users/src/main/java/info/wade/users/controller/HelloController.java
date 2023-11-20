package info.wade.users.controller;

import info.wade.users.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Autowired
    private AuthService authService;

    @GetMapping("/hello")
    public String hello() {
        return ("Hello");
    }


}
