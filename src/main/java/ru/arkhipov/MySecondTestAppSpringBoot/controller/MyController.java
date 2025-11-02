package ru.arkhipov.MySecondTestAppSpringBoot.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.arkhipov.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.arkhipov.MySecondTestAppSpringBoot.model.*;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.arkhipov.MySecondTestAppSpringBoot.service.ValidationService;
import ru.arkhipov.MySecondTestAppSpringBoot.util.DateTimeUtil;

import java.util.Date;

@Slf4j
@RestController

public class MyController {
    private final ValidationService validationService;

    private final ModifyResponseService modifyOperationUidResponseService;
    private final ModifyResponseService modifySystemTimeResponseService;

    @Autowired
    public MyController(ValidationService validationService, @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifySystemTimeResponseService,
                        @Qualifier("ModifyOperationUidResponseService") ModifyResponseService modifyOperationUidResponseService) {
        this.validationService = validationService;
        this.modifySystemTimeResponseService = modifySystemTimeResponseService;
        this.modifyOperationUidResponseService = modifyOperationUidResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
        long currentTime = System.currentTimeMillis();
        Long receivedTime = request.getReceivedTime();
        log.info("request: {}", request);

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        if (receivedTime != null) {
            long timeDifference = currentTime - receivedTime;

            log.info("========================================");
            log.info("СЕРВИС 2: Получен модифицированный Request");
            log.info("========================================");
            log.info("Request UID: {}", request.getUid());
            log.info("Operation UID: {}", request.getOperationUid());
            log.info("Source: {}", request.getSource());
            log.info("System Name: {}", request.getSystemName());
            log.info("----------------------------------------");
            log.info("Время получения Сервисом 1: {} мс", receivedTime);
            log.info("Текущее время в Сервисе 2: {} мс", currentTime);
            log.info("========================================");
            log.info("⏱️  РАЗНИЦА ВРЕМЕНИ: {} миллисекунд", timeDifference);
            log.info("⏱️  РАЗНИЦА ВРЕМЕНИ: {} секунд", timeDifference / 1000.0);
            log.info("========================================");
        } else {
            log.warn("СЕРВИС 2: receivedTime не установлен в запросе");
        }

        try {
            validationService.isValid(bindingResult);
            validationService.isUidValid(request.getUid());
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_CODE_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED_CODE);
            log.error("Unsupported code: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);
            log.error("Unknown error: {}", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifySystemTimeResponseService.modify(response);
        modifyOperationUidResponseService.modify(response);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
