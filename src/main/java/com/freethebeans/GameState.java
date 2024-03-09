package com.freethebeans;

public class GameState {
    private String context;
    private String[] options;
    private String[] transitions;

    public GameState(String context, String[] options, String[] transitions) {
        this.context = context;
        this.options = options;
        this.transitions = transitions;
    }

    public String getContext() {
        return context;
    }

    public String[] getOptions() {
        return options;
    }

    public String[] getTransitions() {
        return transitions;
    }
}
