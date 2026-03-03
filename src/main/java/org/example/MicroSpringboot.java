package org.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MicroSpringboot {

    public static Map<String, Method> controlerMethods = new HashMap<>();

    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        System.out.println("Loading rest controllers and their methods:");

        Class<?> c = Class.forName(args[0]); //instancia de la clase

        if(c.isAnnotationPresent(RestController.class)){
            System.out.println("Found a rest controller");
            for(Method m : c.getMethods()){
                if(m.isAnnotationPresent(GetMapping.class)){
                    GetMapping a = m.getAnnotation(GetMapping.class);
                    String path = a.value();
                    controlerMethods.put(path,m);
                }
            }
        }

        System.out.println("Running method: " + args[1]);

        Method m = controlerMethods.get(args[1]);

        System.out.println("" + m.invoke(null));
    }
}
