package com.example.benomad.logger;

import com.example.benomad.exception.LogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
@Service
public class LogWriterServiceImpl {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private LogRepository logRepository;

    public void get(String message){
        log.info(LogMarkers.READ.get(), message);
        saveLogToDB(LogMarkers.READ.toString(), message);
    }

    public void insert(String message){
        log.info(LogMarkers.CREATE.get(), message);
        saveLogToDB(LogMarkers.CREATE.toString(), message);
    }

    public void update(String message){
        log.info(LogMarkers.UPDATE.get(), message);
        saveLogToDB(LogMarkers.UPDATE.toString(), message);
    }

    public void delete(String message){
        log.info(LogMarkers.DELETE.get(), message);
        saveLogToDB(LogMarkers.DELETE.toString(), message);
    }

    public void auth(String message){
        log.info(LogMarkers.AUTH.get(), message);
        saveLogToDB(LogMarkers.AUTH.toString(), message);
    }

    public void other(String message){
        log.info(LogMarkers.OTHER.get(), message);
        saveLogToDB(LogMarkers.OTHER.toString(), message);
    }

    private void saveLogToDB(String marker, String message){
        LogEntity logEntity = logRepository.findById(1L).orElse(new LogEntity());

        String logFormat = "%s%s --- %s : %s\n";

        String time = formatter.format(LocalDateTime.now(ZoneId.of("Asia/Bishkek")));

        String log = logEntity.getBody();

        logEntity.setBody(String.format(logFormat, log != null ? log : "", time, marker, message));

        logRepository.save(logEntity);
    }

    public LogDTO getLog(){
        return formatLog(logRepository.findById(1L).orElseThrow(LogException::new));
    }

    private LogDTO formatLog(LogEntity logEntity){
        return LogDTO.builder()
                .body(
                        logEntity.getBody().replaceAll("\n", "  |||  ")
                )
                .build();
    }
}
