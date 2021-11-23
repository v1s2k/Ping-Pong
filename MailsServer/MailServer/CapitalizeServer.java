package MailServer;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class CapitalizeServer {

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
            System.out.println("Подключение к   : " + clientSocket);
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new Scanner(clientSocket.getInputStream());

                while (in.hasNextLine()) {
                    System.out.println("cycle started: " + history);
                    TimeUnit.SECONDS.sleep(5);
                            synchronized (allHistory) {
                                allHistory.add(in.nextLine());
                                System.out.println("allHistory: " + allHistory);
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
                            if (history.size()>0){
                                    System.out.println("message: "+ history);
                                    out.println("Archived messages: "+ history);
                                history.clear();
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
}
