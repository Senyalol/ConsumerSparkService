package com.bankSpark.analyticsService.controller;

import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.facade.segments.SegmentUFacade;
import com.bankSpark.analyticsService.http.HttpResponseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Cors
//ResponseEntity

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:5174"})
@RestController
@RequestMapping("/api/segments")
public class SegmentUController {

    private final SegmentUFacade segmentUFacade;

    @Autowired
    public SegmentUController(SegmentUFacade segmentUFacade) {
        this.segmentUFacade = segmentUFacade;
    }

    @GetMapping
    public ResponseEntity<List<SegmentUserDTO>> getAllSegments() {
        return HttpResponseController.build(segmentUFacade.getAllSegments());
    }

    @GetMapping("/id")
    public ResponseEntity<SegmentUserDTO> getSegmentById(@RequestParam int id) {
        return HttpResponseController.buildWithId(segmentUFacade.getSegmentById(id),id);
    }

    @GetMapping("/user")
    public ResponseEntity<List<SegmentUserDTO>> getSegmentsByUserId(@RequestParam int userId) {
        return HttpResponseController.buildWithId(segmentUFacade.getSegmentsByUser(userId),userId);
    }

    //Проверить , написать тесты
    @GetMapping("/username")
    public ResponseEntity<List<SegmentUserDTO>> getSegmentsByUsername(@RequestParam String lastname, @RequestParam(required = false) String name) {

        if(name == null){

            if(segmentUFacade.getSegmentsByUser(lastname).isEmpty()){
                return ResponseEntity.noContent().build();
            }

            return HttpResponseController.buildWithStringValue(segmentUFacade.getSegmentsByUser(lastname),lastname);
        }


        if(segmentUFacade.getSegmentsByUser(name, lastname).isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithStringValue(segmentUFacade.getSegmentsByUser(name, lastname),lastname,name);
    }

    @GetMapping("/type")
    public ResponseEntity<List<SegmentUserDTO>> getSegmentsBySegmentId(@RequestParam String segment) {

        if(segmentUFacade.getCertainSegments(segment).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithExistSegment(segmentUFacade.getCertainSegments(segment),segment);
    }

    //Названия маппингов
    @GetMapping("/R/more")
    public ResponseEntity<List<SegmentUserDTO>> getRMoreSegments(@RequestParam Double R) {

        if(segmentUFacade.getSegmentsByRMore(R).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByRMore(R),R);
    }

    @GetMapping("/R/less")
    public ResponseEntity<List<SegmentUserDTO>> getRLessSegments(@RequestParam Double R) {

        if(segmentUFacade.getSegmentsByRLess(R).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByRLess(R),R);
    }

    @GetMapping("/R/range")
    public ResponseEntity<List<SegmentUserDTO>> getRRangeSegments(@RequestParam Double min, @RequestParam Double max) {

        if(segmentUFacade.getSegmentsByRRange(min, max).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithRange(segmentUFacade.getSegmentsByRRange(min, max),min,max);
    }

    @GetMapping("/F/more")
    public ResponseEntity<List<SegmentUserDTO>> getFMoreSegments(@RequestParam Long F) {

        if(segmentUFacade.getSegmentsByFMore(F).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByFMore(F),F);
    }

    @GetMapping("/F/less")
    public ResponseEntity<List<SegmentUserDTO>> getFLessSegments(@RequestParam Long F) {

        if(segmentUFacade.getSegmentsByFLess(F).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByFLess(F),F);
    }

    @GetMapping("/F/range")
    public ResponseEntity<List<SegmentUserDTO>> getFRangeSegments(@RequestParam Long min, @RequestParam Long max) {

        if(segmentUFacade.getSegmentsByFRange(min, max).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithRange(segmentUFacade.getSegmentsByFRange(min, max),min,max);
    }

    @GetMapping("/M/more")
    public ResponseEntity<List<SegmentUserDTO>> getMMoreSegments(@RequestParam Double M) {

        if(segmentUFacade.getSegmentsByMMore(M).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByMMore(M),M);
    }

    @GetMapping("/M/less")
    public ResponseEntity<List<SegmentUserDTO>> getMLessSegments(@RequestParam Double M) {

        if(segmentUFacade.getSegmentsByMLess(M).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithPositiveValue(segmentUFacade.getSegmentsByMLess(M),M);
    }

    @GetMapping("/M/range")
    public ResponseEntity<List<SegmentUserDTO>> getMRangeSegments(@RequestParam Double min, @RequestParam Double max) {

        if(segmentUFacade.getSegmentsByMRange(min, max).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return HttpResponseController.buildWithRange(segmentUFacade.getSegmentsByMRange(min, max),min,max);
    }

}