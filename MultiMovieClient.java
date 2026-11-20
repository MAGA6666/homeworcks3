import java.io.*;
import java.net.*;

public class MultiMovieClient {
    public static void main(String[] args) {
        String[] requests = {"Крестный отец", "Во все тяжкие", "Неизвестный фильм"};

        for (int i = 0; i < requests.length; i++) {
            final int clientNum = i + 1;
            final String movieTitle = requests[i];

            new Thread(() -> {
                try (Socket socket = new Socket("127.0.0.1", 5000);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    long startTime = System.currentTimeMillis();
                    out.println(movieTitle);
                    String answer = in.readLine();
                    long elapsed = (System.currentTimeMillis() - startTime) / 1000;

                    System.out.println("Клиент №" + clientNum + " (запрос: " + movieTitle + ") получил ответ за "
                            + elapsed + " сек: " + answer);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}