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
    @GetMapping("messages") //curl -X GET -H "Content-Type: application/json" localhost:8080/messages
    public ResponseEntity<List<String>> getMessages() {
        return ResponseEntity.ok(messages);
    }
    @PostMapping("messages") //curl -X POST -H "Content-Type: application/json" localhost:8080/messages -d "data 3 "
    public ResponseEntity<Void> addMessage(@RequestBody String text) {
        messages.add(text);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("messages/{index}") //curl -X GET -H "Content-Type: application/json" localhost:8080/messages/1
    public ResponseEntity<String> getMessage(@PathVariable("index") Integer
                                                     index) {
        return ResponseEntity.ok(messages.get(index));
    }
    @DeleteMapping("messages/{index}") //curl -X DELETE -H "Content-Type: application/json" localhost:8080/messages/0
    public ResponseEntity<Void> deleteText(@PathVariable("index") Integer
                                                   index) {
        messages.remove((int) index);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("messages/{index}") //curl -X PUT -H "Content-Type: application/json" localhost:8080/messages/1 -d "data 4"
    public ResponseEntity<Void> updateMessage(
            @PathVariable("index") Integer i,
            @RequestBody String message) {
        messages.remove((int) i);
        messages.add(i, message);
        return ResponseEntity.accepted().build();
    }
}