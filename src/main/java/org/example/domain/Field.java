package org.example.domain;

import lombok.Getter;
import lombok.Setter;


@Getter
public class Field {
    private final Square[][] field;

    public static final int FIELD_SIZE = 10;
@Setter
    private int fleetHealth;

    public Field(Square[][] field) {
        this.field = field;
    }

    public static Field initField() {
        Square[][] field = new Square[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = new Square(i + 1, j + 1, SquareStatus.CLOSED);
            }
        }
        return new Field(field);
    }

    public void drawBoard(Square[][] board) {
        System.out.println("--------------------------------");
        char ch = 'A';
        for (int i = 0; i < FIELD_SIZE + 1; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (i == FIELD_SIZE) {
                    System.out.print(" " + (ch++) + " ");
                    continue;
                }
                if (board[i][j].getStatus() == SquareStatus.CLOSED) {
                    System.out.print(" - ");
                } else if (board[i][j].getStatus() == SquareStatus.REVEALED) {
                    System.out.print(" o ");
                } else if (board[i][j].getStatus() == SquareStatus.SHIP) {
                    System.out.print(" x ");
                }
                if (j == 9) {
                    System.out.print(" " +i);
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }
}
