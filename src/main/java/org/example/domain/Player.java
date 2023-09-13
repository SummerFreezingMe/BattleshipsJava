package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@AllArgsConstructor
public class Player {
    private String name;
    private boolean isAutoFill;
    private boolean isBot;

    @Getter
    private Field field;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (isAutoFill != player.isAutoFill) return false;
        if (isBot != player.isBot) return false;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (isAutoFill ? 1 : 0);
        result = 31 * result + (isBot ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", isAutoFill=" + isAutoFill +
                ", isBot=" + isBot +
                '}';
    }

    public void placeFleet() {
        //todo: print out some rules
        Scanner sc = new Scanner(System.in);
        List<Ship> fleet = Ship.listOfShips();
        //todo: autogeneration
        for (Ship ship : fleet) {
            placeShip(ship, sc);
        }
        field.drawBoard(this.field.getField());
    }

    private void placeShip(Ship ship, Scanner sc) {
        //todo: stringbuilder, perhaps enum toString
        System.out.println(this.name + ", please, locate your " + ship.name() + " | length: " + ship.getLength());
        String location = sc.nextLine().toUpperCase();
        String[] squares = location.split(" ");
        //todo: collision check
        Square leftSquare = field.getField()[squares[0].charAt(0) - 65]
                [Character.getNumericValue(squares[0].charAt(1))];
        Square rightSquare = field.getField()[squares[1].charAt(0) - 65]
                [Character.getNumericValue(squares[1].charAt(1))];
        if (leftSquare.getX() == rightSquare.getX() &&
                Math.abs(leftSquare.getY() - rightSquare.getY()) == ship.getLength() - 1) {
            int maxY = Math.max(leftSquare.getY(), rightSquare.getY()) - 1;
            int minY = Math.min(leftSquare.getY(), rightSquare.getY()) - 1;
            for (int g = minY; g < maxY + 1; g++) {
                field.getField()[g][leftSquare.getY() - 1].setStatus(SquareStatus.SHIP);
            }

        } else if (leftSquare.getY() == rightSquare.getY() &&
                Math.abs(leftSquare.getX() - rightSquare.getX()) == ship.getLength() - 1) {
            int maxX = Math.max(leftSquare.getX(), rightSquare.getX());
            int minX = Math.min(leftSquare.getX(), rightSquare.getX());
            for (int g = minX; g < maxX + 1; g++) {
                field.getField()[leftSquare.getX() - 1][g].setStatus(SquareStatus.SHIP);
            }
        } else {
            throw new IllegalArgumentException();
        }
        ship.setLeft(leftSquare);
        ship.setRight(rightSquare);
    }
}
