package com.example.Learnova.testAccess.repository;

import com.example.Learnova.testAccess.model.TestAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestAccessRepository extends JpaRepository<TestAccess, Long> {

    //Used when student opens shared link
    Optional<TestAccess> findByAccessToken(String accessToken);

    //Validate only active access
    Optional<TestAccess> findByAccessTokenAndActiveTrue(String accessToken);

}