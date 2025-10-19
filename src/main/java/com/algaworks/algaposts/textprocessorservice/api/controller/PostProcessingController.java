package com.algaworks.algaposts.textprocessorservice.api.controller;

import com.algaworks.algaposts.posts_service.api.model.PostOutput;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/posts/{postId}")
@Slf4j
@RequiredArgsConstructor
public class PostProcessingController {

    private final RabbitTemplate rabbitTemplate;


    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID postId, @RequestBody String input){
        if (input == null || input.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Double calculo;
        try{
            calculo = Double.parseDouble(input);
        }catch(NumberFormatException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
         log.info("passou da checagem de valor");
           /* TemperatureLogOutput logOutput = TemperatureLogOutput.builder()
                    .id(IdGenerator.generateTimeBasedUUID())
                    .sensorId(sensorId)
                    .value(temperature)
                    .registeredAt(OffsetDateTime.now())
                    .build();

            log.info(logOutput.toString());*/

           // String exchange = FANOUT_EXCHANGE_NAME;
             String fila = "text-processor-service.post-processing.v1.q";
           // String routingKey = "";
           // Object payload = logOutput;
       /* MessagePostProcessor messagePostProcessor = (MessagePostProcessor) message -> {
             message.getMessageProperties().setHeader("sensorId",logOutput.getSensorId().toString());
             return message;
        };*/
        //rabbitTemplate.convertSendAndReceive(exchange,routingKey,payload, messagePostProcessor);
        log.info("Tentando consumir da fila {} e {} ", calculo, postId);
        PostOutput postOutput = (PostOutput) rabbitTemplate.receiveAndConvert(fila);
        log.info("retornou da fila {} ", postOutput);
    }
}
