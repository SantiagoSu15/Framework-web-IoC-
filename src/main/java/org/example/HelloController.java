package org.example;

@RestController
public class HelloController {

    @GetMapping("/")
    public static String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/pi")
    public static String getPi() {
        return "pi" + Math.PI;
    }

    @GetMapping("/hello")
    public static String hellow() {
        return "Hello World!";
    }
}