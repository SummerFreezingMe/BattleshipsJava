package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Field {
    private Square[][] field;

    private static final int FIELD_SIZE = 10;


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
