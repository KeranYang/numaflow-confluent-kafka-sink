package io.numaproj.confluent.kafka_sink;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import io.numaproj.confluent.kafka_sink.config.KafkaSinkerConfig;
import io.numaproj.confluent.kafka_sink.sinker.KafkaSinker;
import io.numaproj.numaflow.sinker.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Objects;

@Configuration
@Slf4j
@ComponentScan(basePackages = "io.numaproj.confluent.kafka_sink")
public class KafkaSinkApplicationConfig {

	private final Environment environment;

	public KafkaSinkApplicationConfig(Environment environment) {
		this.environment = environment;
	}

	@Bean
	public Server sinkServer(KafkaSinker kafkaSinker) {
		return new Server(kafkaSinker);
	}
	@Bean
	public KafkaSinkerConfig appConfig() {
		return new KafkaSinkerConfig("kafka-sink-topic");
	}
}
