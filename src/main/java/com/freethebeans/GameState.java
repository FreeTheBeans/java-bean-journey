package com.freethebeans;

public class GameState {
    private String context;
    private String[] options;
    private int[] transitions;

    public GameState(String context, String[] options, int[] transitions) {
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

    public int[] getTransitions() {
        return transitions;
    }
}
