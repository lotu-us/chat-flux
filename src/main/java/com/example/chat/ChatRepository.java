package com.example.chat;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String> {
    @Tailable   //커서를 안닫고 계속 유지한다
    @Query("{sender:?0, receiver:?1}")  //mongodb 쿼리문
    Flux<Chat> findBySender(String sender, String receiver);    //Flux : 흐름

    //tailable 버퍼사이즈 수정
   //db.runCommand({convertToCapped: 'chat', size:8192});

    @Tailable
    @Query("{roomNum:?0}")
    Flux<Chat> findByRoomNum(Integer roomNum);
}
