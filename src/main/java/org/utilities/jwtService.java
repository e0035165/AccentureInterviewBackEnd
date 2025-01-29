package org.utilities;


import org.controllerz.AuthRequest;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
public class jwtService {

    @Value(value = "${jwt.secret-key}")
    private String secretKey;



    public String encrypt(AuthRequest userDetails) {
        return encrypt(Map.of("username", userDetails.username(), "password", userDetails.password()));
    }


    public String encrypt(Map<String,Object> payload) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        String jwt = Jwts.builder().addClaims(payload)
                .setExpiration(Date.from(Instant.now().plusSeconds(60*60*24*365)))
                .signWith(key,SignatureAlgorithm.HS256).compact();
        return jwt;
    }

    public Claims extractAllClaims(String jwt) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

    }


    public String getUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("username", String.class);
    }

    public String getPasswordFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("password", String.class);
    }



    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }



    public boolean isTokenExpired(Claims claims) {
        return !claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }

//    public static void main(String... args) throws NoSuchAlgorithmException {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(256);
//        SecretKey secretKey = keyGenerator.generateKey();
//        String secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println(secret);
//    }

}
