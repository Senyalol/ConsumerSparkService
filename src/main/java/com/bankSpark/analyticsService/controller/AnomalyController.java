package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.anomaly.AnomalyDTO;
import com.bankSpark.analyticsService.http.HttpResponseController;
import com.bankSpark.analyticsService.facade.anomaly.AnomalyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5174"})
@RestController
@RequestMapping("/api/anomaly")
public class AnomalyController {

    private final AnomalyFacade anomalyFacade;

    @Autowired
    public AnomalyController(AnomalyFacade anomalyFacade) {
        this.anomalyFacade = anomalyFacade;
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping
    public ResponseEntity<List<AnomalyDTO>> getAllAnomalies() {
        return HttpResponseController.build(anomalyFacade.getAllAnomalies());
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/id")
    public ResponseEntity<AnomalyDTO> getAnomalyById(@RequestParam int id) {
        return HttpResponseController.buildWithId(anomalyFacade.getAnomalyById(id),id);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/type")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByType(@RequestParam String type) {
        return HttpResponseController.buildWithStringValue(anomalyFacade.getAnomalyByType(type),type);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/sum/range")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesSumFrom(@RequestParam Double min , @RequestParam Double max) {
        return HttpResponseController.buildWithRange(anomalyFacade.getAnomaliesBySumRange(min, max),min,max);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/sum/more")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByMoreSum(@RequestParam Double sum) {
        return HttpResponseController.buildWithPositiveValue(anomalyFacade.getAnomaliesByMoreSum(sum),sum);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/sum/less")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByLessSum(@RequestParam Double sum) {
        return HttpResponseController.buildWithPositiveValue(anomalyFacade.getAnomaliesByLessSum(sum),sum);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/etime/range")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByEventTimeRange(@RequestParam Long min , @RequestParam Long max) {
        return HttpResponseController.buildWithRange(anomalyFacade.getAnomaliesByEventTimeRange(min , max),min,max);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/etime/more")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByMaxEventTime(@RequestParam Long max) {
        return HttpResponseController.buildWithPositiveValue(anomalyFacade.getAnomaliesByMaxEventTime(max),max);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/etime/less")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByMinEventTime(@RequestParam Long min) {
        return HttpResponseController.buildWithPositiveValue(anomalyFacade.getAnomaliesByMinEventTime(min),min);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/avg-check")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByCheck(@RequestParam Double min , @RequestParam(required = false) Double max) {

//        if(max == null){
//            return anomalyFacade.getAnomaliesByAvgCheck(min);
//        }
//
//        return anomalyFacade.getAnomaliesByAvgCheck(min,max);

        return max == null ? HttpResponseController.buildWithPositiveValue(anomalyFacade.getAnomaliesByAvgCheck(min),min) : HttpResponseController.buildWithRange(anomalyFacade.getAnomaliesByAvgCheck(min,max),min,max);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/user")
    public ResponseEntity<List<AnomalyDTO>> getAnomaliesByUser(@RequestParam int userId) {
        return HttpResponseController.buildWithId(anomalyFacade.getAnomaliesByUserId(userId),userId);
    }

}