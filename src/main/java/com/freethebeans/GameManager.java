package com.freethebeans;

import java.util.Scanner;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

public class GameManager {
    private final String SERVER_ENDPOINT = "http://bean.phipson.co.za";
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
        boolean error = false;
        boolean gameover = false;

        System.out.println("-= PRESS ENTER =-");
        scanner.nextLine();

        while (true) {
            GameState currentState = getGameState(currentStateID);

            if (currentState.toString().endsWith("Death")) {
                gameover = true;
                System.out.println("Y O U  L O S T :(\n");
                System.out.println("Welcome to bean heaven. I am your bean angel Gabeanriel. How would you like to proceed?");
                System.out.println("1) Be reincarnated as a bean in BBD's kitchen.");
                System.out.println("2) Forfeit your life and slowly sink into the abyss until nothing remains but the emptiness of what once was of your bean essence.");
            } else if (currentState.toString().endsWith("Escape")) {
                gameover = true;
                System.out.println("Y O U  W O N :)\n");
                System.out.println("... \n 69 years later \n ...");
                System.out.println("All is going well with your new bean life. You are safe from the terrors of BBD, and all is peaceful. Too peaceful. Sometimes you just want something to happen so that you can feel some excitement again. Like that time in BBD, when you navigated through those halls. You find yourself missing it more and more, and wishing that there is a way back. Well now there is. Time travel has just been made possible for beans while I was giving you this monologue. What do you want to do?");
                System.out.println("1) Go back to that fateful morning in BBD's kitchen to experience the thrill again.");
                System.out.println("2) Continue with your boring, mundane, uneventful life.");
            }

            String[] currentStateOptions = currentState.getOptions();
            String[] currentStateTransitions = currentState.getTransitions();

            if (!error && !gameover) {
                System.out.println('\n' + currentState.getContext() + '\n');

                for (int i = 0; i < currentStateOptions.length; i++) {
                    System.out.println((i + 1) + ") " + currentStateOptions[i]);
                }
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

                if (gameover) {
                    if (choiceNumber == 1) {
                        currentStateID = "startState";
                        gameover = false;
                        continue;
                    } else if (choiceNumber == 2) {
                        System.out.println("You have abandoned the bean brothers.");
                        break;
                    } else {
                        error = true;
                        System.out.println("You have to enter a number you silly bean.");
                    }
                } else if (choiceNumber > 0 && choiceNumber <= currentStateOptions.length) {
                    error = false;
                    currentStateID = currentStateTransitions[choiceNumber - 1];
                } else {
                    error = true;
                    System.out.println("You have to choose one of the given options you silly bean.");
                }

                    
            } catch (NumberFormatException e) {
                error = true;
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
