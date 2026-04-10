package com.bankSpark.analyticsService.service.segments;

import com.bankSpark.analyticsService.ORM.Segmentuser;

import java.util.List;

public interface SegmentService {

    List<Segmentuser> getAllSegments();

    Segmentuser getSegmentById(int id);

    List<Segmentuser> getSegmentsByUser(int userId); //Прегрузка

    List<Segmentuser> getSegmentsByUser(String lastName);

    List<Segmentuser> getSegmentsByUser(String firstName, String lastName);

    List<Segmentuser> getCertainSegments(String segment);

    //Касаемо этих 3-х параметров - добавить больше функционала - больше, меньше, Промежуток

    List<Segmentuser> getSegmentsByR(Double r);

    List<Segmentuser> getSegmentsByF(Long f);

    List<Segmentuser> getSegmentsByM(Double m);

    //Методы для получения самых новых/старых данных
    //Зачем поле updated_at - если у меня есть окно в 5 минут и по сути данные обновляются одновременно

}