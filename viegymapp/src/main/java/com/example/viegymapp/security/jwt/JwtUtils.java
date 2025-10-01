package com.example.viegymapp.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${viegym.app.jwtSecret}")
    private String jwtSecret;

    @Value("${viegym.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${viegym.app.jwtCookieName}")
    private String jwtCookie;

    @Value("${viegym.app.jwtRefreshCookieName}")
    private String jwtRefreshCookie;

    //Tạo JWT từ userName
    public String generateTokenFromUsername(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Sinh key từ secret
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Sinh access token cookie
    public ResponseCookie generateJwtCookie(String userName) {
        String jwt = generateTokenFromUsername(userName);
        return generateCookie(jwtCookie, jwt, "/api");
    }

    //Sinh refresh token cookie
    public ResponseCookie generateRefreshJwtCookie(String refreshToken){
        return generateCookie(jwtRefreshCookie, refreshToken, "/api/auth");
    }

    // Lấy Token từ cookie
    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtCookie);
    }
    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValueByName(request, jwtRefreshCookie);
    }

    //Xoá cookie
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/api").maxAge(0).build();
    }
    public ResponseCookie getCleanJwtRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookie, null).path("/api/auth/refreshtoken").maxAge(0).build();
    }

    //Lấy username từ JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    //Tạo cookie chứa JWT
    private ResponseCookie generateCookie(String name, String value, String path){
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .maxAge(24 * 60 * 60)
                .httpOnly(true).build();
        return cookie;
    }

    //lấy giá trị cookie theo tên
    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

}
