package com.example.Learnova.testPerformer.repository;


import com.example.Learnova.testAccess.model.TestAccess;
import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {

    int countByTestAccessAndUser(TestAccess access, UserInfo user);
}
