
import java.util.Random;
import java.util.SplittableRandom;
import java.util.Scanner;
import java.lang.Math;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

	public static void main(String[] args) {
		new Mexico().program();
	}

	final SplittableRandom rand = new SplittableRandom();
	final Scanner sc = new Scanner(in);
	final int maxRolls = 3; // No player may exceed this
	final int startAmount = 3; // Money for a player. Select any
	final int mexico = 1000; // A value greater than any other
	int roundMaxRolls = maxRolls;

	void program() {
		Random rand = new Random();
		// test(); // <----------------- UNCOMMENT to test

		int pot = 0; // What the winner will get
		Player[] players; // The players (array of Player objects)
		Player current; // Current player for round
		Player leader; // Player starting the round

		players = getPlayers();
		current = getRandomPlayer(players);
		leader = current;

		out.println("Mexico Game Started");
		statusMsg(players);

		// while (players.length > 1) { // Game over when only one player left

		// ----- In ----------
		String cmd = getPlayerChoice(current);
		if ("r".equals(cmd)) {

			// --- Process ------

			// ---- Out --------
			roundMsg(current);

		} else if ("n".equals(cmd)) {
			// Process
		} else {
			out.println("?");
		}

		// if ( round finished) {
		// --- Process -----

		// ----- Out --------------------
		out.println("Round done ... lost!");
		out.println("Next to roll is " + current.name);

		statusMsg(players);
		// }
		// }
		out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
	}

	// ---- Game logic methods --------------

	// TODO implement and test methods (one at the time)

	int indexOf(Player[] players, Player player) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == player) {
				return i;
			}
		}
		return -1;
	}

	void rollDice(Player currentPlayer) {
		if (currentPlayer.nRolls >= roundMaxRolls) { return;}

		currentPlayer.fstDice = rand.nextInt(5)+1;
		currentPlayer.secDice = rand.nextInt(5)+1;
		currentPlayer.nRolls++;
	}

	int getScore(Player player) {
		if (player.fstDice == 2 && player.secDice == 1) {
			return mexico;
		}

		if (player.fstDice == player.secDice) {
			return player.fstDice*100;
		}

		int larger = Math.max(player.fstDice, player.secDice);
		int smaller = Math.min(player.fstDice, player.secDice);

		return larger*10 + smaller;
	}

	Player getLoser(Player[] playerArray) {
		int lowestScore = mexico;
		int index = -1;

		for (int i = 0; i < playerArray.length; i++) {
			int score = getScore(playerArray[i]);

			if (score < lowestScore) {
				index = i;
			}


		}
	}

	Player getRandomPlayer(Player[] players) {
		return players[rand.nextInt(players.length)];
	}

	// ---------- IO methods (nothing to do here) -----------------------

	Player[] getPlayers() {
		// Ugly for now. If using a constructor this may
		// be cleaned up.
		Player[] players = new Player[3];
		Player p1 = new Player();
		p1.name = "Olle";
		p1.amount = startAmount;
		Player p2 = new Player();
		p2.name = "Fia";
		p2.amount = startAmount;
		Player p3 = new Player();
		p3.name = "Lisa";
		p3.amount = startAmount;
		players[0] = p1;
		players[1] = p2;
		players[2] = p3;
		return players;
	}

	void statusMsg(Player[] players) {
		out.print("Status: ");
		for (int i = 0; i < players.length; i++) {
			out.print(players[i].name + " " + players[i].amount + " ");
		}
		out.println();
	}

	void roundMsg(Player current) {
		out.println(current.name + " got " + current.fstDice + " and " + current.secDice);
	}

	String getPlayerChoice(Player player) {
		out.print("Player is " + player.name + " > ");
		return sc.nextLine();
	}

	// Possibly useful utility during development
	String toString(Player p) {
		return p.name + ", " + p.amount + ", " + p.fstDice + ", " + p.secDice + ", " + p.nRolls;
	}

	// Class for a player
	class Player {
		String name;
		int amount; // Start amount (money)
		int fstDice; // Result of first dice
		int secDice; // Result of second dice
		int nRolls; // Current number of rolls
	}

	/**************************************************
	 * Testing
	 *
	 * Test are logical expressions that should evaluate to true (and then be
	 * written out) No testing of IO methods Uncomment in program() to run test
	 * (only)
	 ***************************************************/
	void test() {
		// A few hard coded player to use for test
		// NOTE: Possible to debug tests from here, very efficient!
		Player[] ps = { new Player(), new Player(), new Player() };
		ps[0].fstDice = 2;
		ps[0].secDice = 6;
		ps[1].fstDice = 6;
		ps[1].secDice = 5;
		ps[2].fstDice = 1;
		ps[2].secDice = 1;

		// out.println(getScore(ps[0]) == 62);
		// out.println(getScore(ps[1]) == 65);
		// out.println(next(ps, ps[0]) == ps[1]);
		// out.println(getLoser(ps) == ps[0]);

		exit(0);
	}

}
