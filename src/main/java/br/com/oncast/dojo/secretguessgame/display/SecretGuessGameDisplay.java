package br.com.oncast.dojo.secretguessgame.display;

import java.util.List;

public interface SecretGuessGameDisplay {

	void newDicesValues(List<Integer> dicesValues);

	void correctGuess();

	void wrongGuess(int correctSecret);

	void wonTheGame();

}
