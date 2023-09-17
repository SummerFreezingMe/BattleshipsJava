package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;


@Getter
public class Field {
    private final Square[][] field;

    public static final int FIELD_SIZE = 10;
    @Setter
    private int fleetHealth;
    @Setter
    private List<Ship> fleet;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field1 = (Field) o;

        if (getFleetHealth() != field1.getFleetHealth()) return false;
        if (!Arrays.deepEquals(getField(), field1.getField())) return false;
        return getFleet() != null ? getFleet().equals(field1.getFleet()) : field1.getFleet() == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(getField());
        result = 31 * result + getFleetHealth();
        result = 31 * result + (getFleet() != null ? getFleet().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "field=" + Arrays.toString(field) +
                ", fleetHealth=" + fleetHealth +
                ", fleet=" + fleet +
                '}';
    }

    public Field(Square[][] field) {
        this.field = field;
    }

    /**
     * Initialize {@link Field}
     * @return {@link Field} instance
     */
    public static Field initField() {
        Square[][] field = new Square[FIELD_SIZE][FIELD_SIZE];
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                field[i][j] = new Square(i, j, SquareStatus.CLOSED);
            }
        }
        return new Field(field);
    }

    /**
     * Draw the whole battlefield
     * @param enemy {@link Field} of the enemy
     */
    public void drawBattlefield(Square[][] enemy) {
        drawBoard(enemy, true);
        System.out.println("=====================================");
        drawBoard(this.field, false);
    }

    /**
     * Draw single board
     * @param board board to draw
     * @param isEnemy true is this field are of the enemy, false otherwise
     */
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
                        System.out.print(" " + (isEnemy ? "-" : "+") + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("--------------------------------");
    }
}
