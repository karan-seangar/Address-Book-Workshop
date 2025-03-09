package com.lcwd.bridgelabz.addressbook.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lcwd.bridgelabz.addressbook.model.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
