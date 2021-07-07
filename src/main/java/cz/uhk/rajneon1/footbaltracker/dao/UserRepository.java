package cz.uhk.rajneon1.footbaltracker.dao;

import cz.uhk.rajneon1.footbaltracker.model.User;
import cz.uhk.rajneon1.footbaltracker.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Nullable
    User getOneByLogin(String login);

    List<User> findByUserRole(UserRole userRole);

}
