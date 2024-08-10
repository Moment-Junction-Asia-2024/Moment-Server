package com.example.momentService.kafka;

import com.example.momentService.kafka.dto.Answer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaService {
    private final KafkaTemplate<Integer, String> template;
    private final ObjectMapper objectMapper;

    public void sendToKafka(@RequestBody Answer data) throws Exception {
        final ProducerRecord<Integer, String> record = createRecord(data);

        CompletableFuture<SendResult<Integer, String>> future = template.send(record);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent record: {}", record);
            }
            else {
                log.error("Failed to send record: {}", record, ex);
            }
        });
    }

    private ProducerRecord<Integer, String> createRecord(Answer data) throws Exception{
        return new ProducerRecord<>(
                "athena",
                data.getId().intValue(),
                objectMapper.writeValueAsString(data.getEvent())
        );
    }
}
