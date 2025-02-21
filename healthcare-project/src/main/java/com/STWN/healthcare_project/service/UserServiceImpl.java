package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.*;
import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.Role;
import com.STWN.healthcare_project.entity.User;
import com.STWN.healthcare_project.entity.UserRole;
import com.STWN.healthcare_project.model.UserRegisterRequest;
import com.STWN.healthcare_project.model.UserResponse;
import com.STWN.healthcare_project.model.UserUpdateRequest;
import com.STWN.healthcare_project.repository.RoleRepository;
import com.STWN.healthcare_project.repository.UserRepository;
import com.STWN.healthcare_project.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final String USER_CACHE_KEY ="cache:user:";
    private final String USER_ROLES_CACHE_KEY = "cache:user:roles:";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;

    @Override
    @Transactional
    public UserResponse registerRequest(UserRegisterRequest request) {
        if (existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExist("Email "+request.getEmail()+" is already taken");
        }

        if (existsByUsername(request.getUsername())) {
            throw new EmailAlreadyExist("Username "+request.getUsername()+" is already taken");
        }

        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new BadRequestException("Password is not matched");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .build();

        userRepository.save(user);
        log.error("TEST 1");
        Role role = roleRepository.findByName(RoleType.PATIENT)
                .orElseThrow(()-> new ResourceNotFoundException("role is not found"));
        log.error("TEST 2");
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

    @Override
    public UserResponse updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user with id " + userId + " is not found"));

        if(request.getCurrentPassword() != null && request.getNewPassword() != null){
            if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
                throw new InvalidPasswordException("Current password is incorrect");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        if(request.getEmail() != null && !request.getEmail().equals(user.getEmail())){
            if(existsByEmail(request.getEmail())){
                throw new EmailAlreadyExist("email already in used");
            }
            user.setEmail(request.getEmail());
        }
        if(request.getUsername() != null && !request.getUsername().equals(user.getUsername())){
            if(existsByEmail(request.getUsername())){
                throw new UsernameAlreadyExist("username already in used");
            }
            user.setUsername(request.getUsername());
        }

        userRepository.save(user);
        List<Role> roles = roleRepository.findByUserIds(userId);

        String userCacheKey = USER_CACHE_KEY + user.getUsername();
        String rolesCacheKey = USER_ROLES_CACHE_KEY + user.getUsername();

        cacheService.evict(userCacheKey);
        cacheService.evict(rolesCacheKey);

        return UserResponse.fromUSerAndRole(user, roles);
    }
}
