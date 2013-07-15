package br.com.oncast.dojo.secretguessgame.dice;

import java.util.Random;

public class RandomSixFacesDice implements Dice {

	private static final int MAX_DICE_VALUE = 6;

	private static final int MIN_DICE_VALUE = 1;

	private static final Random RANDOM = new Random();

	public int roll() {
		return generateRandomIntegerInRange(MIN_DICE_VALUE, MAX_DICE_VALUE);
	}

	private int generateRandomIntegerInRange(final int min, final int max) {
		return RANDOM.nextInt(max - min) + min;
	}

}
