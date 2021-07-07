package cz.uhk.rajneon1.footbaltracker.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordHandler {

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean matchPasswords(String plain, String hashed) {
        return BCrypt.checkpw(plain, hashed);
    }

}
