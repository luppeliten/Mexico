
import java.util.Random;
import java.util.SplittableRandom;
import java.util.Scanner;
import java.lang.Math;

import static java.lang.System.*;

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
    int numberOfRolledPlayers = 0;

    void program() {
        Random rand = new Random();
        // test();

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

            String cmd = getPlayerChoice(current);
            if ("r".equals(cmd)) {
                if (current.nRolls < roundMaxRolls) {
                    rollDice(current);
                    roundMsg(current);
                } else {
                    numberOfRolledPlayers++;
                    setRoundMaxRolls(current, leader, current.nRolls);
                    current = next(players, current);
                }
            } else if ("n".equals(cmd)) {
                if (!(current.nRolls <= 0)) {
                    numberOfRolledPlayers++;
                    setRoundMaxRolls(current, leader, current.nRolls);
                    current = next(players, current);
                } else {
                    System.out.println("Action is not allowed: must roll dices atleast once");
                }


            } else {
                out.println("?");
            }

            if (allRolled(players)) {

                Player loser = getLoser(players);
                loser.amount--;
                pot++;

                if (loser.amount == 0) {
                    players = removeLoser(players, loser);
                    current = next(players, current);
                } else {
                    current = loser;
                }

                clearRoundResults(players);
                numberOfRolledPlayers = 0;


                // ----- Out --------------------
                out.println("Round done " + loser.name + " lost!");

                if (players.length > 1) {
                    statusMsg(players);
                    out.println("Next to roll is " + current.name);
                }
            }


        }


        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }

    // ---- Game logic methods --------------


    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    void rollDice(Player currentPlayer) {
        if (currentPlayer.nRolls >= roundMaxRolls) {
            return;
        }

        currentPlayer.fstDice = rand.nextInt(5) + 1;
        currentPlayer.secDice = rand.nextInt(5) + 1;
        currentPlayer.nRolls++;
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

        return larger * 10 + smaller;
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

        // No player with the lowest score was found
        if (index == -1) {
            return null;
        }

        return playerArray[index];
    }

    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length)];
    }

    Player next(Player[] players, Player currentPlayer) {
        int currentPlayerId = indexOf(players, currentPlayer);
        int nextPlayerId = (currentPlayerId + 1) % players.length;
        return players[nextPlayerId];
    }

    void clearRoundResults(Player[] players) {
        roundMaxRolls = maxRolls;
        for (Player player : players) {
            player.nRolls = 0;
        }
    }

    Player[] removeLoser(Player[] players, Player loser) {
        int oldLength = players.length;
        int newLength = oldLength - 1;
        Player[] newPlayers = new Player[newLength];
        int newPlayersCursor = 0;

        for (int i = 0; i < oldLength; i++) {
            if (players[i] != loser) {
                newPlayers[newPlayersCursor] = players[i];
                newPlayersCursor++;
            }
        }

        return newPlayers;
    }

    boolean allRolled(Player[] players) {
        int numberOfPlayers = players.length;
        return numberOfRolledPlayers >= numberOfPlayers;
    }


    void setRoundMaxRolls(Player player, Player leader, int rolls) {
        if (player == leader) {
            roundMaxRolls = rolls;
        }
    }


    // ---------- IO methods (nothing to do here) -----------------------

    Player[] getPlayers() {
        Player p1 = new Player("Olle", startAmount);
        Player p2 = new Player("Fia", startAmount);
        Player p3 = new Player("Lisa", startAmount);
        return new Player[]{p1, p2, p3};
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

        Player(String nameC, int amountC) {
            name = nameC;
            amount = amountC;
        }
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
        Player[] ps = getPlayers();
        ps[0].fstDice = 2;
        ps[0].secDice = 6;
        ps[1].fstDice = 6;
        ps[1].secDice = 5;
        ps[2].fstDice = 1;
        ps[2].secDice = 1;

        out.println(getScore(ps[0]) == 62);
        out.println(getScore(ps[1]) == 65);
        out.println(next(ps, ps[0]) == ps[1]);
        out.println(getLoser(ps) == ps[0]);

        exit(0);
    }

}
