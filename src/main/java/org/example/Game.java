package org.example;

import org.example.domain.Field;
import org.example.domain.Player;
import org.example.domain.Square;
import org.example.domain.SquareStatus;

import java.util.Scanner;

public class Game implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private boolean endgame=false;

    @Override
    public void run() {
        boolean isSinglePlayer = gameModeCheck();
        Player first = initPlayer(1, false);
        Player second = initPlayer(2, isSinglePlayer);
        System.out.println(first);
        System.out.println(second);

        first.placeFleet();
        second.placeFleet();
        while (!endgame){
            endgame=makeMove(first,second);
        }
    }

    private boolean makeMove(Player first, Player second) {
        //todo: find out and declare a winner
        return shoot(first,second.getField())||shoot(second,first.getField());
    }

    private boolean shoot(Player player, Field enemy) {
        System.out.printf("Player %s, please, input your name: ", player.getName());
        String shootPosition = scanner.nextLine();
        Square[][] enemyField = enemy.getField();
        int shotX=shootPosition.charAt(0) - 65;
        int shotY =shootPosition.charAt(1);
        switch (enemyField[shotX][shotX].getStatus()){
            case CLOSED -> {
                System.out.println("Oops! You Missed");
                enemyField[shotX][shotY].setStatus(SquareStatus.REVEALED);
            }
            case SHIP ->{
                System.out.println("Nice Shot!");
            enemyField[shotX][shotY].setStatus(SquareStatus.SHOT);
            enemy.setFleetHealth(enemy.getFleetHealth()-1);
            shoot(player,enemy);
            }

            default -> {
                System.out.println("You can't shoot there, try again!");
                shoot(player,enemy);
            }

        }
        return enemy.getFleetHealth()==0;
    }

    private boolean gameModeCheck() {
        System.out.println("Choose player's amount (1/2):");
        int playersAmount = scanner.nextInt();
        scanner.nextLine();
        if (playersAmount == 2 || playersAmount == 1) {
            return playersAmount == 1;
        } else {
            throw new ArithmeticException();
        }
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
        return new Player(playerName, isSinglePlayer, autoPlaced,Field.initField());
    }
}
