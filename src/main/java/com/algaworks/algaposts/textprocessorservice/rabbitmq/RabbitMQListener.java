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

import static com.algaworks.algaposts.textprocessorservice.rabbitmq.RabbitMQConfig.QUEUE_POST_PROCESSING;
import static com.algaworks.algaposts.textprocessorservice.rabbitmq.RabbitMQConfig.QUEUE_POST_RESULT;


@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

    private final RabbitTemplate rabbitTemplate;


    @SneakyThrows
    @RabbitListener(queues = QUEUE_POST_PROCESSING, concurrency = "2-3")
    public void handleProcessingPost(@Payload PostOutput postOutput,
                       @Headers Map<String,Object> headers

    ){
        log.info("TEXT_PROCESSOR FILA. Postid {} Author {}", postOutput.getId(), postOutput.getAuthor());

        Double calculo = 0.10;

        postOutput.setWordCount(Integer.valueOf(postOutput.getBody().trim().length()));
        postOutput.setCalculatedValue(calculo * postOutput.getWordCount());
        log.info("Valor das palavras calculado {}", postOutput.getCalculatedValue());
        log.info("Mandando para a fila de pos processamento.");

        String exchange = "";
        String routingKey = QUEUE_POST_RESULT;
        Object payload = postOutput;

        rabbitTemplate.convertAndSend(exchange,routingKey,payload);

        Thread.sleep(Duration.ofSeconds(5));
    }

}
