package cz.uhk.rajneon1.footbaltracker.security;

import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.model.Player;
import cz.uhk.rajneon1.footbaltracker.model.User;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class GoogleOauthTokenHandler {

    private UserRepository userRepository;
    private EncryptionUtil encryptionUtil;

    public GoogleOauthTokenHandler(UserRepository userRepository, EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.encryptionUtil = encryptionUtil;
    }

    public void saveRefreshToken(String login, String refreshToken) throws IllegalBlockSizeException, InvalidKeyException,
            BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        User user = userRepository.getOneByLogin(login);
        if (user instanceof Player) {
            ((Player) user).setRefreshToken(encryptionUtil.encrypt(refreshToken));
            userRepository.save(user);
        }
    }

    public String retrieveRefreshToken(String login) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        User user = userRepository.getOneByLogin(login);
        if (user instanceof Player) {
            return encryptionUtil.decrypt(((Player) user).getRefreshToken());
        } else {
            return null;
        }
    }

}
