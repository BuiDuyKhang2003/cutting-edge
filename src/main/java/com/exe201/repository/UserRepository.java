package com.exe201.repository;


import com.exe201.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    @Query("""
            SELECT u FROM User u WHERE u.email LIKE CONCAT('%', :email, '%') AND u.role.roleId <> 2
            """)
    Page<User> findByEmail(@Param("email") String email, Pageable pageable);

    @Query("""
            SELECT COUNT(u) FROM User u WHERE u.role.roleId = :roleId
            """)
    long countUserWithRoleId(@Param("roleId") long roleId);
}

