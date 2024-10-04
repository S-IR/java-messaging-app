package com.example.demo.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Message {

    public enum From {
        Jim,
        Tim

    }

    private LocalDate date;
    private String message;
    private From personName;


    public Message( From personName, String message) {
        this.date = LocalDate.now();
        this.message = message;
        this.personName = personName;
    }


    public Message( From personName, String message,  LocalDate date) {
        this.date = date;
        this.message = message;
        this.personName = personName;
    }

    public JSONObject toJsonObject() {


        JSONObject obj = new JSONObject();

        System.out.println("enum value from this msg: " + this.personName);

        obj.put("date" , this.date.toString());
        obj.put("message", this.message);

        String personNameStr = this.personName == From.Jim ? "Jim" : "Tim";
        obj.put("personName", personNameStr);

        return obj;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public From getPersonName() {
        return personName;
    }

    public void setPersonName(From personName) {
        this.personName = personName;
    }

    public static Message toMessage(JSONObject obj){
        String personNameStr = (String) obj.get("personName");
        String message = (String) obj.get("message");
        String dateString = (String) obj.get("date");

        Message.From personName = Message.From.valueOf(personNameStr);
        LocalDate date = LocalDate.parse(dateString);

        Message msg = new Message(personName, message, date);
        return msg;

    }

    public  static  ArrayList<Message> toMessage(JSONArray arr) {
        ArrayList<Message> messages = new ArrayList<>();
        for ( Object obj : arr) {
            Message msg = Message.toMessage( (JSONObject) obj);
            messages.add(msg);
        }
        return messages;

    }
}
