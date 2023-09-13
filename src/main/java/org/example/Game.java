package org.example;

import org.example.domain.Player;

import java.util.Scanner;

public class Game implements Runnable {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        boolean isSinglePlayer;
        System.out.println("Choose player's amount (1/2):");
        int playersAmount = scanner.nextInt();
        scanner.nextLine();
        if (playersAmount == 2 || playersAmount == 1) {
            isSinglePlayer = playersAmount == 1;
        } else {
            throw new ArithmeticException();
        }
        Player first = initPlayer(1, false);
        Player second = initPlayer(2, isSinglePlayer);
        System.out.println(first);
        System.out.println(second);

    }

    //todo: custom input exceptions
    private Player initPlayer(int i, boolean isSinglePlayer) {
        boolean autoPlaced = true;
        String playerName;
        if (!isSinglePlayer) {
            System.out.printf("Player %d, please, input your name:%n", i);
            playerName = scanner.nextLine();
            System.out.printf("Player %s, do you want your ships to be auto-placed(y/n)?", playerName);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
                autoPlaced = answer.equalsIgnoreCase("y");
            } else {
                throw new ArithmeticException();
            }
        } else playerName = "Bot";
        return new Player(playerName, isSinglePlayer, autoPlaced);
    }
}
