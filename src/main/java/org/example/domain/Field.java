package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Field {
    private Square [][] field;

    private static final int FIELD_SIZE = 8;


    public static Field initField(){
        Square[][] field = new Square[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j]=new Square(i+1,j+1, SquareStatus.CLOSED);
            }
        }
        return new Field(field);
    }

}
