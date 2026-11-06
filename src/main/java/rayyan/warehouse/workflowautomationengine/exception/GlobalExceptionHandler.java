package rayyan.warehouse.workflowautomationengine.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rayyan.warehouse.workflowautomationengine.dto.auth.ErrorResponseDTO;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // un registered user tries to sneak in
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedException(
            UnauthorizedException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

    }

    // duplicate email
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponseDTO> handleConflict(
            ConflictException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    // any other unknown exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnknownException(
            RuntimeException ex,
            HttpServletRequest request
    ){
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred, we are looking into it\n"+ex,
                request.getRequestURI(),
                null
        );
    }


    // response method
    public ResponseEntity<ErrorResponseDTO> buildResponse(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors
    ){
        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),// the complete message
                message,// the error field
                path,
                validationErrors
        );
        return ResponseEntity.status(status).body(response);
    }
}
