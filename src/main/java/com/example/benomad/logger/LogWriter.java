package com.example.benomad.logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogWriter {

    public static void get(String message){
        log.info(LogMarkers.READ.get(), message);
    }

    public static void insert(String message){
        log.info(LogMarkers.CREATE.get(), message);
    }

    public static void update(String message){
        log.info(LogMarkers.UPDATE.get(), message);
    }

    public static void delete(String message){
        log.info(LogMarkers.DELETE.get(), message);
    }

    public static void auth(String message){
        log.info(LogMarkers.AUTH.get(), message);
    }

    public static void other(String message){
        log.info(LogMarkers.OTHER.get(), message);
    }

//    public static class http{
//        public static void NOT_FOUND(String message){
//            log.info(LogMarkers.OTHER.get(), message);
//        }
//    }
}
