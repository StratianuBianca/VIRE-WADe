package info.wade.users.service.jwt;

import info.wade.users.dto.LoginReturn;
import info.wade.users.entity.User;
import info.wade.users.repository.UserRepository;
import info.wade.users.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User not found or you already have an account",null);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
    public LoginReturn getUser(String email){
        User user = userRepository.findFirstByEmail(email);
        LoginReturn loginReturn = new LoginReturn();
        loginReturn.setId(user.getId());
        loginReturn.setUsername(user.getName());
        return loginReturn;
    }
}
