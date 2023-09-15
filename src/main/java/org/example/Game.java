package org.example;

import org.example.domain.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private boolean endgame = false;

    @Override
    public void run() {
        boolean isSinglePlayer = gameModeCheck();
        Player first = initPlayer(1, false);
        Player second = initPlayer(2, isSinglePlayer);
        System.out.println(first);
        System.out.println(second);

        first.placeFleet();
        second.placeFleet();
        announceGameStart();
        while (!endgame) {
            endgame = makeMove(first, second);
        }
    }

    private void announceGameStart() {
        System.out.println("""
                                
                                
                                
                                
                Time to start the game!
                """);
    }

    private boolean makeMove(Player first, Player second) {
        //todo: find out and declare a winner
        return shoot(first, second.getField()) || shoot(second, first.getField());
    }

    private boolean shoot(Player player, Field enemy) {
        player.getField().drawBattlefield(enemy.getField());
        System.out.printf("Player %s, please, shoot your shot: ", player.getName());
        String shootPosition = scanner.nextLine().toUpperCase();
        Square[][] enemyField = enemy.getField();
        int shotX = shootPosition.charAt(0) - 65;
        int shotY = shootPosition.charAt(1) - '0';
        switch (enemyField[shotX][shotY].getStatus()) {
            case CLOSED -> {
                System.out.println("Oops! You Missed");
                enemyField[shotX][shotY].setStatus(SquareStatus.REVEALED);
            }
            case SHIP -> {
                System.out.println("Nice Shot!");
                enemyField[shotX][shotY].setStatus(SquareStatus.SHOT);
                enemy.setFleetHealth(enemy.getFleetHealth() - 1);
                checkForDestroyedShips(enemy, enemyField[shotX][shotY]);
                shoot(player, enemy);
            }

            default -> {
                System.out.println("You can't shoot there, try again!");
                shoot(player, enemy);
            }

        }
        return enemy.getFleetHealth() == 0;
    }

    private void checkForDestroyedShips(Field enemy, Square square) {
        //todo: add reveal of bordering waters in case of ship destruction
        List<Ship> fleet = enemy.getFleet();
        for (Ship ship :
                fleet) {
            if (ship.getSquares().contains(square)) {
                ship.setSquares(ship.getSquares()
                        .stream()
                        .filter(item -> !item.equals(square))
                        .collect(Collectors.toList()));
            if (ship.getSquares().size()==0){
                System.out.println(ship.name()+" has been destroyed!");
            }
            }

        }


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
        boolean isAutoPlaced = true;
        String playerName;
        if (!isSinglePlayer) {
            System.out.printf("Player %d, please, input your name:%n", i);
            playerName = scanner.nextLine();
            System.out.printf("Player %s, do you want your ships to be auto-placed(y/n)?", playerName);
            String answer = scanner.nextLine();
            if (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")) {
                isAutoPlaced = answer.equalsIgnoreCase("y");
            } else {
                throw new ArithmeticException();
            }
        } else playerName = "Bot";
        return new Player(playerName, isAutoPlaced, isSinglePlayer, Field.initField());
    }
}
