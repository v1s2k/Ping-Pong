package MailServer;
import java.net.*;
import java. io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

public class MailServer {
    private ServerSocket serverSocket;

    public void start(int port, LinkedList<String> allhist) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true)
            new EchoClientHandler(allhist, serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
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
            System.out.println("Подключение к сокету : " + clientSocket);
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                in = new Scanner(clientSocket.getInputStream());

                while (true) {
                    if (in.hasNextLine()) {
                        synchronized (allHistory) {
                            allHistory.add(in.nextLine());
                            System.out.println(allHistory);
                        }
                    }
                    System.out.println(System.currentTimeMillis() - lastSended);
                    if ((System.currentTimeMillis() - lastSended) > 5000) {
                        synchronized (allHistory) {
                            if (allHistory.size() > bookmark) {
                                history.addAll(bookmark, allHistory);
                                System.out.println(history);
                                bookmark = allHistory.size();
                            }
                            if (history.size()>0){
                                for (String message: history){
                                    out.println(message);
                                }
                                history.clear();
                            }
                            lastSended = System.currentTimeMillis();
                        }
                    }

                }
            } catch (Exception e) {
                System.out.println("Ошибка в:" + clientSocket);
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


    public static void main(String[] args) throws IOException {
        LinkedList<String> allHistory = new LinkedList<>();
        System.out.println("Сервер запущен...");
        MailServer server = new MailServer();
        server.start(5555, allHistory);

    }
}
