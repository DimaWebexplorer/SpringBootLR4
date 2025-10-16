package ru.arkhipov.MySecondTestAppSpringBoot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.arkhipov.MySecondTestAppSpringBoot.model.Response;

import java.util.UUID;

@Slf4j
@Service
@Qualifier("ModifyOperationUidResponseService")
public class ModifyOperationUidResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        log.info("OperationUid before modification: {}", response.getOperationUid());
        UUID uuid = UUID.randomUUID();
        response.setOperationUid(uuid.toString());
        log.info("OperationUid after modification: {}", response.getOperationUid());
        return response;
    }
}
