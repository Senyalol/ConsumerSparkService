package com.bankSpark.analyticsService.security;

import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class InviteTokenService {

    private static final Logger log = LoggerFactory.getLogger(InviteTokenService.class);

    @Autowired
    private InviteTokenRepository inviteTokenRepository;

    private static final int DEFAULT_HOURS_VALID = 72;  // 24 часа
    private static final String DEFAULT_ROLE = Roles.ANALYST.name();

    /**
     * Генерация нового инвайт-токена
     * @param role роль для нового пользователя (ANALYST, MODERATOR, etc.)
     * @param hoursValid срок действия в часах
     * @return TokenResponse с информацией о токене
     */
    @Transactional
    public TokenResponseDTO generateToken(String role, Integer hoursValid) {

        // Устанавливаем значения по умолчанию
        String finalRole = (role == null || role.isEmpty()) ? DEFAULT_ROLE : role;
        int validHours = (hoursValid == null || hoursValid <= 0) ? DEFAULT_HOURS_VALID : hoursValid;

        // Генерируем уникальный токен
        String tokenValue = generateUniqueToken();

        // Создаём сущность
        InviteToken token = new InviteToken();
        token.setToken(tokenValue);
        token.setRole(finalRole);
        token.setUsed(false);
        token.setExpiresAt(LocalDateTime.now().plusHours(validHours));
        token.setCreatedAt(LocalDateTime.now());

        // Сохраняем
        InviteToken savedToken = inviteTokenRepository.save(token);

        log.info("Generated invite token: {} for role {} (valid for {} hours)",
                tokenValue, finalRole, validHours);

        return new TokenResponseDTO(
                savedToken.getToken(),
                savedToken.getRole(),
                savedToken.getExpiresAt(),
                savedToken.getCreatedAt()
        );
    }

}