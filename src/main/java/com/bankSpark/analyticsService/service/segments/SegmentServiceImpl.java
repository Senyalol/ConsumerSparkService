package com.bankSpark.analyticsService.service.segments;

import com.bankSpark.analyticsService.ORM.SegmentUser;
import com.bankSpark.analyticsService.repository.SegmentURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@JsonSerialize
@Service
public class SegmentServiceImpl implements SegmentService {

    private final SegmentURepository segmentURepository;

    @Autowired
    public SegmentServiceImpl(SegmentURepository segmentURepository) {
        this.segmentURepository = segmentURepository;
    }

    @Override
    public List<SegmentUser> getAllSegments() {
        return segmentURepository.findAll();
    }

    @Override
    public SegmentUser getSegmentById(int id) {
        return segmentURepository.findById(id).get();
    }

    @Override
    public List<SegmentUser> getSegmentsByUser(int userId) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByUser(String lastName) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getUser().getLastname().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByUser(String firstName, String lastName) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getUser().getFirstname().equals(firstName) && x.getUser().getLastname().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getCertainSegments(String segment) {
        return segmentURepository.findBySegment(segment);
    }


    @Override
    public List<SegmentUser> getSegmentsByRLess(Double r) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getRMinutes() < r)
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByRRange(Double min, Double max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getRMinutes() >= min && x.getRMinutes() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByFLess(Long f) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getF() <= f)
                .collect(Collectors.toList());
    }


    @Override
    public List<SegmentUser> getSegmentsByFRange(Long min, Long max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getF() >= min && x.getF() <= max)
                .collect(Collectors.toList());
    }


    @Override
    public List<SegmentUser> getSegmentsByMLess(Double m) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getM() <= m)
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByMRange(Double min, Double max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getM() >= min && x.getM() <= max)
                .collect(Collectors.toList());
    }

    //Вопросик по поводу Equals
    @Override
    public List<SegmentUser> getSegmentsByRMore(Double r) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getRMinutes() >= r)
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByFMore(Long f) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getF() >= f)
                .collect(Collectors.toList());
    }

    @Override
    public List<SegmentUser> getSegmentsByMMore(Double m) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getM() >= m)
                .collect(Collectors.toList());
    }

}