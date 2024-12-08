package io.numaproj.confluent.kafka_sink;

import io.numaproj.numaflow.sinker.Datum;
import io.numaproj.numaflow.sinker.DatumIterator;
import io.numaproj.numaflow.sinker.Response;
import io.numaproj.numaflow.sinker.ResponseList;
import io.numaproj.numaflow.sinker.Server;
import io.numaproj.numaflow.sinker.Sinker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class KafkaSinkApplication extends Sinker {

	public static void main(String[] args) throws Exception {
		Server server = new Server(new KafkaSinkApplication());

		// Start the server
		server.start();

		// wait for the server to shut down
		server.awaitTermination();
	}

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
				log.info("Received message: {}, headers - {}", msg, datum.getHeaders());
				responseListBuilder.addResponse(Response.responseOK(datum.getId()));
			} catch (Exception e) {
				responseListBuilder.addResponse(Response.responseFailure(
						datum.getId(),
						e.getMessage()));
			}
		}
		return responseListBuilder.build();
	}
}
