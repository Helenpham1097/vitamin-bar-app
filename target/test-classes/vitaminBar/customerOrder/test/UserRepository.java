package com.vitaminBar.customerOrder.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query(value = "Select * from users where users.username =:userName", nativeQuery = true)
    Optional<Users> findUserByUserName (@Param("userName") String userName);

    @Query(value = "UPDATE users set failed_attempt =:failedAttempt where username =:userName",
            nativeQuery = true)
    @Modifying
    void updateFailedAttempts(@Param("failedAttempt") int failedAttempt,
                                     @Param("userName") String userName);
}
