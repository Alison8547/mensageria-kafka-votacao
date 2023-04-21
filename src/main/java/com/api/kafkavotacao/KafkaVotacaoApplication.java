package com.api.kafkavotacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class KafkaVotacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaVotacaoApplication.class, args);
	}

}
