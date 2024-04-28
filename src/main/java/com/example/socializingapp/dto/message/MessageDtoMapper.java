package com.example.socializingapp.dto.message;

import com.example.socializingapp.entities.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageDtoMapper {

    public MessageDto fromMessage(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setReceiver(message.getReceiver().getUsername());
        messageDto.setSender(message.getSender().getUsername());
        messageDto.setContent(message.getContent());

        return messageDto;
    }
}
