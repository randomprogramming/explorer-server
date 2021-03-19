package com.randomprogramming.explorer.security;

import com.randomprogramming.explorer.services.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.access-token-secret}")
    private String accessTokenSecret;

    @Value("${security.jwt.token.access-token-life}")
    private int accessTokenLife;

    final private CustomUserDetailsService userDetailsService;

    public JwtTokenProvider(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        // Encode the access token secret into b64, just for some extra security
        accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenSecret.getBytes());
    }

    public String createToken(String username) {
        var today = new Date();
        var expirationDate = new Date(today.getTime() + accessTokenLife);

        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(today)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, accessTokenSecret)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        // subject is the username of the person to whom the token is issued to
        return Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public String extractTokenFromRequest(HttpServletRequest req) throws JwtException {
        String bearerToken = req.getHeader("Authorization");
        String tokenPrefix = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(tokenPrefix)) {
            // Remove the 'Bearer ' from the token and return it
            return bearerToken.substring(tokenPrefix.length());
        } else {
            throw new JwtException("Error when looking for JWT.");
        }
    }

    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parser().setSigningKey(accessTokenSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Expired or invalid JWT.");
        }
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getSubjectFromToken(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
