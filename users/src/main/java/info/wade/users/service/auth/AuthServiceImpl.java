package info.wade.users.service.auth;

import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UserDTO;
import info.wade.users.entity.User;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO createUser(RegisterDTO registerDTO) {
        User findIfUserExists = userRepository.findFirstByEmail(registerDTO.getEmail());
        if(findIfUserExists != null){
            return new UserDTO();
        }
        User user = new User();
        user.setName(registerDTO.getName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(registerDTO.getPassword()));
        User createdUser = userRepository.save(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setName(createdUser.getName());
        userDTO.setId(createdUser.getId());
        return userDTO;
    }

    @Override
    public UserDTO updateUser(RegisterDTO registerDTO){
        User findIfUserExists = userRepository.findByEmail(registerDTO.getEmail());
        if(findIfUserExists == null){
            return new UserDTO();
        }
        findIfUserExists.setEmail(registerDTO.getEmail());
        findIfUserExists.setName(registerDTO.getName());
        findIfUserExists.setPassword(registerDTO.getPassword());
        User updatedUser = userRepository.save(findIfUserExists);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(updatedUser.getEmail());
        userDTO.setName(updatedUser.getName());
        userDTO.setId(updatedUser.getId());
        return userDTO;
    }
}
