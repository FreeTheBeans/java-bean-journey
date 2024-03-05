package com.freethebeans;

import java.util.Scanner;

// ==============
/**
 * This will serve as a guide to create the
 * GameManager with connections to the server
 * through the API. References here to "server"
 * are meant to be replaced.
 */
// ==============

public class GameManager {
    // private Server server;
    private Scanner scanner;

    GameManager() {
        // server = new Server();
        scanner = new Scanner(System.in);
    }

    public void runGame() {
        int currentState = 0;

        System.out.println("Welcome to Bean Escape Simulator!");
        System.out.println("Press enter to begin");
        scanner.nextLine();

        while (true) {
            // GameState gameState = server.getGameState(currentState);
            // System.out.println(gameState.getContext());

            // for (int i = 0; i < gameState.getOptions().length; i++) {
            // System.out.println((i + 1) + ") " + gameState.getOptions()[i]);
            // }

            System.out.println("Select your next move:");
            System.out.print("> ");
            String input = scanner.nextLine();

            if (!input.equals("q")) {
                int choice = Integer.parseInt(input);
                // int nextStateID = gameState.getTransitions()[choice - 1];
                // currentState = nextStateID;
                // if (gameState.isEndState()) {
                // System.out.println("Congratulations! You have escaped!");
                // break;
                // }
            } else {
                System.out.println("You have abandoned the bean brothers.");
                break;
            }
        }

        scanner.close();
    }
}
