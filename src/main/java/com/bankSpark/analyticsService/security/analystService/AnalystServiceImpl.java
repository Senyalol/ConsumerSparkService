package com.bankSpark.analyticsService.security.analystService;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.ORM.analyst.Analyst;
import com.bankSpark.analyticsService.exception.CreateAnalystException;
import com.bankSpark.analyticsService.exception.UpdateAnalystException;
import com.bankSpark.analyticsService.mapper.AnalystMapper;
import com.bankSpark.analyticsService.repository.AnalystRepository;
import com.bankSpark.analyticsService.repository.InviteTokenRepository;
import com.bankSpark.analyticsService.security.analystService.createChecks.*;
import com.bankSpark.analyticsService.security.analystService.updateChecks.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@Service
public class AnalystServiceImpl implements AnalystService {

    private final AnalystRepository analystRepository;
    private final AnalystMapper analystMapper;
    private final PasswordEncoder passwordEncoder;

    private final InviteTokenRepository tokenRepository;

    @Autowired
    public AnalystServiceImpl(AnalystRepository analystRepository, AnalystMapper analystMapper, InviteTokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.analystRepository = analystRepository;
        this.analystMapper = analystMapper;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
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
            analystRepository.save(newAnalyst);

            return analystMapper.toFullInfoDTO(analystRepository.getAnalystByLogin(createAnalystDTO.getLogin()).get());
        }

        throw new CreateAnalystException();
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

        }
        catch (Exception e){
            throw new UpdateAnalystException();
        }

        return analystMapper.toFullInfoDTO(analystRepository.findById(id).get());
    }

    @Transactional
    @Override
    public void deleteAnalyst(int id) {
        analystRepository.deleteById(id);
    }

}