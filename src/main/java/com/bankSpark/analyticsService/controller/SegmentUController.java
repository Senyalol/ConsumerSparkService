package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.facade.segments.SegmentUFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Cors
//ResponseEntity

@RestController
@RequestMapping("/api/segments")
public class SegmentUController {

    private final SegmentUFacade segmentUFacade;

    @Autowired
    public SegmentUController(SegmentUFacade segmentUFacade) {
        this.segmentUFacade = segmentUFacade;
    }

    @GetMapping
    public List<SegmentUserDTO> getAllSegments() {
        return segmentUFacade.getAllSegments();
    }

    @GetMapping("/id")
    public SegmentUserDTO getSegmentById(@RequestParam int id) {
        return segmentUFacade.getSegmentById(id);
    }

    @GetMapping("/user")
    public List<SegmentUserDTO> getSegmentsByUserId(@RequestParam int userId) {
        return segmentUFacade.getSegmentsByUser(userId);
    }

    //Проверить , написать тесты
    @GetMapping("/username")
    public List<SegmentUserDTO> getSegmentsByUsername(@RequestParam String lastname, @RequestParam(required = false) String name) {

        if(name == null){
            return segmentUFacade.getSegmentsByUser(lastname);
        }

        return segmentUFacade.getSegmentsByUser(name, lastname);
    }

    @GetMapping("/type")
    public List<SegmentUserDTO> getSegmentsBySegmentId(@RequestParam String segment) {
        return segmentUFacade.getCertainSegments(segment);
    }

    //Названия маппингов
    @GetMapping("/R/more")
    public List<SegmentUserDTO> getRMoreSegments(@RequestParam Double R) {
        return segmentUFacade.getSegmentsByRMore(R);
    }

    @GetMapping("/R/less")
    public List<SegmentUserDTO> getRLessSegments(@RequestParam Double R) {
        return segmentUFacade.getSegmentsByRLess(R);
    }

    @GetMapping("/R/range")
    public List<SegmentUserDTO> getRRangeSegments(@RequestParam Double min, @RequestParam Double max) {
        return segmentUFacade.getSegmentsByRRange(min, max);
    }

    @GetMapping("/F/more")
    public List<SegmentUserDTO> getFMoreSegments(@RequestParam Long F) {
        return segmentUFacade.getSegmentsByFMore(F);
    }

    @GetMapping("/F/less")
    public List<SegmentUserDTO> getFLessSegments(@RequestParam Long F) {
        return segmentUFacade.getSegmentsByFLess(F);
    }

    @GetMapping("/F/range")
    public List<SegmentUserDTO> getFRangeSegments(@RequestParam Long min, @RequestParam Long max) {
        return segmentUFacade.getSegmentsByFRange(min, max);
    }

    @GetMapping("/M/more")
    public List<SegmentUserDTO> getMMoreSegments(@RequestParam Double M) {
        return segmentUFacade.getSegmentsByMMore(M);
    }

    @GetMapping("/M/less")
    public List<SegmentUserDTO> getMLessSegments(@RequestParam Double M) {
        return segmentUFacade.getSegmentsByMLess(M);
    }

    @GetMapping("/M/range")
    public List<SegmentUserDTO> getMRangeSegments(@RequestParam Double min, @RequestParam Double max) {
        return segmentUFacade.getSegmentsByMRange(min, max);
    }

}