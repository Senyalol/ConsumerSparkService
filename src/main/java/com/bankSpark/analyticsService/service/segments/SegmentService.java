package com.bankSpark.analyticsService.service.segments;

import com.bankSpark.analyticsService.ORM.Segmentuser;

import java.util.List;

public interface SegmentService {

    List<Segmentuser> getAllSegments();

    Segmentuser getSegmentById(int id);

    List<Segmentuser> getSegmentsByUser(int userId); //Прегрузка

    List<Segmentuser> getSegmentsByUser(String lastName);

    List<Segmentuser> getSegmentsByUser(String firstName, String lastName);

    //Отсрортировать по фиксированному сегменту (словарь проверка)

    List<Segmentuser> getCertainSegments(String segment);

    //Главные метрики

    //R
    List<Segmentuser> getSegmentsByRMore(Double r);

    List<Segmentuser> getSegmentsByRLess(Double r);

    List<Segmentuser> getSegmentsByRRange(Double min, Double max);

    //F

    List<Segmentuser> getSegmentsByFLess(Long f);

    List<Segmentuser> getSegmentsByFMore(Long f);

    List<Segmentuser> getSegmentsByFRange(Long min, Long max);

    //M

    List<Segmentuser> getSegmentsByMMore(Double m);

    List<Segmentuser> getSegmentsByMLess(Double m);

    List<Segmentuser> getSegmentsByMRange(Double min, Double max);

    //Методы для получения самых новых/старых данных
    //Зачем поле updated_at - если у меня есть окно в 5 минут и по сути данные обновляются одновременно

}