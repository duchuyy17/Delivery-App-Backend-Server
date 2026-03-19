package com.laptrinhjavaweb.news.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.laptrinhjavaweb.news.dto.request.mongo.ChatMessageInput;
import com.laptrinhjavaweb.news.dto.response.mongo.ChatMessageResponse;
import com.laptrinhjavaweb.news.mongo.ChatDocument;
import com.laptrinhjavaweb.news.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatGraphQLController {
    private final ChatService chatService;

    @QueryMapping
    public List<ChatDocument> chat(@Argument("order") String orderId) {
        return chatService.getChatByOrder(orderId);
    }

    @MutationMapping
    public ChatMessageResponse sendChatMessage(
            @Argument String orderId, @Argument("message") ChatMessageInput messageInput) {
        ChatDocument output = chatService.sendMessage(orderId, messageInput);

        return new ChatMessageResponse(true, null, output);
    }
}
