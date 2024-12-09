package io.numaproj.confluent.kafka_sink.sinker;

import io.numaproj.confluent.kafka_sink.config.KafkaSinkerConfig;
import io.numaproj.numaflow.sinker.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class KafkaSinker extends Sinker implements DisposableBean {
    private final String topicName;

    @Autowired
    public KafkaSinker(KafkaSinkerConfig config) {
        this.topicName = config.getTopicName();
    }

    /*
    @PostConstruct
    public void sinkServer() throws Exception {
        log.info("initialize kafka sink, topic name is - {}", this.topicName);
        Server server = new Server(this);
        server.start();
    }
     */

    @Override
    public ResponseList processMessages(DatumIterator datumIterator) {
        ResponseList.ResponseListBuilder responseListBuilder = ResponseList.newBuilder();
        while (true) {
            Datum datum;
            try {
                datum = datumIterator.next();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                continue;
            }
            // null means the iterator is closed, so we break the loop
            if (datum == null) {
                break;
            }
            try {
                String msg = new String(datum.getValue());
                log.info("Received message: {}, headers - {}, topic name is - {}", msg, datum.getHeaders(), this.topicName);
                responseListBuilder.addResponse(Response.responseOK(datum.getId()));
            } catch (Exception e) {
                responseListBuilder.addResponse(Response.responseFailure(
                        datum.getId(),
                        e.getMessage()));
            }
        }
        return responseListBuilder.build();
    }

    @Override
    public void destroy() {
        log.info("send shutdown signal");
        log.info("kafka producer closed");
    }
}
