package info.wade.users.service.auth;

import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UpdateUserDTO;
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
    public RegisterDTO updateUser(UpdateUserDTO updateUserDTO){
        User findIfUserExists = userRepository.findById(updateUserDTO.getId());
        if(findIfUserExists == null){
            return new RegisterDTO();
        }
        findIfUserExists.setEmail(updateUserDTO.getEmail());
        findIfUserExists.setName(updateUserDTO.getName());
        findIfUserExists.setPassword(new BCryptPasswordEncoder().encode(updateUserDTO.getPassword()));
        User updatedUser = userRepository.save(findIfUserExists);
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail(updatedUser.getEmail());
        registerDTO.setName(updatedUser.getName());
        registerDTO.setPassword(updatedUser.getPassword());
        return registerDTO;
    }

    public UpdateUserDTO getUser(Long id){
        User findIfUserExists = userRepository.findById(id);
        if(findIfUserExists == null){
            return new UpdateUserDTO();
        }
        UpdateUserDTO user = new UpdateUserDTO();
        user.setEmail(findIfUserExists.getEmail());
        user.setName(findIfUserExists.getName());
        user.setPassword(findIfUserExists.getPassword());
        user.setId(findIfUserExists.getId());
        return user;
    }
}
