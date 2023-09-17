package org.example;

import org.example.domain.Bot;
import org.example.domain.Field;
import org.example.domain.Player;

import java.util.Scanner;

public class Game implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private boolean endgame = false;

    /**
     * Starts the game
     */
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
        endgame(first,second);
    }

    /**
     * End of the game
     * @param first first {@link Field}
     * @param second second {@link Field}
     */
    private void endgame(Player first, Player second) {
        System.out.println("Game Over!");
        String winner;
        if (first.getField().getFleet().size()==0){
            winner  = second.getName();
        }
        else if(second.getField().getFleet().size()==0){
            winner  = first.getName();
        }else {throw new RuntimeException();}
        System.out.println("Winner: "+winner);
    }

    /**
     * Small message signalizing game start
     */
    private void announceGameStart() {
        System.out.println("""
                                
                                
                                
                                
                Time to start the game!
                """);
    }

    private boolean makeMove(Player first, Player second) {
        //todo: find out and declare a winner
        return first.shootInit(second.getField(),scanner) || second.shootInit(first.getField(),scanner);
    }


    /**
     * Checking what kind of game mode is chosen
     * @return true if single-player, false if 2 players
     */
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

    /**
     * @param i order of a player (first or second)
     * @param isSinglePlayer true if single-player, false if 2 players
     * @return initialized  {@link Player}.
     */
    //todo: custom input exceptions
    private Player initPlayer(int i, boolean isSinglePlayer) {
        boolean isAutoPlaced;
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
            return new Player(playerName, isAutoPlaced, false, Field.initField());
        }
        else return new Bot("Bot", true, true, Field.initField());

    }
}
