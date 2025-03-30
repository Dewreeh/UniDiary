package org.repin.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.repin.dto.LoginInfoDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final MailService mailService;

    public KafkaConsumerService(KafkaTemplate<String, String> kafkaTemplate,
                                ObjectMapper objectMapper,
                                MailService mailService){
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.mailService = mailService;
    }

    @KafkaListener(topics = "send-generated-password", groupId = "email-consumer-group")
    public void listenGeneratedPassword(String message){
        try {
            LoginInfoDto dto = objectMapper.readValue(message, LoginInfoDto.class);
            mailService.sendEmail(dto.getEmail(), "Сгенерированный пароль", dto.toString());
        } catch (Exception e){
            log.info("Ошибка чтения JSON");
        }

    }
}
