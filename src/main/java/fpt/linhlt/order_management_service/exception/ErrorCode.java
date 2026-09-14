package fpt.linhlt.order_management_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_EXISTED(1001, "Email existed", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_INVALID(1003, "password must at least 5 characters", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1004, "unauthenticated", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
