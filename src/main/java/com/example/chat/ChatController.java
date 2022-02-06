package com.example.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RestController
public class ChatController {
    @Autowired private ChatRepository chatRepository;

    //MediaType.TEXT_EVENT_STREAM_VALUE : sse프로토콜을 사용해서 데이터의 요청흐름은 끊지만 응답흐름을 계속 유지시킬 수 있음
    @CrossOrigin    //자바스크립트에서 요청할 수 있도록
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMessage(@PathVariable String sender, @PathVariable String receiver){
        return chatRepository.findBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin    //자바스크립트에서 요청할 수 있도록
    @PostMapping("/chat")
    public Mono<Chat> saveMessage(@RequestBody Chat chat){
        chat.setCreateTime(LocalDateTime.now());
        return chatRepository.save(chat);   //return 없이 void로 save만 해도 상관없음.
    }



    @CrossOrigin    //자바스크립트에서 요청할 수 있도록
    @GetMapping(value = "/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getRoomMessage(@PathVariable Integer roomNum){
        return chatRepository.findByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
