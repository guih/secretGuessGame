package br.com.oncast.dojo.petals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.oncast.dojo.secretguessgame.dice.Dice;
import br.com.oncast.dojo.secretguessgame.display.SecretGuessGameDisplay;
import br.com.oncast.dojo.secretguessgame.game.SecretGuessGame;
import br.com.oncast.dojo.secretguessgame.secret.Secret;

import static org.junit.Assert.assertEquals;

public class GameTest {

	@Mock
	private SecretGuessGameDisplay display;

	@Mock
	private Dice diceValueProvider;

	@Mock
	private Secret secret;

	private SecretGuessGame game;

	private int numberOfDices;

	private int consecutiveCorrectGuessesToWinTheGame;

	private int correctSecret;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		numberOfDices = 5;
		consecutiveCorrectGuessesToWinTheGame = 6;
		correctSecret = 8;
		game = new SecretGuessGame(numberOfDices, consecutiveCorrectGuessesToWinTheGame, display, diceValueProvider, secret);
		Mockito.when(secret.getSecret(Mockito.anyListOf(Integer.class))).thenReturn(correctSecret);
	}

	@Test
	public void shouldRowFiveDices() throws Exception {
		game.start();
		List<Integer> displayedDices = capturedisplayedDicesValues();
		assertEquals(numberOfDices, displayedDices.size());
	}

	@Test
	public void shouldRowDicesAccordingToDiceValueProvider() throws Exception {
		int providedDiceValue = 10;
		Mockito.when(diceValueProvider.roll()).thenReturn(providedDiceValue);

		game.start();
		List<Integer> displayedDices = capturedisplayedDicesValues();

		assertEquals(numberOfDices, displayedDices.size());
		for (int diceValue : displayedDices) {
			assertEquals(providedDiceValue, diceValue);
		}
	}

	@Test
	public void shouldDisplayThatTheGuessIsCorrectAndDisplayTheDicesAgainWhenTheGuessIsCorrect() throws Exception {
		game.start();
		game.guessSecret(correctSecret);

		Mockito.verify(display).correctGuess();
		Mockito.verify(display, Mockito.times(2)).newDicesValues(Mockito.anyListOf(Integer.class));
	}

	@Test
	public void shouldAlwaysRollAndDisplayNewDiceValuesWhenTheUsersGuessesTheSecret() throws Exception {
		game.start();
		int timesThatDicesWereRolled = 1;
		for (int i = 0; i < 12; i++) {
			game.guessSecret(i);
			Mockito.verify(display, Mockito.times(++timesThatDicesWereRolled)).newDicesValues(Mockito.anyListOf(Integer.class));
		}
	}

	@Test
	public void shouldShowTheRightSecretResultWhenTheUserGuessTheWrongResult() throws Exception {
		game.start();
		game.guessSecret(0);
		Mockito.verify(display).wrongGuess(correctSecret);
	}

	@Test
	public void shouldShowWonTheGameAfterSixConsecutiveCorrectGuessesAndShouldNotDisplayNewDicesValuesAnymore() throws Exception {
		game.start();
		for (int i = 0; i < consecutiveCorrectGuessesToWinTheGame; i++) {
			Mockito.verify(display, Mockito.never()).wonTheGame();
			game.guessSecret(correctSecret);
		}
		Mockito.verify(display).wonTheGame();
		Mockito.verify(display, Mockito.times(consecutiveCorrectGuessesToWinTheGame)).newDicesValues(Mockito.anyListOf(Integer.class));
	}

	@Test
	public void restartingTheGameShouldResetConsecutiveCorrectGuessesCount() throws Exception {
		game.start();
		for (int i = 0; i < consecutiveCorrectGuessesToWinTheGame - 1; i++) {
			game.guessSecret(correctSecret);
		}
		game.start();
		game.guessSecret(correctSecret);
		Mockito.verify(display, Mockito.never()).wonTheGame();
	}

	@Test
	public void shouldResetConsecutiveCorrectGuessesCountWhenAWrongGuessIsGiven() throws Exception {
		game.start();
		for (int i = 0; i < consecutiveCorrectGuessesToWinTheGame - 1; i++) {
			game.guessSecret(correctSecret);
		}
		game.guessSecret(0);
		for (int i = 0; i < consecutiveCorrectGuessesToWinTheGame - 1; i++) {
			game.guessSecret(correctSecret);
			Mockito.verify(display, Mockito.never()).wonTheGame();
		}
		game.guessSecret(correctSecret);
		Mockito.verify(display).wonTheGame();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Integer> capturedisplayedDicesValues() {
		ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
		Mockito.verify(display).newDicesValues(captor.capture());
		return captor.getValue();
	}

}
