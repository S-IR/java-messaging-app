package com.example.demo.controller;


import com.example.demo.model.Message;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


@Controller
public class MainController {

//    @GetMapping("/chat")
//    public String index(){
//        return "hello.html";
//    }

    public static final String JSON_PATH = "./output.json";


    @PostMapping("/chat")
    public ResponseEntity<?> postMessage(@RequestParam String name, @RequestBody String body, Model model) throws IllegalArgumentException,
            ParseException, IOException {
        System.out.println("body: "+ body );


        File file = new File(JSON_PATH);
        boolean isNewFile = !file.exists();

        JSONParser parser = new JSONParser();


        Message.From actualName;
        try {
            actualName = Message.From.valueOf(name.trim());
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                System.out.println("BAD NAME , GOTTEN" + name);
                return ResponseEntity.badRequest().body("you are not very nice!");
            }
            throw e;
        }
        Message msg = new Message(actualName, body);


        if (isNewFile) {
            FileWriter writer = new FileWriter(JSON_PATH);

            JSONObject jsonFormat = new JSONObject();

            JSONArray messagesArray = new JSONArray();
            messagesArray.add(msg.toJsonObject());

            jsonFormat.put("messages", messagesArray);
            writer.write(jsonFormat.toJSONString());
            System.out.println("CREATING MESSAGES");

            writer.close();
        } else {

            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(JSON_PATH));
            JSONArray existingMessages = (JSONArray) jsonObject.get("messages");

            existingMessages.add(msg.toJsonObject());
            FileWriter writer = new FileWriter(JSON_PATH);
            writer.write(jsonObject.toJSONString());
            System.out.println("ADDING MESSAGE");
            writer.close();

        }

        Resource resource = new ClassPathResource("templates/thank-you.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    ;

    @GetMapping("/chat")
    public String getMessages(Model model) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(JSON_PATH));
        JSONArray jsonMessages = (JSONArray) jsonObject.get("messages");
        ArrayList<Message> messages = Message.toMessage(jsonMessages);


        model.addAttribute("messages", messages);
        return "messages";
    }


}
