package org.example;

import com.sun.tools.javac.Main;
import org.example.Anotaciones.GetMapping;
import org.example.Anotaciones.RequestParam;
import org.example.Anotaciones.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MicroSpringboot {

    public static Map<String, Method> controlerMethods = new HashMap<>();
    public static Map<String, Object> controllerInstances = new HashMap<>();


    public static void main(String[] args) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException, InstantiationException {
        System.out.println("Loading rest controllers and their methods:");


        Path root = Paths.get(System.getProperty("user.dir"));
        Path srcFolder = Files.list(root)
                .filter(p -> p.getFileName().toString().equals("src"))
                .findFirst()
                .orElse(null);

        if (srcFolder != null) {
            Path mainJavaPath = srcFolder.resolve("main/java/org");
            if (Files.exists(mainJavaPath)) {
                Stream<Path> stream = Files.walk(mainJavaPath);
                List<Path> todosLosArchivos = stream
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".java"))
                        .toList();

                List<String> stringFiles = todosLosArchivos.stream().map(Path :: toString)
                        .map(sPath -> {
                            int index = sPath.indexOf("org");
                            if (index != -1) {
                                String nuevo = sPath.substring(index);
                                return nuevo.replace("\\", ".")
                                        .substring(0, nuevo.length() - 5);
                            }
                            return sPath;
                        }).toList();

                //stringFiles.forEach(System.out::println);

                for(String claseString :stringFiles ){
                    Class<?> c = Class.forName(claseString); //instancia de la clase

                    if(c.isAnnotationPresent(RestController.class)){
                        System.out.println("Found a rest controller");
                        Object instance = c.getDeclaredConstructor().newInstance();
                        for (Method m : c.getMethods()) {
                            if (m.isAnnotationPresent(GetMapping.class)) {
                                GetMapping a = m.getAnnotation(GetMapping.class);
                                String path = a.value();
                                controlerMethods.put(path, m);
                                controllerInstances.put(path, instance);
                            }
                        }
                    }
                }
                System.out.println("Running method: " + args[0]);

                String input = args[0];
                String path = input.split("\\?")[0];
                String queryString = input.contains("?") ? input.split("\\?")[1] : "";

                Map<String, String> queryParams = new HashMap<>();
                if (!queryString.isBlank()) {
                    for (String pair : queryString.split("&")) {
                        String[] kv = pair.split("=", 2);
                        queryParams.put(kv[0], kv.length > 1 ? kv[1] : "");
                    }
                }

                Method m = controlerMethods.get(path);

                Parameter[] params = m.getParameters();
                Object[] invokeArgs = new Object[params.length];
                for (int i = 0; i < params.length; i++) {
                    if (params[i].isAnnotationPresent(RequestParam.class)) {
                        RequestParam rp = params[i].getAnnotation(RequestParam.class);
                        invokeArgs[i] = queryParams.getOrDefault(rp.value(), rp.defaultValue());
                    }
                }

                Object instance = controllerInstances.get(path);

                System.out.println("" + m.invoke(instance,invokeArgs));

            }
        }
    }
}
