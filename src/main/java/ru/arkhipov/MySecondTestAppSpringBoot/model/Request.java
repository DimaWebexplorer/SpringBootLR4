package ru.arkhipov.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Request {
    @NotBlank(message = "uid должен присутствовать")
    @Size(max = 32, message = "uid должен быть не длиннее 32 символов")
    private String uid;

    @NotBlank(message = "operationUid должен присутствовать")
    @Size(max = 32, message = "operationUid должен быть не длиннее 32 символов")
    private String operationUid;

    private Systems systemName;

    @NotBlank(message = "systemTime должен присутствовать")
    private String systemTime;
    private String source;

    @NotNull(message = "communicationId должен присутствовать")
    @Min(value = 1, message = "communicationId должен быть больше 0")
    @Max(value = 100000, message = "communicationId должен быть не больше 100000")
    private int communicationId;

    private int templateId;
    private int productCode;
    private int smsCode;

    @Override
    public String toString() {
        return "Request{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName='" + systemName + '\'' +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }
}
