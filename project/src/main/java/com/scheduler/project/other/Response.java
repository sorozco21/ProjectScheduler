package com.scheduler.project.other;

import com.scheduler.project.exception.ProjectSchedulingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private String message;
    private HttpStatus status;
    private String timestamp;
    private T resultData;
    private static final DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public Response(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus;
        this.timestamp = ZonedDateTime.now().format(dt);
    }
    public Response(String message, HttpStatus httpStatus, T resultData) {
        this.message = message;
        this.status = httpStatus;
        this.timestamp = ZonedDateTime.now().format(dt);
        this.resultData = resultData;
    }

    @Builder
    public static <T> Response<T> success(String message, HttpStatus httpStatus, T resultData) {
        return new Response<>(message, httpStatus, resultData);
    }

    @Builder
    public static <T> Response<T> failed(ProjectSchedulingException error, HttpStatus httpStatus, T resultData) {
        return new Response<>(error.getMessage(), httpStatus, resultData);
    }

    public static <T> Response<T> ok(T resultData) {
        return success("SUCCESS", HttpStatus.OK, resultData);
    }
}
