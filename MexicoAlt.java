
import java.util.Random;
import java.util.SplittableRandom;
import java.util.Scanner;
import java.lang.Math;

import static java.lang.System.*;

public class MexicoAlt {

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

		while (players.length > 1) { // Game over when only one player left

			if (current.nRolls == 0) {
				rollDice(current);
				roundMsg(current);
			}

			// Play
			if ((roundMaxRolls != 1) && (current.nRolls < roundMaxRolls)) { // Only ask if reroll is allowed
				String cmd = getPlayerChoice(current); // Input
				if ("r".equals(cmd)) {
					rollDice(current);
					roundMsg(current);
				} else if ("n".equals(cmd)) {
					if (current == leader) {
						roundMaxRolls = current.nRolls;
					}
					current = next(players, current);
				} else {
					out.println("?");
				}
			} else {
				current = next(players, current);
			}

			// Round end
			if ((leader == current) && next(players, current).nRolls != 0) {

				Player loser = getLoser(players);
				loser.amount--;
				if (loser.amount == 0) {
					players = removePlayer(players, loser);
					current = next(players, current);
				} else {
					current = loser;
				}

				leader = current;
				clearRoundResults(players);

				// ----- Out --------------------
				out.println("Round done " + loser.name + " lost!");
				out.println("||");
				out.println("Next to roll is " + current.name);

				statusMsg(players);
			}
		}

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
		if (currentPlayer.nRolls >= roundMaxRolls) {return;}

		currentPlayer.fstDice = rand.nextInt(5)+1;
		currentPlayer.secDice = rand.nextInt(5)+1;
		currentPlayer.nRolls++;

		out.println("|");
		out.println("DEBUG: " + toString(currentPlayer));
	}

	int getScore(Player player) {

		int larger = Math.max(player.fstDice, player.secDice);
		int smaller = Math.min(player.fstDice, player.secDice);

		if (larger == 2 && smaller == 1) {
			return mexico;
		}

		if (larger == smaller) {
			return player.fstDice * 100;
		}

		return larger*10 + smaller;
	}

	Player getLoser(Player[] playerArray) {
		int lowestScore = mexico;
		int index = -1;

		for (int i = 0; i < playerArray.length; i++) {
			int score = getScore(playerArray[i]);

			if (score < lowestScore) {
				index = i;
				lowestScore = score;
			}
		}
		return playerArray[index];
	}

    Player[] removePlayer (Player[] ps, Player p) {
		Player[] remPs = new Player[ps.length - 1];
		for (int i = 0, k = 0; i < ps.length - 1; i++) {

			if (ps[i] != p) {
				remPs[k++] = ps[i];
			}
		}
		return remPs;
	}

	Player next(Player[] ps, Player current) {
		int currentIndex = (indexOf(ps, current) + 1);
		int nextIndex = currentIndex % (ps.length);
		return ps[nextIndex];
	}

	Player getRandomPlayer(Player[] players) {
		return players[rand.nextInt(players.length)];
	}
	void clearRoundResults(Player[] ps) {
		for (int i = 0; i < ps.length; i++) {
			ps[i].nRolls = 0;
			roundMaxRolls = 3;
		}
	}

	// ---------- IO methods (nothing to do here) -----------------------

	Player[] getPlayers() {
		// Ugly for now. If using a constructor this may
		// be cleaned up.
		Player[] players = new Player[3];
		Player p1 = new Player();
		p1.name = "Olle";
		p1.amount = startAmount;
		p1.nRolls = 0;
		Player p2 = new Player();
		p2.name = "Fia";
		p2.amount = startAmount;
		p2.nRolls = 0;
		Player p3 = new Player();
		p3.name = "Lisa";
		p3.amount = startAmount;
		p3.nRolls = 0;
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
		return p.name + ", a: " + p.amount + ", fst: " + p.fstDice + ", scd: " + p.secDice + ", n: " + p.nRolls + ", nMax: " + roundMaxRolls;
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
