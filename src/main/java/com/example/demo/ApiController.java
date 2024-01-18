package com.example.demo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Controller
@RestController
@RequestMapping("/api")
public class ApiController {
    private final UserRepository userRepository;
    public ApiController(UserRepository tutorialRepository) {
        this.userRepository = tutorialRepository;
    }
    @PostMapping("/users/{repeatPassword}") //curl -X POST -H "Content-Type: application/json" localhost:8080/api/users/12345 -d "{\"username\":\"Danya\",\"password\":\"12345\", \"age\":\"100\"}"
    public User createUser(@RequestBody User user,
                           @PathVariable("repeatPassword") String repeatPassword) {
        List<User> users = userRepository.findByUsername(user.getUsername());
        if (user.getPassword().equals(repeatPassword)) {
            if (users.isEmpty()) {
                return userRepository
                        .save(new User(user.getUsername(),
                                user.getPassword(), user.getAge()));
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/users/{id}") //curl -v -X GET -H "Content-Type: application/json" localhost:8080/api/users/1
    public UserInfo getUserById(@PathVariable("id") long id) {
        Optional<User> userData =
                userRepository.findById(id);

        if (userData.isPresent()) {
            User user = userData.get();
            UserInfo info = new UserInfo(user.getUsername(), user.getId(), user.getAge());
            return info;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/users/{id}") //curl -v -X DELETE -H "Content-Type: application/json" localhost:8080/api/users/1
    public HttpStatus deleteUser(@PathVariable("id") long id) {
        Optional<User> userData =
                userRepository.findById(id);

        if (userData.isPresent()) {
            userRepository.deleteById(id);
            return HttpStatus.NO_CONTENT;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/users/{id}/{repeatPassword}") //curl -v -X PUT -H "Content-Type: application/json" localhost:8080/api/tutorials/2/12345 -d "Danya"
    public User updateUser(@PathVariable("id") long id,
                           @PathVariable("repeatPassword") String repeatPassword,
                           @RequestBody String username) {

        Optional<User> userData =
                userRepository.findById(id);
        if (userData.isPresent()) {
            User _user = userData.get();
            if (repeatPassword.equals(_user.getPassword())) {
                _user.setUsername(username);
                return userRepository.save(_user);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users") //curl -v -X GET -H "Content-Type: application/json" localhost:8080/api/users?age=100
    public List<UserInfo> getAllUsers(@RequestParam(value = "age", defaultValue = "-1") int age) {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        List<UserInfo> infos = new ArrayList<>();
        if (age > 0) {
            for (int i=0; i<users.size(); i++){
                User user=users.get(i);
                int userAge=user.getAge();
                if ((userAge-age)>-5 && (userAge-age)<5){
                    UserInfo info=new UserInfo(user.getUsername(), user.getId(), user.getAge());
                    infos.add(info);
                }
            }

        } else {
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                UserInfo info = new UserInfo(user.getUsername(), user.getId(), user.getAge());
                infos.add(info);
            }
        }
        return infos;
    }
}
