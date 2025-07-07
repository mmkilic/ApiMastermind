package com.mmkilic.game.entity;

public record ResponseGuess (int attempts, String guess, boolean win, int positive, int negative) {}
