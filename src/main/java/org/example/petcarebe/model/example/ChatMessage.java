package org.example.petcarebe.model.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String type;      // JOIN, CHAT, PRIVATE
    private String sender;
    private String receiver;  // optional for private
    private String content;
}
