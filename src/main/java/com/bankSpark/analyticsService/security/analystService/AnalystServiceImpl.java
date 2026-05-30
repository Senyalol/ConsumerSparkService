package com.bankSpark.analyticsService.security.analystService;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.ORM.inviteToken.InviteToken;
import com.bankSpark.analyticsService.exception.CreateAnalystException;
import com.bankSpark.analyticsService.exception.UpdateAnalystException;
import com.bankSpark.analyticsService.mapper.AnalystMapper;
import com.bankSpark.analyticsService.repository.AnalystRepository;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.security.JWTService;
import com.bankSpark.analyticsService.security.analystService.createChecks.*;
import com.bankSpark.analyticsService.security.analystService.updateChecks.*;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class AnalystServiceImpl implements AnalystService {

    private final AnalystRepository analystRepository;
    private final AnalystMapper analystMapper;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    private final InviteTokenRepository tokenRepository;

    private static final Logger LOGGER = LogManager.getLogger(AnalystServiceImpl.class);


    @Autowired
    public AnalystServiceImpl(AnalystRepository analystRepository, AnalystMapper analystMapper, InviteTokenRepository tokenRepository, PasswordEncoder passwordEncoder,JWTService jwtService) {
        this.analystRepository = analystRepository;
        this.analystMapper = analystMapper;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    @Override
    public AnalystInfoDTO createAnalyst(CreateAnalystDTO createAnalystDTO) {

        List<AnalystCreateCheck> createChecks = Arrays.asList(
          new AnalystTokenCreateCheck(tokenRepository),
          new AnalystLoginCreateCheck(analystRepository),
          new AnalystPasswordCreateCheck()
        );

        MainAnalystCreateCheck analystCreateCheck = new MainAnalystCreateCheck(createChecks);

        if(analystCreateCheck.allCreateChecks(createAnalystDTO)){

            Analyst newAnalyst = new Analyst();

            newAnalyst.setToken(tokenRepository.findByToken(createAnalystDTO.getToken()).get());
            newAnalyst.setLogin(createAnalystDTO.getLogin());
            newAnalyst.setPassword(passwordEncoder.encode(createAnalystDTO.getPassword()));
            InviteToken inviteToken = tokenRepository.findByToken(createAnalystDTO.getToken()).get();

            newAnalyst.setRole(inviteToken.getRole());
            analystRepository.save(newAnalyst);

            inviteToken.setUsed(true);
            tokenRepository.save(inviteToken);
            LOGGER.info("Analyst - {} , was created", newAnalyst.getLogin());

            return analystMapper.toFullInfoDTO(analystRepository.getAnalystByLogin(createAnalystDTO.getLogin()).get());
        }

        throw new CreateAnalystException();
    }

    @Override
    public int getIdByLogin(String login) {
        return login != null && analystRepository.getAnalystByLogin(login).isPresent()
                ? analystRepository.getAnalystByLogin(login).get().getId()
                : 0;
    }

    @Override
    public List<AnalystInfoDTO> getAnalysts() {
        return analystRepository.findAll().stream()
                .map(x -> analystMapper.toFullInfoDTO(x))
                .toList();
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsByRole(String role) {
        return analystRepository.findAll().stream()
                .filter(x -> x.getRole().equals(role))
                .map(x -> analystMapper.toFullInfoDTO(x))
                .toList();
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsAfterCreatedAt(Instant created_at) {
        return analystRepository.findAll().stream()
                .filter(x -> x.getCreatedAt().isAfter(created_at))
                .map(x -> analystMapper.toFullInfoDTO(x))
                .toList();
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsBeforeCreatedAt(Instant created_at) {
        return analystRepository.findAll().stream()
                .filter(x -> x.getCreatedAt().isBefore(created_at))
                .map(x -> analystMapper.toFullInfoDTO(x))
                .toList();
    }

    @Override
    public AnalystInfoDTO getAnalystById(int id) {
        return analystMapper.toFullInfoDTO(analystRepository.findById(id).get());
    }

    @Override
    public AnalystInfoDTO getAnalystByLogin(String login) {
        return analystMapper.toFullInfoDTO(analystRepository.getAnalystByLogin(login).get());
    }

    @Transactional
    @Override
    public AnalystInfoDTO updateAnalyst(int id,UpdateAnalystDTO updateAnalystDTO) {

        Analyst oldAnalyst = analystRepository.findById(id).get();

        List<AnalystUpdateCheck> updateChecks = Arrays.asList(
                new AnalystTokenUpdateCheck(tokenRepository),
                new AnalystLoginUpdateCheck(analystRepository),
                new AnalystPasswordUpdateCheck(passwordEncoder),
                new AnalystRoleUpdateCheck()
        );

        MainAnalystUpdateCheck analystUpdateCheck = new MainAnalystUpdateCheck(updateChecks);

        try{

            analystUpdateCheck.updateChecks(oldAnalyst, updateAnalystDTO);
            LOGGER.info("Analyst - {} was updated",oldAnalyst.getLogin());
        }
        catch (Exception e){
            throw new UpdateAnalystException();
        }

        return analystMapper.toFullInfoDTO(analystRepository.findById(id).get());
    }

    @Transactional
    @Override
    public void deleteAnalyst(int id) {
        String deletedLogin = analystRepository.findById(id).get().getLogin();
        analystRepository.deleteById(id);
        LOGGER.info("Analyst - {} was deleted",deletedLogin);
    }

    //Методы для фасада
    @Override
    public JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO) {

        Analyst certainAnalyst = analystRepository.getAnalystByLogin(authDTO.getLogin()).get();

        InviteToken analystToken = tokenRepository.findByToken(authDTO.getToken()).get();

        JwtAuthenticationDTO jwtDTO = new JwtAuthenticationDTO();
        jwtDTO.setToken("Error authentication");
        jwtDTO.setRefreshToken("Error refresh token");

        if(authDTO.getLogin() != null
                && authDTO.getPassword() != null
                && analystRepository.getAnalystByLogin(authDTO.getLogin()).isPresent()
                && certainAnalyst.getToken().equals(analystToken)){

            if(passwordEncoder.matches(authDTO.getPassword(),certainAnalyst.getPassword())){

                LOGGER.info("{} User {} successfully authenticated",certainAnalyst.getId(),certainAnalyst.getLogin());
                return  jwtService.getTokenForAnalyst(authDTO.getLogin());

            }
            else{
                LOGGER.error("Incorrect login , password or token");
            }

        }

        else{
            LOGGER.error("Analyst with login - {} not found",authDTO.getLogin());
        }

        return jwtDTO;
    }

    @Override
    public JwtTokenDTO getOut(String token) {

        try{

            Analyst exitAnalyst = analystRepository.getAnalystByLogin(jwtService.getLoginFromToken(token)).get();

            String login = exitAnalyst.getLogin();

            String deadToken = jwtService.getOutFromAccount(login).getToken();
            JwtTokenDTO deadTokenDTO = new JwtTokenDTO();
            deadTokenDTO.setToken(deadToken);
            return deadTokenDTO;

        }
        catch (Exception e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException();
        }

    }

    @Override
    public Analyst analystFromToken(String jwt) {

//        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
//        jwtTokenDTO.setToken(jwt);
        String login = jwtService.getLoginFromToken(jwt);

        return analystRepository.getAnalystByLogin(login).get();
    }

}