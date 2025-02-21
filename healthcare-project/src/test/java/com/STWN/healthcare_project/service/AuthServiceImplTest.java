package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.exception.InvalidPasswordException;
import com.STWN.healthcare_project.constant.RoleType;
import com.STWN.healthcare_project.entity.Role;
import com.STWN.healthcare_project.entity.User;
import com.STWN.healthcare_project.model.AuthRequest;
import com.STWN.healthcare_project.model.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthRequest authRequest;
    private UserInfo userInfo;

    @BeforeEach
    void setUp(){
        authRequest = new AuthRequest("testUser", "testPassword");
        userInfo = new UserInfo(
                User.builder()
                        .username("testUser")
                        .build(),
                List.of(Role.builder()
                                .name(RoleType.PATIENT)
                        .build())
        );
    }

    @Test
    void authenticate_SuccessFullAuthentication_ReturnUserInfo(){
        Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null);
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

        UserInfo result = authService.authenticate(authRequest);

        assertNotNull(result);
        assertEquals(userInfo.getUsername(), result.getUsername());
    }

    @Test
    void authenticate_FailedAuthentication_ThrowInvalidPassword(){
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)));
        assertThrows(InvalidPasswordException.class, ()-> authService.authenticate(authRequest));
    }
}