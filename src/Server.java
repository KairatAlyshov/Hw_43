import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Server {

    public static void run(){
        try {
        HttpServer server = makeServer("localHost", 9889);
        initRoutes(server);
        server.start();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private static HttpServer makeServer(String host, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(host, port);
        String msg = "запускаем сервер по адресу" + " http://%s:%s/%n";
        System.out.printf(msg, address.getHostName(), address.getPort());
        HttpServer server = HttpServer.create(address, 50);
        System.out.println("  удачно!");   return server;
    }

    private static void initRoutes(HttpServer server) {
        server.createContext("/", Server::handleRequest);
        server.createContext("/apps/", Server::handleRequestApp);
        server.createContext("/apps/profile",Server::handleRequestProfileApp);
        server.createContext("/index.html",Server::css);
        server.createContext("/index.html",Server::html);
    }


    private static void handleRequest(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "HTTP Метод", method);
                write(writer, "Запрос", uri.toString());
                write(writer, "Обработан через", ctxPath);
                writeHeaders(writer, "Заголовки запроса",
                        exchange.getRequestHeaders());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequestApp(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "Method: ", method);
                write(writer, "Requesr: ", uri.toString());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequestProfileApp(HttpExchange exchange) {
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");

            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                String method = exchange.getRequestMethod();
                URI uri = exchange.getRequestURI();
                String ctxPath = exchange.getHttpContext()
                        .getPath();
                write(writer, "Method: ", method);
                write(writer, "Requesr: ", uri.toString());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Writer getWriterFrom(HttpExchange exchange) {
        OutputStream output = exchange.getResponseBody();
        Charset charset = StandardCharsets.UTF_8;
        return new PrintWriter(output, false, charset);
    }

    private static void write(Writer writer, String msg, String method) {
        String data = String.format("%s: %s%n%n", msg, method);
        try {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } }

    private static void writeHeaders(Writer writer, String type, Headers headers) {
        write(writer, type, "");
        headers.forEach((k, v) -> write(writer, "\t" + k, v.toString()));
    }

    private static void html(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        Path path = Path.of("src/homework/" + uri);
        byte[] cssWay = Files.readAllBytes(path);
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");

            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                for (byte b: cssWay) {
                    writer.write(b);
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void css(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().toString();
        Path path = Path.of("/src/homework/css" + uri);
        byte[] cssWay = Files.readAllBytes(path);
        try {
            exchange.getResponseHeaders().add("Content-Type", "text/css");

            int responseCode = 200;
            int length = 0;
            exchange.sendResponseHeaders(responseCode, length);

            try (Writer writer = getWriterFrom(exchange)) {
                for (byte b: cssWay) {
                    writer.write(b);
                }
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
