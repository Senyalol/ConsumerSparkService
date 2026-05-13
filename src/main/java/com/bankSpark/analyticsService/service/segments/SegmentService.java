package com.bankSpark.analyticsService.service.segments;

import com.bankSpark.analyticsService.ORM.segment.SegmentUser;

import java.util.List;

public interface SegmentService {

    List<SegmentUser> getAllSegments();

    SegmentUser getSegmentById(int id);

    List<SegmentUser> getSegmentsByUser(int userId); //Прегрузка

    List<SegmentUser> getSegmentsByUser(String lastName);

    List<SegmentUser> getSegmentsByUser(String firstName, String lastName);

    //Отсрортировать по фиксированному сегменту (словарь проверка)

    List<SegmentUser> getCertainSegments(String segment);

    //Главные метрики

    //R
    List<SegmentUser> getSegmentsByRMore(Double r);

    List<SegmentUser> getSegmentsByRLess(Double r);

    List<SegmentUser> getSegmentsByRRange(Double min, Double max);

    //F

    List<SegmentUser> getSegmentsByFLess(Long f);

    List<SegmentUser> getSegmentsByFMore(Long f);

    List<SegmentUser> getSegmentsByFRange(Long min, Long max);

    //M

    List<SegmentUser> getSegmentsByMMore(Double m);

    List<SegmentUser> getSegmentsByMLess(Double m);

    List<SegmentUser> getSegmentsByMRange(Double min, Double max);

    //Методы для получения самых новых/старых данных
    //Зачем поле updated_at - если у меня есть окно в 5 минут и по сути данные обновляются одновременно

}