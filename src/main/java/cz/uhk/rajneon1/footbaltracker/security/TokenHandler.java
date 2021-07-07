package cz.uhk.rajneon1.footbaltracker.security;

import cz.uhk.rajneon1.footbaltracker.utils.DateUtil;
import io.jsonwebtoken.*;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenHandler {

    private Environment environment;

    public TokenHandler(Environment environment) {
        this.environment = environment;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, environment.getProperty("security.token.signingkey"))
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + DateUtil.WEEK))
                .compact();
    }

    public String getUsernameFromToken(String token) throws MalformedJwtException, SignatureException, ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(environment.getProperty("security.token.signingkey"))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}