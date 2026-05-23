package com.bankSpark.analyticsService.security.randomKeyAPI;

import com.bankSpark.analyticsService.DTO.inviteTokenDTO.FullTokenInfoDTO;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.exception.GenerateKeyException;
import com.bankSpark.analyticsService.mapper.InviteTokenMapper;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.DTO.inviteTokenDTO.TokenResponseDTO;
import com.bankSpark.analyticsService.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
public class InviteTokenServiceImpl implements InviteTokenService {

    private static final Logger log = LoggerFactory.getLogger(InviteTokenServiceImpl.class);

    private final InviteTokenRepository inviteTokenRepository;

    @Autowired
    public InviteTokenServiceImpl(InviteTokenRepository inviteTokenRepository) {
        this.inviteTokenRepository = inviteTokenRepository;
    }

    private static final int DEFAULT_HOURS_VALID = 72;  // 24 часа
    private static final String DEFAULT_ROLE = Roles.ANALYST.name();

    @Transactional
    public TokenResponseDTO generateToken(String role, Integer hoursValid) {

        try {
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
            token.setExpiresAt(Instant.now().plusSeconds(validHours * 3600L));
            token.setCreatedAt(Instant.now());

            // Сохраняем
            InviteToken savedToken = inviteTokenRepository.save(token);

            log.info("Generated invite token: {} for role {} (valid for {} hours)",
                    tokenValue, finalRole, validHours);

            return new TokenResponseDTO(
                    savedToken.getToken(),
                    savedToken.getRole(),
                    LocalDateTime.ofInstant(savedToken.getExpiresAt(), ZoneId.systemDefault()),
                    LocalDateTime.ofInstant(savedToken.getCreatedAt(), ZoneId.systemDefault())
            );
        }
        catch (Exception e) {
            throw new GenerateKeyException();
        }

    }

    /**
     * Генерация токена с параметрами по умолчанию (ANALYST, 24 часа)
     */
    public TokenResponseDTO generateDefaultToken() {
        return generateToken(DEFAULT_ROLE, DEFAULT_HOURS_VALID);
    }

    private String generateUniqueToken() {
        String token;
        do {
            String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            token = "ANALYST-" + randomPart;
        } while (inviteTokenRepository.findByToken(token).isPresent());

        return token;
    }

    //Валидировать токен
    public boolean isValidToken(String token) {
        return inviteTokenRepository
                .findByTokenAndUsedFalseAndExpiresAtAfter(token, LocalDateTime.now())
                .isPresent();
    }

    //Извлечь полную информацию по токену
    public InviteToken getTokenInfo(String token) {
        return inviteTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found: " + token));
    }

    /**
     * Ручная отмена токена (админ)
     */
    @Transactional
    public void revokeToken(String token) {
        InviteToken inviteToken = inviteTokenRepository
                .findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found: " + token));

        if (inviteToken.getUsed()) {
            throw new RuntimeException("Cannot revoke token that has already been used");
        }

        inviteTokenRepository.delete(inviteToken);

        log.info("Token revoked and deleted: {}", token);
    }

    /**
     * Удалить все неиспользованные токены
     */
    @Transactional
    public int cleanupExpiredTokens() {
        Instant now = Instant.now();

        // Считаем, сколько будет удалено
        int countToDelete = inviteTokenRepository.countByUsedFalseAndExpiresAtBefore(now);

        // Удаляем
        int deletedCount = inviteTokenRepository.deleteByUsedFalseAndExpiresAtBefore(now);

        log.info("Found {} expired tokens, deleted {}", countToDelete, deletedCount);
        return deletedCount;
    }

    @Override
    public List<FullTokenInfoDTO> getAllInviteTokens() {
        return inviteTokenRepository.findAll().stream()
                .map(x -> InviteTokenMapper.toFullDTO(x))
                .toList();
    }

    @Override
    public List<FullTokenInfoDTO> getAllInviteTokensByUsed(Boolean used) {

        if(used != null) {

            return inviteTokenRepository.findAll().stream()
                    .filter(x -> x.getUsed() == used)
                    .map(x -> InviteTokenMapper.toFullDTO(x))
                    .toList();

        }
        throw new GenerateKeyException();
    }

}