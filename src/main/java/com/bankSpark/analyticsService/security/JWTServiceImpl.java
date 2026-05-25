package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.repository.AnalystRepository;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JWTServiceImpl implements JWTService {

    private final AnalystRepository analystRepository;
    private static final Logger LOGGER = LogManager.getLogger(JWTServiceImpl.class);

    @Value("${app.signature_key}")
    private String signatureKey;

    @Autowired
    public JWTServiceImpl(AnalystRepository analystRepository) {
        this.analystRepository = analystRepository;
    }

    //Получить подпись ключа
    private SecretKey getSignInKey(){
        try {
            byte[] keyBytes = Decoders.BASE64.decode(signatureKey);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            throw new RuntimeException("Invalid secret key", e);
        }
    }

    //Получить роль
    @Override
    public String getRoleAnalyst(String login){

        Analyst analyst = analystRepository.getAnalystByLogin(login).get();

        return analyst.getRole();
    }

    //Получить токен пользователя
    @Override
    public String getTokenAnalyst(String login){
        Analyst analyst = analystRepository.getAnalystByLogin(login).get();
        return analyst.getToken().getToken();
    }

    //Валидация токена , проверка на ориг
    @Override
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
            LOGGER.info("Token validated");
            return true;
        }

        catch (ExpiredJwtException e){
            LOGGER.warn("Token expired - {}",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        catch (UnsupportedJwtException e){
            LOGGER.warn("Token unsupported - {}",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        catch (MalformedJwtException e){
            LOGGER.warn("Token malformed - {}",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        catch (SecurityException e){
            LOGGER.warn("Security exception - {}",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
        catch(Exception e){
            LOGGER.warn("JWT invalid Exception - {}",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Метод для генерации JWT токена
    @Override
    public String generateJwtToken(String login, String role, String password,String token) {
        Date lifeTimeJWT = Date.from(LocalDateTime.now().plusHours(24).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(login)
                .claim("role",role)
                .claim("password",password)
                .claim("token",token)
                .setExpiration(lifeTimeJWT)
                .signWith(getSignInKey())
                .compact();

    }

    //Метод при прохождении аутентификации которого - пользователь получает токен
    @Override
    public JwtAuthenticationDTO getTokenForUser(String login) {

        String password = analystRepository.getAnalystByLogin(login).get().getPassword();
        String role = getRoleAnalyst(login);
        String token = getTokenAnalyst(login);

        JwtAuthenticationDTO jwtDTO = new JwtAuthenticationDTO();
        jwtDTO.setToken(generateJwtToken(login, role, password,token));
        jwtDTO.setRefreshToken(generateJwtToken(login, role, password,token));

        return jwtDTO;
    }

    //Генерация рефреш токена
    @Override
    public JwtAuthenticationDTO generateRefreshToken(String login, String refreshToken) {

        String password = analystRepository.getAnalystByLogin(login).get().getPassword();
        String role = getRoleAnalyst(login);
        String token = getTokenAnalyst(login);

        JwtAuthenticationDTO jwtDTO = new JwtAuthenticationDTO();
        jwtDTO.setToken(generateJwtToken(login, role, password,token));
        jwtDTO.setRefreshToken(refreshToken);

        return jwtDTO;
    }

    @Override
    public String getLoginFromToken(String token) {
        //Claims - для хранения и парса полезной информации внутри JWT
        Claims login = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();

        return login.getSubject();
    }

    @Override
    public JwtAuthenticationDTO getOutFromAccount(String login) {

        String analystRole = getRoleAnalyst(login);
        String analystPassword = analystRepository.getAnalystByLogin(login).get().getPassword();
        String analystToken = getTokenAnalyst(login);

        Date lifeTimeToken = Date.from(LocalDateTime.now().plusSeconds(0).atZone(ZoneId.systemDefault()).toInstant());

        String deadJwt = Jwts.builder()
                .setSubject(login)
                .claim("role",analystRole)
                .claim("password",analystPassword)
                .claim("token",analystToken)
                .setExpiration(lifeTimeToken)
                .signWith(getSignInKey())
                .compact();

        JwtAuthenticationDTO expiredJWT = new JwtAuthenticationDTO();
        expiredJWT.setToken(deadJwt);
        expiredJWT.setRefreshToken(deadJwt);

        return expiredJWT;
    }

}