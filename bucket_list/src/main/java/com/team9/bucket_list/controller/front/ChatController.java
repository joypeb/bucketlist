package com.team9.bucket_list.controller.front;

import com.team9.bucket_list.domain.dto.chat.ChatRequest;
import com.team9.bucket_list.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat/room")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations template;

    //방 리스트 화면으로 이동
    @GetMapping("/list")
    public String chatRoomList() {
        return "chatRoomList";
    }

    //방 내부
    @GetMapping("/{roomId}")
    public String chatRoom(@PathVariable Long roomId) {
        //채팅방의 내용은 비밀이어야하기때문에 채팅방에 있는 유저중에 로그인된 유저가 있는지 확인 후 최근 20개의 메시지를 보낸다
        //chatService에서 비교, 로그인이 완성될 경우 코드 작성

        return "chatRoom";
    }

    @MessageMapping("/chat/enter")
    public void enter(@Payload ChatRequest chatRequest, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatRequest.getUserName());
        template.convertAndSend("/sub/chat/room/"+chatRequest.getRoomId(),chatRequest);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload ChatRequest chatRequest) {
        chatService.updateMessage(chatRequest);
        template.convertAndSend("/sub/chat/room/"+chatRequest.getRoomId(), chatRequest);
    }
}