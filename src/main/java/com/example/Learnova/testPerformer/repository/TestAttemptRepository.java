package com.example.Learnova.testPerformer.repository;


import com.example.Learnova.testAccess.model.TestAccess;
import com.example.Learnova.testPerformer.model.TestAttempt;
import com.example.Learnova.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAttemptRepository extends JpaRepository<TestAttempt, Long> {

    int countByTestAccessAndUser(TestAccess access, UserInfo user);

    @Query("""
    SELECT a FROM TestAttempt a
    JOIN FETCH a.testAccess ta
    JOIN FETCH ta.test t
    WHERE a.user = :user
      AND a.endTime IS NOT NULL
    ORDER BY a.endTime DESC
""")
    List<TestAttempt> findCompletedByUser(@Param("user") UserInfo user);
    int countByTestAccessAndUserAndEndTimeIsNotNull(
            TestAccess access,
            UserInfo user
    );
    TestAttempt findByTestAccessAndUserAndEndTimeIsNull(
            TestAccess access,
            UserInfo user
    );
}
