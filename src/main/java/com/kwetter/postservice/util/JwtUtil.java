package com.kwetter.postservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtil {

    private String SECRET_KEY = "changekeyandlocation";

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token){
        return Long.parseLong(extractClaim(token, Claims::getId));
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public ArrayList<String> extractRoles(String token) {
        return (ArrayList<String>) extractClaim(token, claims -> claims.get("Roles"));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

}
