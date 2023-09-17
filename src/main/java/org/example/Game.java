package org.example;

import org.example.domain.Bot;
import org.example.domain.Field;
import org.example.domain.Player;
import org.example.exceptions.WrongCommandException;

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
        shipPlacementInfo();

        first.placeFleet();
        second.placeFleet();
        announceGameStart();
        while (!endgame) {
            endgame = makeMove(first, second);
        }
        endgame(first,second);
    }

    /**
     * Print info about fleet placement
     */
    private void shipPlacementInfo() {
        System.out.println("""
              To place your ships write two coordinates on the
              distance of the size of current ship.
              two coordinates should be separated with a space.
              Speedboats should be initialized with one coordinate repeated twice""");}

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
                Write name of the square you want to shoot
                """);
    }

    private boolean makeMove(Player first, Player second) {
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
            throw new WrongCommandException(playersAmount);
        }
    }

    /**
     * @param i order of a player (first or second)
     * @param isSinglePlayer true if single-player, false if 2 players
     * @return initialized  {@link Player}.
     */
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
                throw new WrongCommandException(answer);
            }
            return new Player(playerName, isAutoPlaced, false, Field.initField());
        }
        else return new Bot("Bot", true, true, Field.initField());

    }
}
