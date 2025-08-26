package com.lbritosan.first_spring_app.controller;

import com.lbritosan.first_spring_app.domain.User;
import com.lbritosan.first_spring_app.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hello-world")
public class HelloWorldController {

    // STATELESS - o que é?
    // STATEFULL - o que é?

    @Autowired
    private HelloWorldService helloWorldService;
    /*
    public HelloWorldController(HelloWorldService helloWorldService){
        this.helloWorldService = helloWorldService;
    }
    */

    // GET /hello-world
    @GetMapping
    public String helloWorld(){
        return helloWorldService.helloWorld("Leonardo");
    }

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String helloWorldPost(@PathVariable("id") String id,
                                 @RequestParam(value = "filter", defaultValue = "nenhum") String filter,
                                 @RequestBody User body) {
        return "Hello World " + body.getName() + " id: " + id + " filter: " + filter;
    }
}
