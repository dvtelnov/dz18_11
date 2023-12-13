package com.example.demo;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@Controller
@RestController
public class ApiController {
    private final List<User> users = new ArrayList<>();


    @PostMapping("users/{repeatPassword}")    //curl -X POST -H "Content-Type: application/json" localhost:8080/users -d "{\"username\":\"Danya\",\"password\":\"15dfsgsd\",\"id\":\"2345678\",\"age\":\"12\"}"
    public ResponseEntity<Void> addUser(@RequestBody User user, // curl -v -X POST -H "Content-Type: application/json" localhost:8080/users/15dfsgsd -d "{\"username\":\"Danya\",\"password\":\"15dfsgsd\",\"id\":\"1234\",\"age\":\"17\"}"
                                        @PathVariable("repeatPassword") String repeatPassword) {
            String userID = user.getId();
            String password=user.getPassword();
            if (repeatPassword.equals(password)) {
                for (int i = 0; i < users.size(); i++) {
                    User userCheck = users.get(i);
                    String IDCheck = userCheck.getId();
                    if (IDCheck.equals(userID)) {
                        return status(409).build();
                    }
                }
                users.add(user);
                return ResponseEntity.accepted().build();
            } else {
                return status(400).build();
            }
    }

    @GetMapping("users/{page}/{quantity}") //curl -X GET -v -H "Content-Type: application/json" "localhost:8080/users/0/1?age=239&sortBy=id&direction=up"
    public ResponseEntity<List<UserInfo>> getUsers(
            @RequestParam(value = "age", defaultValue = "-1") int age,
            @RequestParam(value = "sortBy", defaultValue = "no") String sortBy,
            @RequestParam(value = "direction", defaultValue = "no") String direction,
            @PathVariable("page") int page,
            @PathVariable ("quantity") int quantity
    ) {
        if (age <= -1) {
            List<UserInfo> userInfos = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                UserInfo userInfo = new UserInfo(user.getUsername(), user.getId(), user.getAge());
                userInfos.add(userInfo);
            }
            if (sortBy.equals("id")){
                List<String>IDs=new ArrayList<>();
                for (int j=0; j<userInfos.size(); j++){
                    UserInfo user=userInfos.get(j);
                    String userID=user.getId();
                    IDs.add(userID);
                }
                if (direction.equals("up")){
                    Collections.sort(IDs);
                }
                if (direction.equals("down")){
                    Collections.sort(IDs);
                    Collections.reverse(IDs);
                }
                List<UserInfo>sorted=new ArrayList<>();
                for (int m=0; m<IDs.size(); m++){
                    String ID=IDs.get(m);
                    for (int n=0; n<userInfos.size(); n++){
                        UserInfo userinfo=userInfos.get(n);
                        String userId=userinfo.getId();
                        if (userId.equals(ID)){
                           sorted.add(userinfo);
                        }
                    }
                }
                if (page>=0&&quantity>0){
                    List<UserInfo>answer=new ArrayList<>();
                    int index=page*quantity;
                    while (index<page*quantity+quantity&&index<sorted.size()){
                        UserInfo info=sorted.get(index);
                        answer.add(info);
                        index++;
                    }
                    return ResponseEntity.ok(answer);
                }
                return ResponseEntity.ok(sorted);
            }
            if (page>=0&&quantity>0){
                List<UserInfo>answer=new ArrayList<>();
                int index=page*quantity;
                while (index<page*quantity+quantity&&index<userInfos.size()){
                    UserInfo info=userInfos.get(index);
                    answer.add(info);
                    index++;
                }
                return ResponseEntity.ok(answer);
            }
            return ResponseEntity.ok(userInfos);
        } else {
            List<UserInfo> userInfos = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                int userAge = user.getAge();
                if (age - 5 <= userAge && age + 5 >= userAge) {
                    UserInfo userInfo = new UserInfo(user.getUsername(), user.getId(), user.getAge());
                    userInfos.add(userInfo);
                }
            }
                if (sortBy.equals("id")){
                    List<String>IDs=new ArrayList<>();
                    for (int j=0; j<userInfos.size(); j++){
                        UserInfo user=userInfos.get(j);
                        String userID=user.getId();
                        IDs.add(userID);
                    }
                    if (direction.equals("up")){
                        Collections.sort(IDs);
                    }
                    if (direction.equals("down")){
                        Collections.sort(IDs);
                        Collections.reverse(IDs);
                    }
                    List<UserInfo>sorted=new ArrayList<>();
                    for (int m=0; m<IDs.size(); m++){
                        String ID=IDs.get(m);
                        for (int n=0; n<userInfos.size(); n++){
                            UserInfo userinfo=userInfos.get(n);
                            String userId=userinfo.getId();
                            if (userId.equals(ID)){
                                sorted.add(userinfo);
                            }
                        }
                    }
                    if (page>=0&&quantity>0){
                        List<UserInfo>answer=new ArrayList<>();
                        int index=page*quantity;
                        while (index<page*quantity+quantity&&index<sorted.size()){
                            UserInfo info=sorted.get(index);
                            answer.add(info);
                            index++;
                        }
                        return ResponseEntity.ok(answer);
                    }
                    return ResponseEntity.ok(sorted);
                }
            if (page>=0&&quantity>0){
                List<UserInfo>answer=new ArrayList<>();
                int index=page*quantity;
                while (index<page*quantity+quantity&&index<userInfos.size()){
                    UserInfo info=userInfos.get(index);
                    answer.add(info);
                    index++;
                }
                return ResponseEntity.ok(answer);
            }
                return ResponseEntity.ok(userInfos);
        }
    }

    @GetMapping("users/{id}") //curl -X GET -H "Content-Type: application/json" localhost:8080/users/12
    public ResponseEntity<UserInfo> getUser(@PathVariable("id") String id) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String checkedId = user.getId();
            if (checkedId.equals(id)) {
                UserInfo userInfo = new UserInfo(user.getUsername(), user.getId(), user.getAge());
                return ResponseEntity.ok(userInfo);
            }
        }
        return status(404).build();
    }

    @DeleteMapping("users/{id}") //curl -v -X DELETE -H "Content-Type: application/json" localhost:8080/users/3478
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String checkedId = user.getId();
            if (checkedId.equals(id)) {
                users.remove(i);
                return ResponseEntity.noContent().build();
            }
        }
        return status(404).build();
    }

    @PutMapping("users/{id}/{repeatPassword}") //curl -v -X PUT -H "Content-Type: application/json" localhost:8080/users/12 -d "Fedya"
    public ResponseEntity<Void> updateContact( //curl -v -X PUT -H "Content-Type: application/json" localhost:8080/users/1234/15dfsgsd -d "Yaroslav"
            @PathVariable("id") String id,
            @PathVariable("repeatPassword") String repeatPassword,
            @RequestBody String name) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            String checkedId = user.getId();
            if (checkedId.equals(id)) {
                if (user.getPassword().equals(repeatPassword)) {
                    User userNew = new User(name, user.getPassword(), user.getId(), user.getAge());
                    users.remove(i);
                    users.add(i, userNew);
                    return ResponseEntity.noContent().build();
                } else {
                    return status(400).build();
                }
            }
        }
        return status(404).build();
    }
}