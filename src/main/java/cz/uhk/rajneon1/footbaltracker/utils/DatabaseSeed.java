package cz.uhk.rajneon1.footbaltracker.utils;

import cz.uhk.rajneon1.footbaltracker.dao.UserRepository;
import cz.uhk.rajneon1.footbaltracker.model.Trainer;
import cz.uhk.rajneon1.footbaltracker.model.UserRole;
import cz.uhk.rajneon1.footbaltracker.security.PasswordHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseSeed {

    private UserRepository userRepository;
    private PasswordHandler passwordHandler;

    public DatabaseSeed(UserRepository userRepository, PasswordHandler passwordHandler) {
        this.userRepository = userRepository;
        this.passwordHandler = passwordHandler;
    }

    @PostConstruct
    public void createTrainerIfTheresNone() {
        if (userRepository.findByUserRole(UserRole.TRAINER).isEmpty()) {
            Trainer trainer = new Trainer("trener", passwordHandler.hashPassword("Heslo12345*"));
            userRepository.save(trainer);
        }

    }

}
