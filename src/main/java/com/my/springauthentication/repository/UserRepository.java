package com.my.springauthentication.repository;

import com.my.springauthentication.model.Role;
import com.my.springauthentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    User findByRole(Role role);
}
