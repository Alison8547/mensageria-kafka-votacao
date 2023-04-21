package com.api.kafkavotacao.application.controller;

import com.api.kafkavotacao.domain.model.ResultPayload;
import com.api.kafkavotacao.domain.service.ResultProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResultControllerImpl implements ResultController {

    private final ResultProducerService resultProducerService;

    @Override
    public ResponseEntity<?> send(List<ResultPayload> resultPayloadList) {
        for (ResultPayload resultPayload : resultPayloadList) {
            resultProducerService.producerResult(resultPayload);
        }

        return ResponseEntity.ok("Registros enviados!");

    }
}
