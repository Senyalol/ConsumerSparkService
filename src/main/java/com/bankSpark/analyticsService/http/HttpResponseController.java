package com.bankSpark.analyticsService.http;

import com.bankSpark.analyticsService.ORM.segment.SEGMENTS;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class HttpResponseController {

    //Базовая проверка на пустые обьекты
    public static <T>ResponseEntity<T> build(T data) {

        if(data == null){
            return ResponseEntity.noContent().build();
        }
        if(data instanceof List && ((List<?>)data).isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(data);
    }

    //Проверка для Id
    public static <T>ResponseEntity<T> buildWithId(T data, int id) {

        if(!isIdValid(id)){
            return ResponseEntity.badRequest().build();
        }

        return build(data);

    }

    //Проверка для диапазонов
    public static <T>ResponseEntity<T> buildWithRange(T data,Double min, Double max) {

        if(!isValidRange(min, max)){
            return ResponseEntity.badRequest().build();
        }

        return build(data);

    }

    public static <T>ResponseEntity<T> buildWithRange(T data,Long min, Long max) {

        if(!isValidRange(min, max)){
            return ResponseEntity.badRequest().build();
        }

        return build(data);

    }

    //Проверки на положительные значения
    public static <T>ResponseEntity<T> buildWithPositiveValue(T data, Double value){
        if(!isPositive(value)){
            return ResponseEntity.badRequest().build();
        }
        return build(data);
    }

    public static <T>ResponseEntity<T> buildWithPositiveValue(T data,Long value){
        if(!isPositive(value)){
            return ResponseEntity.badRequest().build();
        }
        return build(data);
    }

    //Проверка строки
    public static <T>ResponseEntity<T> buildWithStringValue(T data, String value){

        if(!isValidString(value)){
            return ResponseEntity.badRequest().build();
        }
        return build(data);
    }

    //Проверка на 2 строки
    public static <T>ResponseEntity<T> buildWithStringValue(T data, String value1,String value2){

        if(!isValidString(value1) || !isValidString(value2)){
            return ResponseEntity.badRequest().build();
        }
        return build(data);
    }

    //Проверка существующего сегмента
    public static <T>ResponseEntity<T> buildWithExistSegment(T data, String segment){

        if(!isValidSegment(segment)){
            return ResponseEntity.badRequest().build();
        }

        return build(data);

    }

    //Нужно добавить проверку на аномалию

    private static boolean isIdValid(int id){
        return id > 0;
    }

    private static boolean isValidRange(Double min, Double max) {

        if (min == null || max == null) {
            return false;
        }
        if (min > max) {
            return false;
        }
        return true;
    }

    private static boolean isValidRange(Long min, Long max) {

        if(min == null || max == null) {
            return false;
        }
        if(min > max){
            return false;
        }

        return true;
    }

    private static boolean isPositive(Double value){
        return value != null && value >= 0;
    }

    private static boolean isPositive(Long value){
        return value != null && value >= 0;
    }

    private static boolean isValidString(String str){
        return str != null && !str.trim().isEmpty();
    }

    private static boolean isValidSegment(String segment) {
        if(!isValidString(segment)) return false;
        for(SEGMENTS s : SEGMENTS.values()) {
            if(s.name().equals(segment)) return true;
        }
        return false;
    }

}