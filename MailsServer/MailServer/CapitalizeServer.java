package MailServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
/**
 * Серверная программа, которая принимает запросы от
 клиентов на обработку строк с заглавных букв.
 * Когда клиент подключается, то запускается новый поток
 * для его обработки. Получение клиентских данных, их
 * использование и отправка ответа - все это делается в
 * потоке, что обеспечивает гораздо большую пропускную
 *способность, поскольку одновременно может
 * обрабатываться больше клиентов.
 */
public class CapitalizeServer {
    /**
     * Запускается сервер. Когда клиент подключается,
     * сервер создает новый поток для обслуживания и
     * немедленно возвращается к прослушиванию.
     * Приложение ограничивает количество потоков через
     * пул потоков (в противном случае миллионы клиентов
     * могут привести к исчерпанию ресурсов сервера из-за
     * выделения слишком большого количества потоков).
     */
    public static void main(String[] args) throws
            Exception {
        try (var listener = new ServerSocket(5555)) {
            System.out.println("Сервер запущен...");
            LinkedList<String> allHistory = new LinkedList<>();
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new EchoClientHandler(allHistory, listener.accept()));
            }
        }
    }
    private static class EchoClientHandler extends Thread {
        LinkedList<String> allHistory;
        private LinkedList<String> history = new LinkedList<>();
        private long lastSended;
        private Socket clientSocket;
        private PrintWriter out;
        private Scanner in;
        private int bookmark = 0;

        public EchoClientHandler(LinkedList<String> allhist, Socket socket) {
            this.allHistory = allhist;
            this.clientSocket = socket;
            this.lastSended = System.currentTimeMillis();
        }

        public void run() {
            System.out.println("Подключение: " + clientSocket);
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                in = new Scanner(clientSocket.getInputStream());

                while (true) {
                    System.out.println("cycle started: " + history);
                    while((System.currentTimeMillis() - lastSended) < 500) {
                        System.out.println("pass 1: " + (System.currentTimeMillis() - lastSended));
                        if (in.hasNext()) {
                            synchronized (allHistory) {
                                allHistory.add(in.nextLine());
                                System.out.println("allHistory: " + allHistory);
                            }
                        }
                    }
                        synchronized (allHistory) {
                            System.out.println("sizes: " + allHistory.size() + ", " + bookmark);
                            if (allHistory.size() > bookmark) {
                                for (int i = bookmark; i < allHistory.size(); i++) {
                                    history.add(allHistory.get(i));
                                }
                                System.out.println("history: " + history);
                                bookmark = allHistory.size();
                            }
                        }
                    System.out.println("pass 2: " + (System.currentTimeMillis() - lastSended));
                    if ((System.currentTimeMillis() - lastSended) > 500) {
                        /*synchronized (allHistory) {
                            if (allHistory.size() > bookmark) {
                                //history.addAll(bookmark, allHistory);
                                for (int i = bookmark; i < allHistory.size(); i++) {
                                    history.add(allHistory.get(i));
                                }
                                System.out.println("history: "+history);
                                bookmark = allHistory.size();
                            }
                        }*/
                            if (history.size()>0){
                                for (String message: history){
                                    System.out.println("message: "+message);
                                    out.println(message);
                                }
                                history.clear();
                                //lastSended = System.currentTimeMillis();
                                //System.out.println("cycle ended"+history);
                            }
                        lastSended = System.currentTimeMillis();
                    }
                    System.out.println("cycle ended: "+history);
                }
            } catch (Exception e) {
                System.out.println("Ошибка:" + clientSocket);
                System.out.println(e);
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Closed: " + clientSocket);
            }
        }
    }



/*    private static class Capitalizer implements Runnable
    {
        private Socket socket;
        Capitalizer(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            System.out.println("Подключение: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);
                while (in.hasNextLine()) {

                    out.println(in.nextLine().toUpperCase());
                }
            } catch (Exception e) {
                System.out.println("Ошибка:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }
    }*/
}
