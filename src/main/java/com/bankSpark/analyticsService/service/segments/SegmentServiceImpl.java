package com.bankSpark.analyticsService.service.segments;

import com.bankSpark.analyticsService.ORM.Segmentuser;
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
    public List<Segmentuser> getAllSegments() {
        return segmentURepository.findAll();
    }

    @Override
    public Segmentuser getSegmentById(int id) {
        return segmentURepository.findById(id).get();
    }

    @Override
    public List<Segmentuser> getSegmentsByUser(int userId) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByUser(String lastName) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getUser().getLastname().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByUser(String firstName, String lastName) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getUser().getFirstname().equals(firstName) && x.getUser().getLastname().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getCertainSegments(String segment) {
        return segmentURepository.findBySegment(segment);
    }


    @Override
    public List<Segmentuser> getSegmentsByRLess(Double r) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getRMinutes() < r)
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByRRange(Double min, Double max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getRMinutes() >= min && x.getRMinutes() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByFLess(Long f) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getF() <= f)
                .collect(Collectors.toList());
    }


    @Override
    public List<Segmentuser> getSegmentsByFRange(Long min, Long max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getF() >= min && x.getF() <= max)
                .collect(Collectors.toList());
    }


    @Override
    public List<Segmentuser> getSegmentsByMLess(Double m) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getM() <= m)
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByMRange(Double min, Double max) {
        return segmentURepository.findAll().stream()
                .filter(x -> x.getM() >= min && x.getM() <= max)
                .collect(Collectors.toList());
    }

    //Вопросик по поводу Equals
    @Override
    public List<Segmentuser> getSegmentsByRMore(Double r) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getRMinutes() >= r)
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByFMore(Long f) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getF() >= f)
                .collect(Collectors.toList());
    }

    @Override
    public List<Segmentuser> getSegmentsByMMore(Double m) {
        return segmentURepository.findAll()
                .stream()
                .filter(x -> x.getM() >= m)
                .collect(Collectors.toList());
    }

}