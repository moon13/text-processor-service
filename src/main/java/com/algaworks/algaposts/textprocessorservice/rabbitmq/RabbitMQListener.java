package com.algaworks.algaposts.textprocessorservice.rabbitmq;

import com.algaworks.algaposts.posts_service.api.model.PostOutput;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

   // private final TemperatureMonitoringService temperatureMonitoringService;

    private final RabbitTemplate rabbitTemplate;


    @SneakyThrows
    @RabbitListener(queues = "text-processor-service.post-processing.v1.q", concurrency = "2-3")
    public void handleProcessingPost(@Payload PostOutput postOutput,
                       @Headers Map<String,Object> headers

    ){
        log.info("TEXT_PROCESSOR FILA. Postid {} Author {}", postOutput.getId(), postOutput.getAuthor());


        String fila = "text-processor-service.post-processing.v1.q";
        String fila_pos= "post-service.post-processing-result.v1.q";
        String routingKey = "";

        //Integer.valueOf(input.getBody().trim().length()
        Double calculo = 0.10;
        //log.info("Tentando consumir da fila {} e {} ", calculo, postOutput.getId());
        //PostOutput postOutput = (PostOutput) rabbitTemplate.receiveAndConvert(fila);
        //log.info("retornou da fila {} ", postOutput);
        postOutput.setWordCount(Integer.valueOf(postOutput.getBody().trim().length()));
        postOutput.setCalculatedValue(calculo * postOutput.getWordCount());
        log.info("Valor das palavras calculado {}", postOutput.getCalculatedValue());
        log.info("Mandando para a fila de pos processamento.");
        Object payload = postOutput;
        rabbitTemplate.convertSendAndReceive(fila_pos,payload);

        //rabbitTemplate.convertAndSend(exchange, routingKey, payload);

       // temperatureMonitoringService.processTemperatureReading(temperatureLogData);
       Thread.sleep(Duration.ofSeconds(5));
    }

}
