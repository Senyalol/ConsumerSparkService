package com.bankSpark.analyticsService.facade.segments;

import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;
import com.bankSpark.analyticsService.annotation.Facade;
import com.bankSpark.analyticsService.mapper.SegmentUMapper;
import com.bankSpark.analyticsService.service.segments.SegmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Facade
public class SegmentUFacadeImpl implements SegmentUFacade {

    private final SegmentService segmentService;
    private final SegmentUMapper segmentUMapper;

    @Autowired
    public SegmentUFacadeImpl(SegmentService segmentService, SegmentUMapper segmentUMapper) {
        this.segmentService = segmentService;
        this.segmentUMapper = segmentUMapper;
    }

    @Override
    public List<SegmentUserDTO> getAllSegments() {
        return segmentUMapper.toListDTO(segmentService.getAllSegments());
    }

    @Override
    public SegmentUserDTO getSegmentById(int id) {
        return segmentUMapper.toDTO(segmentService.getSegmentById(id));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByUser(int userId) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByUser(userId));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByUser(String lastName) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByUser(lastName));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByUser(String firstName, String lastName) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByUser(firstName, lastName));
    }

    @Override
    public List<SegmentUserDTO> getCertainSegments(String segment) {
        return segmentUMapper.toListDTO(segmentService.getCertainSegments(segment));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByRMore(Double r) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByRMore(r));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByRLess(Double r) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByRLess(r));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByRRange(Double min, Double max) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByRRange(min, max));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByFLess(Long f) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByFLess(f));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByFMore(Long f) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByFMore(f));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByFRange(Long min, Long max) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByFRange(min, max));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByMMore(Double m) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByMMore(m));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByMLess(Double m) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByMLess(m));
    }

    @Override
    public List<SegmentUserDTO> getSegmentsByMRange(Double min, Double max) {
        return segmentUMapper.toListDTO(segmentService.getSegmentsByMRange(min, max));
    }

}