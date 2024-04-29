package com.example.socializingapp.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessageDto {

    @NotBlank(message = "Sender cannot be blank")
    @Size(min = 2, max = 30, message = "Username should have 2-30 characters!")
    private String sender;
    @NotBlank(message = "Receiver cannot be blank")
    @Size(min = 2, max = 30, message = "Username should have 2-30 characters!")
    private String receiver;
    @NotBlank(message = "Message content cannot be blank")
    private String content;


}
