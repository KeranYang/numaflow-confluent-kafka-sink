package io.numaproj.confluent.kafka_sink;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class KafkaSinkApplication {

	public static void main(String[] args) {
		log.info("Starting Confluent Kafka sink application...");
		SpringApplication.run(KafkaSinkApplication.class, args);
	}

}
