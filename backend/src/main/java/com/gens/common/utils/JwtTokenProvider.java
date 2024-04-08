package com.gens.common.utils;

import com.gens.common.exceptionHandling.CustomResponseException;
import com.gens.common.exceptionHandling.ErrorCodeEnum;
import com.gens.usermanagement.model.document.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwtToken.secretKey}")
    private String secretKey;

    @Value("${jwtToken.expirationTime}")
    private long validityInMilliseconds;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getEmail());
        claims.put("email", userDetails.getEmail());
        claims.put("role", userDetails.getRole().toLowerCase());
        claims.put("customerId", userDetails.getCustomerId());
        claims.put("secretKey", userDetails.getUuid());
        Date now = new Date();
        Instant currentInstant = Instant.now();
        long milliseconds = currentInstant.toEpochMilli();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void validateSecretKey(User user, String secretKey) {
        if (!user.getUuid().equals(secretKey)) {
            throw new CustomResponseException(ErrorCodeEnum.ER1005, HttpStatus.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Exception while validation token, Reason: {}", e.getMessage());
            throw e;
        }
    }
}
