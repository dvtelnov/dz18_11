package com.example.demo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
public class ApiController {
    private final List<String> messages = new ArrayList<>();
    private final List<User> users = new ArrayList<>();

    @GetMapping("messages") //curl -X GET -H "Content-Type: text/plain" localhost:8080/messages
    public ResponseEntity<List<String>> getMessages() {
        return ResponseEntity.ok(messages);
    }

    @PostMapping("messages") //curl -X POST -H "Content-Type: text/plain" localhost:8080/messages -d "data 3 "
    public ResponseEntity<Void> addMessage(@RequestBody String text) {
        messages.add(text);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("messages/{index}") //curl -X GET -H "Content-Type: text/plain" localhost:8080/messages/2
    public ResponseEntity<String> getMessage(@PathVariable("index") Integer
                                                     index) {
        return ResponseEntity.ok(messages.get(index));
    }

    @DeleteMapping("messages/{index}") //curl -X DELETE -H "Content-Type: text/plain" localhost:8080/messages/0
    public ResponseEntity<Void> deleteText(@PathVariable("index") Integer
                                                   index) {
        messages.remove((int) index);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("messages/{index}") //curl -X PUT -H "Content-Type: text/plain" localhost:8080/messages/1 -d "data 4 "
    public ResponseEntity<Void> updateMessage(
            @PathVariable("index") Integer i,
            @RequestBody String message) {
        messages.remove((int) i);
        messages.add(i, message);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("messages/search/{text}") //curl -X GET -H "Content-Type: text/plain" localhost:8080/messages/search/ata
    public ResponseEntity<Integer> getMessage(@PathVariable("text") String
                                                     text) {
        for (int i = 0; i < messages.size(); i++) {
            String checked = messages.get(i);
            if (checked.contains(text)) {
               return ResponseEntity.ok(i);
            }
        }
        return ResponseEntity.ok(-1);
    }
    @GetMapping("messages/count") //curl -X GET -H "Content-Type: text/plain" localhost:8080/messages/count
    public ResponseEntity<Integer> getMessagesCount() {
        return ResponseEntity.ok(messages.size());
    }
    @PostMapping("messages/{index}/create") //curl -X POST -H "Content-Type: text/plain" localhost:8080/messages/1/create -d "data 5 "
    public ResponseEntity<Void> addMessageIndex(
            @PathVariable("index") Integer i,
            @RequestBody String text) {
        messages.add(i, text);
        return ResponseEntity.accepted().build();
    }
    @DeleteMapping("messages/search/{text}") //curl -X DELETE -H "Content-Type: text/plain" localhost:8080/messages/search/at
    public ResponseEntity<Void> deleteText(@PathVariable("text") String text) {
        int index=0;
        while (index!=messages.size()){
            String checked= messages.get(index);
            if (checked.contains(text)){
                messages.remove(index);
                index=0;
            } else {
                index++;
            }
        }
        return ResponseEntity.noContent().build();
    }
    @PostMapping("users") //curl -X POST -H "Content-Type: application/json" localhost:8080/users -d "{\"name\":\"Danya\",\"age\":\"17\"}"
    public ResponseEntity<Void> addUsser(@RequestBody User user) {
        users.add(user);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("users") //curl -X GET -H "Content-Type: application/json" localhost:8080/users
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(users);
    }
    @GetMapping("users/{index}") //curl -X GET -H "Content-Type: application/json" localhost:8080/users/2
    public ResponseEntity<User> getUser(@PathVariable("index") Integer
                                                     index) {
        return ResponseEntity.ok(users.get(index));
    }
    @DeleteMapping("users/{index}") //curl -X DELETE -H "Content-Type: application/json" localhost:8080/users/1
    public ResponseEntity<Void> deleteUser(@PathVariable("index") Integer
                                                   index) {
        users.remove((int) index);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("users/{index}") //curl -X PUT -H "Content-Type: application/json" localhost:8080/users/3 -d "16"
    public ResponseEntity<Void> updateAge(
            @PathVariable("index") Integer i,
            @RequestBody Integer age) {
        User user=users.get(i);
        String userName=user.getName();
        User userNew=new User(age, userName);
        users.remove((int) i);
        users.add(i, userNew);
        return ResponseEntity.accepted().build();
    }

}