package com.csci310.db.repositories;  

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.csci310.db.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>  
{
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndPassword(String username, String password);

    Optional<User> findById(UUID id);

    @Modifying
    @Query(value = "DELETE FROM User u WHERE u.id = :user_id")
    void deleteUserById(UUID user_id);

    @Modifying
    @Query(value = "DELETE FROM User u WHERE u.username = :username")
    void deleteUserByUsername(String username);

}  