package cz.uhk.rajneon1.footbaltracker.security;

import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserVerifier {

    private PasswordHandler passwordHandler;
    private UserRepository userRepository;

    public UserVerifier(PasswordHandler passwordHandler, UserRepository userRepository) {
        this.passwordHandler = passwordHandler;
        this.userRepository = userRepository;
    }

    public boolean verifyUser(String username, String password) {
        User user = userRepository.getOneByLogin(username);
        if (user == null) {
            return false;
        } else return passwordHandler.matchPasswords(password, user.getPassword());
    }
}
