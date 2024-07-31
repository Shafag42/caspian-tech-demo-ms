package com.caspiantech.user.ms.service.Impl;


import com.caspiantech.user.ms.dao.entity.UserEntity;
import com.caspiantech.user.ms.exceptions.UnauthorizedException;
import com.caspiantech.user.ms.model.enums.AccountStatus;
import com.caspiantech.user.ms.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 45; // 45 min
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 5; // 5 days

    private final SecretKey secretKey;

    @Autowired
    public JwtServiceImpl(@Value("${security.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        checkAuthorization(userDetails);
        return generateToken(userDetails, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    private void checkAuthorization(UserDetails userDetails) {
        if (!isAuthorized(userDetails)) {
            throw new UnauthorizedException("User is not authorized to generate token");
        }
    }

    private String generateToken(UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", ((UserEntity) userDetails).getId())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey)
                .compact();
    }

    private boolean isAuthorized(UserDetails userDetails) {
        UserEntity userEntity = (UserEntity) userDetails;
        // Check if the user account is enabled
        return userEntity.getAccountStatus().equals(AccountStatus.ENABLED);
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        final Long userId = extractUserId(token);
        return (username.equals(userDetails.getUsername()) && userId.equals(((UserEntity) userDetails).getId()) && !isTokenExpired(token));
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token.trim())
                    .getPayload();
        } catch (SignatureException e) {
            throw new UnauthorizedException("Invalid token. Please check your token and try again.");
        }
    }

    public Date getExpirationDateFromToken(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
