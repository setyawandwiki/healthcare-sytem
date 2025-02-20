package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.*;
import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.Role;
import com.STWN.healthcare_project.entity.User;
import com.STWN.healthcare_project.entity.UserRole;
import com.STWN.healthcare_project.model.UserRegisterRequest;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.repository.RoleRepository;
import com.STWN.healthcare_project.repository.UserRepository;
import com.STWN.healthcare_project.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse registerRequest(UserRegisterRequest request) {
        if(existsByEmail(request.getEmail())){
            throw new EmailAlreadyExist("email already exist");
        }

        if(existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExist("username already exist");
        }

        if(!request.getPassword().equals(request.getPasswordConfirmation())){
            throw new BadRequestException("password is not matched");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        userRepository.save(user);

        Role role = roleRepository.findByName(RoleType.PATIENT)
                .orElseThrow(()-> new ResourceNotFoundException("role is not found"));

        UserRole userRole = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getUserId(), role.getRoleId()))
                .build();

        userRoleRepository.save(userRole);

        return UserResponse.fromUSerAndRole(user, List.of(role));
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found exception with id : " + id));
        List<Role> roles = roleRepository.findByUserIds(id);
        return UserResponse.fromUSerAndRole(user, roles);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user not found exception with username : " + username));
        List<Role> roles = roleRepository.findByUserIds(user.getUserId());
        return UserResponse.fromUSerAndRole(user, roles);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
