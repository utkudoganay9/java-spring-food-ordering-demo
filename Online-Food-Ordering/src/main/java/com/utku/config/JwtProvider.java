package com.utku.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;

@Service
public class JwtProvider {

   private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

   public String generatedToken(Authentication auth){
       Collection<? extends GrantedAuthority>authorities=auth.getAuthorities();
       String roles=populateAuthorities(authorities);

       String jwt = Jwts.builder().issuedAt(new Date())
               .expiration((new Date(new Date().getTime()+86400000)))
               .claim("email",auth.getName())
               .claim("authorities",roles)
               .signWith(key)
               .compact();
       return jwt;
   }
   public String getEmailFromJwtToken(String jwt){
       jwt=jwt.substring(7);
       Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
       String email= String.valueOf(claims.get("email"));
       return email;
   }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String>auths=new HashSet<>();

        for (GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());

        }

        return String.join(",",auths);
    }
}
