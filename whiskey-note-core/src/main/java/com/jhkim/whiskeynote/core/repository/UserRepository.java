package com.jhkim.whiskeynote.core.repository;

import com.jhkim.whiskeynote.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String userName);
    Optional<User> findUserById(Long id);
}
