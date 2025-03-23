package com.application;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReminderApp {
    public static void main(String[] args) {
        System.out.println("Hello World!!");
        ObjectMapper mapper = new ObjectMapper();
        try{
            File file = new File("data.json");
            List<Payment> paymentList = Arrays.asList(mapper.readValue(file, Payment[].class));
            System.out.println(paymentList);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
