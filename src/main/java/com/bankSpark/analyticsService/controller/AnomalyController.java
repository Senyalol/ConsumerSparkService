package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.AnomalyDTO;
import com.bankSpark.analyticsService.facade.anomaly.AnomalyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anomaly")
public class AnomalyController {

    private final AnomalyFacade anomalyFacade;

    @Autowired
    public AnomalyController(AnomalyFacade anomalyFacade) {
        this.anomalyFacade = anomalyFacade;
    }

    @GetMapping
    public List<AnomalyDTO> getAllAnomalies() {
        return anomalyFacade.getAllAnomalies();
    }

    @GetMapping("/{id}")
    public AnomalyDTO getAnomalyById(@PathVariable int id) {
        return anomalyFacade.getAnomalyById(id);
    }

    @GetMapping("/type/{type}")
    public List<AnomalyDTO> getAnomaliesByType(@PathVariable String type) {
        return anomalyFacade.getAnomalyByType(type);
    }

    @GetMapping("/sum/range")
    public List<AnomalyDTO> getAnomaliesSumFrom(@RequestParam Double min , @RequestParam Double max) {
        return anomalyFacade.getAnomaliesBySumRange(min, max);
    }

    @GetMapping("/sum/more")
    public List<AnomalyDTO> getAnomaliesByMoreSum(@RequestParam Double sum) {
        return anomalyFacade.getAnomaliesByMoreSum(sum);
    }

    @GetMapping("/sum/less")
    public List<AnomalyDTO> getAnomaliesByLessSum(@RequestParam Double sum) {
        return anomalyFacade.getAnomaliesByLessSum(sum);
    }

    @GetMapping("/etime/range")
    public List<AnomalyDTO> getAnomaliesByEventTimeRange(@RequestParam Long min , @RequestParam Long max) {
        return anomalyFacade.getAnomaliesByEventTimeRange(min , max);
    }

    @GetMapping("/etime/more")
    public List<AnomalyDTO> getAnomaliesByMaxEventTime(@RequestParam Long max) {
        return anomalyFacade.getAnomaliesByMaxEventTime(max);
    }

    @GetMapping("/etime/less")
    public List<AnomalyDTO> getAnomaliesByMinEventTime(@RequestParam Long min) {
        return anomalyFacade.getAnomaliesByMinEventTime(min);
    }

    @GetMapping("/check")
    public List<AnomalyDTO> getAnomaliesByCheck(@RequestParam Double min , @RequestParam(required = false) Double max) {

        if(max == null){
            return anomalyFacade.getAnomaliesByAvgCheck(min);
        }

        return anomalyFacade.getAnomaliesByAvgCheck(min,max);

    }

    @GetMapping("/user")
    public List<AnomalyDTO> getAnomaliesByUser(@RequestParam int userId) {
        return anomalyFacade.getAnomaliesByUserId(userId);
    }

}