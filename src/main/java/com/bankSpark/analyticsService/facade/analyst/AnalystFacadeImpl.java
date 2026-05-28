package com.bankSpark.analyticsService.facade.analyst;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.annotation.Facade;
import com.bankSpark.analyticsService.mapper.AnalystMapper;
import com.bankSpark.analyticsService.security.analystService.AnalystService;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Facade
public class AnalystFacadeImpl implements AnalystFacade {

    private final AnalystService analystService;
    private final AnalystMapper analystMapper;

    @Autowired
    public AnalystFacadeImpl(AnalystService analystService,AnalystMapper analystMapper) {
        this.analystService = analystService;
        this.analystMapper = analystMapper;
    }

    @Override
    public int getIdByLogin(String login) {
        return analystService.getIdByLogin(login);
    }

    @Override
    public List<AnalystInfoDTO> getAnalysts() {
        return analystService.getAnalysts();
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsByRole(String role) {
        return analystService.getAnalystsByRole(role);
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsAfterCreatedAt(LocalDateTime created_at) {
        Instant createdInstant = created_at.atZone(ZoneId.systemDefault()).toInstant();
        return analystService.getAnalystsAfterCreatedAt(createdInstant);
    }

    @Override
    public List<AnalystInfoDTO> getAnalystsBeforeCreatedAt(LocalDateTime created_at) {
        Instant createdInstant = created_at.atZone(ZoneId.systemDefault()).toInstant();
        return analystService.getAnalystsBeforeCreatedAt(createdInstant);
    }

    @Override
    public AnalystInfoDTO getAnalystById(int id) {
        return analystService.getAnalystById(id);
    }

    @Override
    public AnalystInfoDTO getAnalystByLogin(String login) {
        return analystService.getAnalystByLogin(login);
    }

    @Override
    public AnalystInfoDTO createAnalyst(CreateAnalystDTO createAnalystDTO) {
        return analystService.createAnalyst(createAnalystDTO);
    }

    @Override
    public AnalystInfoDTO updateAnalyst(int id, UpdateAnalystDTO updateAnalystDTO) {
        return analystService.updateAnalyst(id, updateAnalystDTO);
    }

    @Override
    public void deleteAnalyst(int id) {
        analystService.deleteAnalyst(id);
    }

    @Override
    public JwtAuthenticationDTO signIn(AuthAnalystDTO authDTO) {
        return analystService.signIn(authDTO);
    }

    @Override
    public JwtTokenDTO getOut(String token) {

        String trueToken = getPayLoadToken(token);

        return analystService.getOut(trueToken);
    }

    @Override
    public AnalystInfoDTO analystFromToken(String jwt) {

        String trueToken = getPayLoadToken(jwt);

        return analystMapper.toFullInfoDTO(analystService.analystFromToken(trueToken));
    }

    //Получить часть токена с полезной информацией
    private String getPayLoadToken(String token){

        String payLoadData = token;

        if(payLoadData != null && payLoadData.startsWith("Bearer ")) {
            payLoadData = payLoadData.substring(7).trim();
        }

        return payLoadData;
    }

}