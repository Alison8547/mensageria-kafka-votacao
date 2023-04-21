package com.api.kafkavotacao.domain.service;

import com.api.kafkavotacao.domain.model.ResultPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultProducerServiceImpl implements ResultProducerService {

    private final KafkaTemplate<String, ResultPayload> kafkaTemplate;

    @Override
    public void producerResult(ResultPayload message) {
        ListenableFuture<SendResult<String, ResultPayload>> future = kafkaTemplate.sendDefault(UUID.randomUUID().toString(), message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Kafka unable to send message='{}`, reason: {}", message, ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, ResultPayload> result) {

            }
        });

    }
}
