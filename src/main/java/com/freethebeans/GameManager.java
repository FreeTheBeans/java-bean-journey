package com.freethebeans;

import java.util.Scanner;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameManager {
    private final String SERVER_ENDPOINT = "http://bean.phipson.co.za";
    // private final String SERVER_ENDPOINT = "http://prod.bean.phipson.co.za";
    // for local testing
    // private final String SERVER_ENDPOINT = "http://localhost";
    private final int SERVER_PORT = 31415;
    private Scanner scanner;

    GameManager() {
        scanner = new Scanner(System.in);
    }

    public void runGame() throws Exception {
        // check for connection to server - expect 'pong'
        pingPong();

        String currentStateID = "dummyState";

        System.out.println("-= PRESS ENTER =-");
        scanner.nextLine();

        while (true) {
            GameState currentState = getGameState(currentStateID);
            String[] currentStateOptions = currentState.getOptions();
            String[] currentStateTransitions = currentState.getTransitions();
            System.out.println('\n' + currentState.getContext() + '\n');

            for (int i = 0; i < currentStateOptions.length; i++) {
                System.out.println((i + 1) + ") " + currentStateOptions[i]);
            }

            // System.out.println("Select your next move:");
            System.out.print("> ");
            String input = scanner.nextLine();


            if (input.equals("q")) {
                System.out.println("You have abandoned the bean brothers.");
                break;
            }

            try {
                int choiceNumber = Integer.parseInt(input);

                if (choiceNumber < currentStateOptions.length && choiceNumber > -1) {
                    currentStateID = currentStateTransitions[choiceNumber - 1];

                    // if (gameState.isEndState()) {
                    // System.out.println("Congratulations! You have escaped!");
                    // break;
                    // }

                } else {
                    System.out.println("You have to choose one of the given options you silly bean.");
                }

                    
            } catch (NumberFormatException e) {
                System.out.println("You have to enter a number you silly bean.");
            }
            
        }

        scanner.close();
    }

    private void pingPong() throws Exception {
        try {
            String request = String.format("%s:%d/api/ping", SERVER_ENDPOINT, SERVER_PORT);
            @SuppressWarnings("null")
            String response = new RestTemplate().getForObject(request, String.class);
            JSONObject responseJSON = new JSONObject(response);
            String res = responseJSON.getString("message");
            if (res.equals("pong")) {
                System.out.println(" connected!\n");
            } else {
                throw new Exception("Ping did not recieve a pong. Ping is lonely :'(");
            }
        } catch (Exception e) {
            System.err.println("\n\nPing did not recieve a pong. Ping is lonely :'(");
            throw e;
        }
    }

    @SuppressWarnings("null")
    private GameState getGameState(String stateName) {
        String context = "";
        String[] stateOptions = null;
        String[] stateTransitions = null;
        try {
            String request = String.format("%s:%d/api/state/%s", SERVER_ENDPOINT, SERVER_PORT, stateName);
            String response = new RestTemplate().getForObject(request, String.class);

            JSONObject responseJSON = new JSONObject(response);
            String message = responseJSON.getString("message");

            JSONObject innerJSON = new JSONObject(message);
            context = innerJSON.getString("context");
            JSONArray optionsArray = innerJSON.getJSONArray("options");
            JSONArray transitionsArray = innerJSON.getJSONArray("transitions");

            int optionsLength = optionsArray.length();
            stateOptions = new String[optionsLength];
            stateTransitions = new String[optionsLength];

            for (int i = 0; i < optionsLength; i++) {
                stateOptions[i] = optionsArray.getString(i);
                stateTransitions[i] = transitionsArray.getString(i);
            }
        } catch (Exception e) {
            System.err.println("Error: error getting state information.");
            e.printStackTrace();
        }
        return new GameState(context, stateOptions, stateTransitions);
    }

}
