package com.example.Learnova.user.repository;

import com.example.Learnova.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserInfo,Long> {

    boolean existsByEmail(String email);

    Optional<UserInfo> findByEmail(String email);
}
