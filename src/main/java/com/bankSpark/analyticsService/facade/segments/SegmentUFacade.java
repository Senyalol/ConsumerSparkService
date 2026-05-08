package com.bankSpark.analyticsService.facade.segments;

import com.bankSpark.analyticsService.DTO.segmentsRFM.SegmentUserDTO;

import java.util.List;

public interface SegmentUFacade {

    List<SegmentUserDTO> getAllSegments();

    SegmentUserDTO getSegmentById(int id);

    List<SegmentUserDTO> getSegmentsByUser(int userId); //Прегрузка

    List<SegmentUserDTO> getSegmentsByUser(String lastName);

    List<SegmentUserDTO> getSegmentsByUser(String firstName, String lastName);

    //Отсрортировать по фиксированному сегменту (словарь проверка)

    List<SegmentUserDTO> getCertainSegments(String segment);

    //Главные метрики

    //R
    List<SegmentUserDTO> getSegmentsByRMore(Double r);

    List<SegmentUserDTO> getSegmentsByRLess(Double r);

    List<SegmentUserDTO> getSegmentsByRRange(Double min, Double max);

    //F

    List<SegmentUserDTO> getSegmentsByFLess(Long f);

    List<SegmentUserDTO> getSegmentsByFMore(Long f);

    List<SegmentUserDTO> getSegmentsByFRange(Long min, Long max);

    //M

    List<SegmentUserDTO> getSegmentsByMMore(Double m);

    List<SegmentUserDTO> getSegmentsByMLess(Double m);

    List<SegmentUserDTO> getSegmentsByMRange(Double min, Double max);

}