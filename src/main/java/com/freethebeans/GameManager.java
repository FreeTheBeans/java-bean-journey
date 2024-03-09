package com.freethebeans;

import java.util.Scanner;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameManager {
    private final String SERVER_ENDPOINT = "http://localhost";
    private final int SERVER_PORT = 31415;
    private Scanner scanner;

    GameManager() {
        scanner = new Scanner(System.in);
    }

    public void runGame() {
        String request = String.format("%s:%d/api/ping", SERVER_ENDPOINT, SERVER_PORT);
        String response = new RestTemplate().getForObject(request, String.class);
        JSONObject responseJSON = new JSONObject(response);
        System.out.println("Response from Server: " + responseJSON.getString("message"));

        String currentStateID = "dummyState";

        System.out.println("Welcome to Bean Escape Simulator!");
        System.out.println("Press enter to begin");
        scanner.nextLine();

        while (true) {
            GameState currentState = getGameState(currentStateID);
            String[] currentStateOptions = currentState.getOptions();
            String[] currentStateTransitions = currentState.getTransitions();
            System.out.println(currentState.getContext());

            for (int i = 0; i < currentStateOptions.length; i++) {
                System.out.println((i + 1) + ") " + currentStateOptions[i]);
            }

            System.out.println("Select your next move:");
            System.out.print("> ");
            String input = scanner.nextLine();

            if (!input.equals("q")) {
                int choiceNumber = Integer.parseInt(input);
                currentStateID = currentStateTransitions[choiceNumber - 1];
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
