# MicroSpringBoot — Servidor Web con IoC en Java

Un servidor web construido desde cero en Java puro, sin dependencias externas, con un framework IoC (Inversión de Control) que carga y registra automáticamente componentes web (POJOs) usando reflexión de Java.

---

## Arquitectura

**Estructura del proyecto**
```
src/main/java/org/example/
├── MicroSpringBoot.java          → Núcleo: IoC, escaneo de anotaciones
├── controller/
│   └── HelloController.java     → Componente web de ejemplo
├── anotaciones/
│   ├── RestController.java      → Marca una clase como componente web
│   ├── GetMapping.java          → Mapea un método a una ruta GET
│   └── RequestParam.java        → Inyecta parámetros de query string
└── util/
    └── ...                      → Clases  de ejemplo

```

### Flujo de arranque (IoC)
```
MicroSpringBoot.main(args)
    │
    ▼
Escanea el classpath buscando clases con @RestController
    │
    ▼
Por cada clase encontrada, busca métodos con @GetMapping / @RequestParam
    │  Registra: ruta → método reflexivo
```

---

## Requisitos

- **Java 21**
- **Maven 3.6** o superior

Verificar instalación:
```bash
java -version
mvn -version
```

---

## Instalación y ejecución

### 1. Clonar el repositorio
```bash
git clone https://github.com/SantiagoSu15/Framework-web-IoC-.git
```

### 2. Compilar
```bash
mvn compile
```

### 3. Ejecutar — pasando el componente por línea de comandos (versión inicial)
```bash
java -cp target/classes org.example.MicroSpringBoot org.example.controller.HelloController {endpoint}
```

### 4. Ejecutar — escaneo automático del classpath (versión final)
```bash
java -cp target/classes org.example.MicroSpringboot {endpoint}
```



---

## Uso del framework

### Definir un componente web

Anotar la clase con `@RestController` y sus métodos con `@GetMapping`:
```java
@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```

### Usar parámetros de query string con `@RequestParam`
```java
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
```
Debido que al metodo de la clase GreetingController no es estatico toca guardar la instancia en un nuevo hashmap ``Map<String, Object> controllerInstances ``
y guardar en el bucle de los metodos  `` controllerInstances.put(path, instance);``.

EN la invocacion se busca la instancia de la clase en el map con el path
``` 
Object instance = controllerInstances.get(path); 
System.out.println("" + m.invoke(instance,invokeArgs));
```
y en el invoke deja de ser null pasando la instancia

---
# Evidencias Funcionamiento

**Primera version**

![Primera](/docs/1.png)

**Segunda Version**
![Segunda](/docs/2.png)

**Request Param**
![Request Param](/docs/3.png)

**Clase GreetingController**
![Clase GreetingController](/docs/4.png)


**Nota**
El lab https://github.com/SantiagoSu15/Microframework-Web/tree/main ya permite subir archivos estaticos html, png etc
revisar readme

# Evidencia Servidor de aplicaciones AWS

**Conexion y ejecucion desde git bash**
![Conexion](/docs/img.png)

**Endpoins**
![Endpoints](/docs/img_1.png)

![Endpoints](/docs/img_2.png)

![Endpoints](/docs/img_3.png)

![Endpoints](/docs/img_4.png)

![Endpoints](/docs/img_5.png)

![Endpoints](/docs/img_7.png)


![Endpoints](/docs/img_6.png)

## Construido con

- **Java** — Lenguaje principal
- **Maven** — Gestión de dependencias y build
- **Java Reflection API** (`java.lang.reflect`) — Carga y registro dinámico de componentes

---

## Autor

* **Santiago Suarez** — [SantiagoSu15](https://github.com/SantiagoSu15)