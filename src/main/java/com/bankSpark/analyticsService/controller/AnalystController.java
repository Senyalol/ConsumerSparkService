package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.analyst.AnalystInfoDTO;
import com.bankSpark.analyticsService.DTO.analyst.AuthAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.CreateAnalystDTO;
import com.bankSpark.analyticsService.DTO.analyst.UpdateAnalystDTO;
import com.bankSpark.analyticsService.facade.analyst.AnalystFacade;
import com.bankSpark.analyticsService.http.HttpResponseController;
import com.bankSpark.analyticsService.security.sDTO.JwtAuthenticationDTO;
import com.bankSpark.analyticsService.security.sDTO.JwtTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analyst")
public class AnalystController {

    private final AnalystFacade analystFacade;
    
    @Autowired
    public AnalystController(AnalystFacade analystFacade) {
        this.analystFacade = analystFacade;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AnalystInfoDTO>> getAnalysts() {
        return HttpResponseController.build(analystFacade.getAnalysts());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/params/role")
    public ResponseEntity<List<AnalystInfoDTO>> getAnalystsByRole(@RequestParam String role) {
        return HttpResponseController.buildWithRoles(analystFacade.getAnalystsByRole(role),role);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/date/after")
    public ResponseEntity<List<AnalystInfoDTO>> getAnalystAfterDate(@RequestParam LocalDateTime afterDate) {
        return HttpResponseController.buildWithDate(analystFacade.getAnalystsAfterCreatedAt(afterDate),afterDate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/date/before")
    public ResponseEntity<List<AnalystInfoDTO>> getAnalystBeforeDate(@RequestParam LocalDateTime beforeDate) {
        return HttpResponseController.buildWithDate(analystFacade.getAnalystsBeforeCreatedAt(beforeDate),beforeDate);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/params/id")
    public ResponseEntity<AnalystInfoDTO> getAnalystById(@RequestParam int id) {
        return HttpResponseController.buildWithId(analystFacade.getAnalystById(id),id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/params/login")
    public ResponseEntity<AnalystInfoDTO> getAnalystByLogin(@RequestParam String login) {
        return HttpResponseController.buildWithStringValue(analystFacade.getAnalystByLogin(login),login);
    }

    @PostMapping
    public ResponseEntity<AnalystInfoDTO> addAnalyst(@RequestBody CreateAnalystDTO newAnalyst) {
        return HttpResponseController.build(analystFacade.createAnalyst(newAnalyst));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping
    public ResponseEntity<AnalystInfoDTO> patchAnalystById(@RequestParam int analystId, @RequestBody UpdateAnalystDTO updateAnalystDTO){
        return HttpResponseController.buildWithId(analystFacade.updateAnalyst(analystId,updateAnalystDTO),analystId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteAnalystById(@RequestParam int analystId) {
        analystFacade.deleteAnalyst(analystId);
        return HttpResponseController.buildWithId(analystId);
    }

    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    @PostMapping("/auth")
    public ResponseEntity<JwtAuthenticationDTO> signIn(@RequestBody AuthAnalystDTO authAnalystDTO){
        return HttpResponseController.build(analystFacade.signIn(authAnalystDTO));
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/exit")
    public ResponseEntity<JwtTokenDTO> exit(@RequestHeader("Authorization") String token){
        return HttpResponseController.buildWithStringValue(analystFacade.getOut(token),token);
    }

    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('ANALYST')")
    @GetMapping("/getFromJWT")
    public ResponseEntity<AnalystInfoDTO> getFromJWT(@RequestHeader("Authorization") String token){
        return HttpResponseController.buildWithStringValue(analystFacade.analystFromToken(token),token);
    }

}