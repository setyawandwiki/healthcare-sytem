package com.STWN.healthcare_project.service;

import com.STWN.healthcare_project.common.util.DateUtil;
import com.STWN.healthcare_project.config.JwtSecretConfig;
import com.STWN.healthcare_project.model.UserInfo;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signInKey;
    @Override
    public String generateToken(UserInfo userInfo) {
        LocalDateTime expirationTime = LocalDateTime.now().plus(jwtSecretConfig.getJwtExpirationTime());
        Date expiryTime = DateUtil.convertLocalDateTimeToDate(expirationTime);
        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryTime)
                .signWith(signInKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(signInKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            log.error("error while parsing jwt token {}", token);
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
