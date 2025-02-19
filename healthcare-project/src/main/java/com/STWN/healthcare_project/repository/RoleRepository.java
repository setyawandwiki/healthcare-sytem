package com.STWN.healthcare_project.repository;

import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);

    @Query(value = """
            SELECT r.* FROM roles r
            JOIN user_role ur
            ON ur.role_id = r.role_id
            JOIN users u ON u.user_id = ur.user_id
            WHERE u.user_id = :userId
            """, nativeQuery = true)
    List<Role> findByUserIds(@Param("userId") Long userId);
}
