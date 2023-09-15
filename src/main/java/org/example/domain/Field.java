package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
public class Field {
    private final Square[][] field;

    public static final int FIELD_SIZE = 10;
    @Setter
    private int fleetHealth;
    @Setter
    private List<Ship> fleet;

    public Field(Square[][] field) {
        this.field = field;
    }

    public static Field initField() {
        Square[][] field = new Square[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = new Square(i, j, SquareStatus.CLOSED);
            }
        }
        return new Field(field);
    }

    public void drawBattlefield(Square[][] enemy) {
        drawBoard(enemy, true);
        System.out.println("=====================================");
        drawBoard(this.field, false);
    }

    public void drawBoard(Square[][] board, boolean isEnemy) {
        System.out.println("--------------------------------");
        char ch = 'A';
        for (int j = 0; j < FIELD_SIZE + 1; j++) {
            for (int i = 0; i < FIELD_SIZE + 1; i++) {
                if (i == FIELD_SIZE) {
                    System.out.print(" " + j + " ");
                    continue;
                }
                if (j == 10) {
                    System.out.print(" " + (ch++) +" ");
                } else {
                    if (board[i][j].getStatus() == SquareStatus.CLOSED) {
                        System.out.print(" - ");
                    } else if (board[i][j].getStatus() == SquareStatus.REVEALED) {
                        System.out.print(" o ");
                    } else if (board[i][j].getStatus() == SquareStatus.SHOT) {
                        System.out.print(" x ");
                    } else if (board[i][j].getStatus() == SquareStatus.SHIP) {
                        System.out.print(" " + (isEnemy ? "o" : "+") + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }
}
