package MailServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class CapitalizeClient {
    public static void main(String[] args) throws Exception {


        try (var socket = new Socket("127.0.0.1", 5555)) {
            System.out.println("Введите строки текста, затем Ctrl + D или Ctrl + C, чтобы выйти");
            var scanner = new Scanner(System.in);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            while (true) {
                if (scanner.hasNext()) {
                    out.println(scanner.nextLine());
                }
                if (in.hasNext()) {
                    System.out.println(in.nextLine());
                }
            }
        }
    }
}
