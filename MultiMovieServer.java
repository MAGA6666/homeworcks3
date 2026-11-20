import java.io.*;
import java.net.*;
import java.util.*;

public class MultiMovieServer {
    private static int totalRequests = 0; // Общий счётчик обращений
    private static final Object lock = new Object(); // Замок для синхронизации
    private static final Map<String, Double> movies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        movies.put("Крестный отец", 9.2);
        movies.put("Волк с Уолл-стрит", 8.0);
        movies.put("Король Талсы", 8.1);
        movies.put("Во все тяжкие", 9.5);

        ServerSocket server = new ServerSocket(5000);
        System.out.println("Многопоточный сервер запущен на порту 5000 и ждёт клиентов...");

        while (true) {
            Socket client = server.accept();
            new Thread(() -> handle(client)).start();
        }
    }

    private static void handle(Socket client) {
        String threadName = Thread.currentThread().getName();
        System.out.println("Клиент подключился! Обслуживает поток: " + threadName);

        try (client;
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

            String title = in.readLine();
            Thread.sleep(3000);

            String response;
            if (title != null && movies.containsKey(title)) {
                response = "Оценка фильма «" + title + "»: " + movies.get(title);
            } else {
                response = "Фильм «" + title + "» не найден в каталоге";
            }

            out.println(response);

            int currentCount;
            synchronized (lock) {
                totalRequests++;
                currentCount = totalRequests;
            }
            System.out.println("Поток " + threadName + " ответил клиенту. Всего обслужено: " + currentCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
