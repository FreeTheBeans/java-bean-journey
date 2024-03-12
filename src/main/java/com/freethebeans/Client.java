package com.freethebeans;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Client implements CommandLineRunner {
    public static void main(String[] args) {
        printStartBanner();
        try {
            SpringApplication.run(Client.class, args);
            System.exit(0);
        } catch (Exception e) {
            System.err.println("\nError: " + e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        GameManager gameManager = new GameManager();
        gameManager.runGame();
    }

    static void printStartBanner() {
        String startBanner = """

                __        _______ _     ____ ___  __  __ _____
                \\ \\      / / ____| |   / ___/ _ \\|  \\/  | ____|
                 \\ \\ /\\ / /|  _| | |  | |  | | | | |\\/| |  _|
                  \\ V  V / | |___| |__| |__| |_| | |  | | |___
                   \\_/\\_/  |_____|_____\\____\\___/|_|  |_|_____|

                 _____ ___
                |_   _/ _ \\
                  | || | | |
                  | || |_| |
                  |_| \\___/

                 _____ ____  _____ _____ _____ _   _ _____ ____  _____    _    _   _
                |  ___|  _ \\| ____| ____|_   _| | | | ____| __ )| ____|  / \\  | \\ | |
                | |_  | |_) |  _| |  _|   | | | |_| |  _| |  _ \\|  _|   / _ \\ |  \\| |
                |  _| |  _ <| |___| |___  | | |  _  | |___| |_) | |___ / ___ \\| |\\  |
                |_|   |_| \\_\\_____|_____| |_| |_| |_|_____|____/|_____/_/   \\_\\_| \\_|


                               """;
        System.out.println(startBanner);
        System.out.print("Connecting to server...");
    }
}
