package info.wade.users.service;

import info.wade.users.entity.User;
import info.wade.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void setSpotifyToken(Long userId, String token, String refreshToken) {
        User user = userRepository.findById(userId);
        user.setSpotifyToken(token);
        userRepository.save(user);
    }

    public User getUserById(Long userId){
        User user = userRepository.findById(userId);
        return user;
    }
}
