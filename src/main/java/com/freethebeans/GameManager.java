package com.freethebeans;

import java.util.ArrayList;
import java.util.List;
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
    final Scanner scanner;
    final RestTemplate restTemplate;

    public GameManager(Scanner scanner, RestTemplate restTemplate) {
        this.scanner = scanner;
        this.restTemplate = restTemplate;
        // scanner = new Scanner(System.in);
        // restTemplate = new RestTemplate();
    }

    public void runGame() throws Exception {
        // check for connection to server - expect 'pong'
        pingPong();

        String currentStateID = "dummyState";

        System.out.println("-= PRESS ENTER =-");
        scanner.nextLine();

        while (true) {
            GameState currentState = getGameState(currentStateID);
            List<String> currentStateOptions = currentState.getOptions();
            List<String> currentStateTransitions = currentState.getTransitions();
            System.out.println('\n' + currentState.getContext() + '\n');

            for (int i = 0; i < currentStateOptions.size(); i++) {
                System.out.println((i + 1) + ") " + currentStateOptions.get(i));
            }

            // System.out.println("Select your next move:");
            System.out.print("> ");
            String input = scanner.nextLine();

            if (!input.equals("q")) {
                int choiceNumber = Integer.parseInt(input);
                currentStateID = currentStateTransitions.get(choiceNumber-1);
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

    public void pingPong() throws Exception {
        try {
            String request = String.format("%s:%d/api/ping", SERVER_ENDPOINT, SERVER_PORT);
            String response = restTemplate.getForObject(request, String.class);
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
    public GameState getGameState(String stateName) {
        String context = "";
        List<String> stateOptions = new ArrayList<>();
        List<String> stateTransitions = new ArrayList<>();
        try {
            String request = String.format("%s:%d/api/state/%s", SERVER_ENDPOINT, SERVER_PORT, stateName);
            String response = restTemplate.getForObject(request, String.class);

            JSONObject responseJSON = new JSONObject(response);
            String message = responseJSON.getString("message");

            JSONObject innerJSON = new JSONObject(message);
            context = innerJSON.getString("context");
            JSONArray optionsArray = innerJSON.getJSONArray("options");
            JSONArray transitionsArray = innerJSON.getJSONArray("transitions");

            int optionsLength = optionsArray.length();
            

            for (int i = 0; i < optionsLength; i++) {
                stateOptions.add(optionsArray.getString(i));
                stateTransitions.add( transitionsArray.getString(i));
            }
        } catch (Exception e) {
            System.err.println("Error: error getting state information.");
            e.printStackTrace();
        }
        return new GameState(context, stateOptions, stateTransitions);
    }

}
