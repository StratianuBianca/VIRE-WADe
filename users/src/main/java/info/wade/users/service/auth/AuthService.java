package info.wade.users.service.auth;

import info.wade.users.dto.RegisterDTO;
import info.wade.users.dto.UserDTO;

public interface AuthService {
    UserDTO createUser(RegisterDTO registerDTO);
    UserDTO updateUser(RegisterDTO registerDTO);
}
