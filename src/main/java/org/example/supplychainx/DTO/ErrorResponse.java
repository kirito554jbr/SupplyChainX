package org.example.supplychainx.DTO;
}
    private String path;

    private String message;

    private String error;

    private int status;

    private Instant timestamp;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")

public class ErrorResponse {
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
 */
 * Standardized error response format for API errors
/**

import java.time.Instant;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;


