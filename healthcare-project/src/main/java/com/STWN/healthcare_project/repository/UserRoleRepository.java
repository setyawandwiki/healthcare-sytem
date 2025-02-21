package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    void deleteByIdUserId(Long userId);
    @Query(value = """
            SELECT * FROM user_role
            WHERE user_id = :userId
            AND role_id = :roleId
            LIMIT 1
            """, nativeQuery = true)
    Optional<UserRole> existsByUserIdAndRoleId(Long userId, Long roleId);
}
