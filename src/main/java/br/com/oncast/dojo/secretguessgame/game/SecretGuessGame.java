package br.com.oncast.dojo.secretguessgame.game;

import java.util.ArrayList;

import br.com.oncast.dojo.secretguessgame.dice.Dice;
import br.com.oncast.dojo.secretguessgame.display.SecretGuessGameDisplay;
import br.com.oncast.dojo.secretguessgame.secret.Secret;

public class SecretGuessGame {

	private final SecretGuessGameDisplay display;

	private final int numberOfDices;

	private final Dice dice;

	private final Secret secret;

	private ArrayList<Integer> dicesValues;

	private final int consecutiveCorrectGuessesToWinTheGame;

	private int consecutiveCorrectGuesses;

	public SecretGuessGame(int numberOfDices, int consecutiveCorrectGuessesToWinTheGame, SecretGuessGameDisplay display, Dice dice, Secret secret) {
		this.numberOfDices = numberOfDices;
		this.consecutiveCorrectGuessesToWinTheGame = consecutiveCorrectGuessesToWinTheGame;
		this.display = display;
		this.dice = dice;
		this.secret = secret;
	}

	public void start() {
		this.consecutiveCorrectGuesses = 0;
		rollDices();
	}

	public boolean guessSecret(int guessedResult) {
		int correctSecret = secret.getSecret(dicesValues);
		boolean isCorrectGuess = correctSecret == guessedResult;

		if (isCorrectGuess) {
			display.correctGuess();
			consecutiveCorrectGuesses++;
		} else {
			display.wrongGuess(correctSecret);
			consecutiveCorrectGuesses = 0;
		}

		if (hasWon()) display.wonTheGame();
		else rollDices();

		return isCorrectGuess;
	}

	private boolean hasWon() {
		return consecutiveCorrectGuesses >= consecutiveCorrectGuessesToWinTheGame;
	}

	private void rollDices() {
		dicesValues = new ArrayList<Integer>();
		for (int i = 0; i < numberOfDices; i++) {
			dicesValues.add(dice.roll());
		}
		display.newDicesValues(dicesValues);
	}

}
