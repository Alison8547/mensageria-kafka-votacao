package com.api.kafkavotacao.domain.service;

import com.api.kafkavotacao.domain.model.ResultPayload;

public interface ResultProducerService {

    void producerResult(ResultPayload message);
}
