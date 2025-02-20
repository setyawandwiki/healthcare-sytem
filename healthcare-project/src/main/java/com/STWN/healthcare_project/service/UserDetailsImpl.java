package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.UserNotFoundException;
import com.STWN.healthcare_project.entity.Role;
import com.STWN.healthcare_project.entity.User;
import com.STWN.healthcare_project.model.UserInfo;
import com.STWN.healthcare_project.repository.RoleRepository;
import com.STWN.healthcare_project.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsImpl implements UserDetailsService {

    private final String USER_CACHE_KEY = "cache:user";
    private final String USER_ROLES_CACHE_KEY = "cache:user:roles";
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CacheService cacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String userCacheKey = USER_CACHE_KEY + username;
        String roleCacheKey = USER_ROLES_CACHE_KEY + username;

        Optional<User> userOpt = cacheService.get(userCacheKey, com.STWN.healthcare_project.entity.User.class);
        Optional<List<Role>> rolesOpt = cacheService.get(roleCacheKey, new TypeReference<List<Role>>() {

        });

        if(userOpt.isPresent() && rolesOpt.isPresent()){
            return UserInfo.builder()
                    .roles(rolesOpt.get())
                    .user(userOpt.get())
                    .build();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("User with username " + username + " is not ofund"));
        List<Role> roles = roleRepository.findByUserIds(user.getUserId());

        UserInfo userInfo = UserInfo.builder()
                .roles(roles)
                .user(user)
                .build();

        cacheService.put(userCacheKey, userInfo);
        cacheService.put(roleCacheKey, roles);

        return userInfo;
    }
}
