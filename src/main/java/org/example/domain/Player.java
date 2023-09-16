package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class Player {
    private String name;
    private boolean isAutoFill;
    private boolean isBot;

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

    public boolean shoot(Field enemy, Scanner scanner) {
        this.getField().drawBattlefield(enemy.getField());
        System.out.printf("Player %s, please, shoot your shot: ", this.getName());
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
                shoot(enemy,scanner);
            }

            default -> {
                System.out.println("You can't shoot there, try again!");
                shoot(enemy,scanner);
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

        }}

    public void placeFleet() {
        //todo: print out some rules
        Scanner sc = new Scanner(System.in);
        List<Ship> fleet = Ship.listOfShips();
        field.setFleet(fleet);
        int fleetHealth = 0;

        for (Ship ship : fleet) {
            boolean placed = false;
            while (!placed) {
                if (isAutoFill) {
                    placed = autoPlacement(ship);
                } else {
                    placed = placeShip(ship, sc);
                }
                fleetHealth += ship.getLength();
            }
            field.setFleetHealth(fleetHealth);
            field.drawBoard(this.field.getField(), false);
        }
    }

    private boolean placeShip(Ship ship, Scanner sc) {
        //todo: stringbuilder, perhaps enum toString
        System.out.println(this.name + ", please, locate your " + ship.name() + " | length: " + ship.getLength());
        String location = sc.nextLine().toUpperCase();
        return placementAttempt(ship, location);
    }

    private boolean placementAttempt(Ship ship, String location) {
        String[] squares = location.split(" ");
        Square leftSquare = field.getField()[squares[0].charAt(0) - 65]
                [squares[0].charAt(1) - '0'];
        Square rightSquare = field.getField()[squares[1].charAt(0) - 65]
                [squares[1].charAt(1) - '0'];
        List<Square> shipSquares = new ArrayList<>();
        if (leftSquare.getX() == rightSquare.getX() &&
                Math.abs(leftSquare.getY() - rightSquare.getY()) == ship.getLength() - 1) {
            int maxY = Math.max(leftSquare.getY(), rightSquare.getY());
            int minY = Math.min(leftSquare.getY(), rightSquare.getY());
            for (int g = minY; g < maxY + 1; g++) {
                if (collisionCheck(leftSquare.getX(), g)) {
                    System.out.println("Uh oh, you can't put it there!");
                    return false;
                }
            }
            for (int g = minY; g < maxY + 1; g++) {
                field.getField()[leftSquare.getX()][g].setStatus(SquareStatus.SHIP);
            }

        } else if (leftSquare.getY() == rightSquare.getY() &&
                Math.abs(leftSquare.getX() - rightSquare.getX()) == ship.getLength() - 1) {
            int maxX = Math.max(leftSquare.getX(), rightSquare.getX());
            int minX = Math.min(leftSquare.getX(), rightSquare.getX());
            for (int g = minX; g < maxX + 1; g++) {
                if (collisionCheck(g, leftSquare.getY())) {
                    System.out.println("Uh oh, you can't put it there!");
                    return false;
                }
            }
            for (int g = minX; g < maxX + 1; g++) {
                field.getField()[g][leftSquare.getY()].setStatus(SquareStatus.SHIP);
                shipSquares.add(field.getField()[g][leftSquare.getY()]);
            }
        } else {
            throw new RuntimeException();
        }
        ship.setSquares(shipSquares);
        return true;
    }

    private boolean collisionCheck(int x, int y) {
        Square[][] field = getField().getField();
        //todo: rework
        if (x == 0) {
            if (y == 0) {
                return field[0][1].getStatus() == SquareStatus.SHIP || field[1][1].getStatus() == SquareStatus.SHIP ||
                        field[1][0].getStatus() == SquareStatus.SHIP;
            } else if (y == Field.FIELD_SIZE - 1) {
                return field[0][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[1][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[1][Field.FIELD_SIZE - 1].getStatus() == SquareStatus.SHIP;

            } else {
                return field[0][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[1][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[1][y].getStatus() == SquareStatus.SHIP ||
                        field[1][y + 1].getStatus() == SquareStatus.SHIP ||
                        field[0][y + 1].getStatus() == SquareStatus.SHIP;
            }
        } else if (x == Field.FIELD_SIZE - 1) {
            if (y == 0) {
                return field[Field.FIELD_SIZE - 1][1].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][1].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 1][0].getStatus() == SquareStatus.SHIP;
            } else if (y == Field.FIELD_SIZE - 1) {
                return field[Field.FIELD_SIZE - 1][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][Field.FIELD_SIZE - 1].getStatus() == SquareStatus.SHIP;
            } else {
                return field[Field.FIELD_SIZE - 1][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][y].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 2][y + 1].getStatus() == SquareStatus.SHIP ||
                        field[Field.FIELD_SIZE - 1][y + 1].getStatus() == SquareStatus.SHIP;
            }
        } else {
            if (y == 0) {
                return field[x - 1][0].getStatus() == SquareStatus.SHIP ||
                        field[x - 1][1].getStatus() == SquareStatus.SHIP ||
                        field[x][1].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][1].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][0].getStatus() == SquareStatus.SHIP;
            } else if (y == Field.FIELD_SIZE - 1) {
                return field[x - 1][Field.FIELD_SIZE - 1].getStatus() == SquareStatus.SHIP ||
                        field[x - 1][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[x][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][Field.FIELD_SIZE - 2].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][Field.FIELD_SIZE - 1].getStatus() == SquareStatus.SHIP;
            } else {
                return field[x - 1][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[x][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][y - 1].getStatus() == SquareStatus.SHIP ||
                        field[x - 1][y].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][y].getStatus() == SquareStatus.SHIP ||
                        field[x - 1][y + 1].getStatus() == SquareStatus.SHIP ||
                        field[x][y + 1].getStatus() == SquareStatus.SHIP ||
                        field[x + 1][y + 1].getStatus() == SquareStatus.SHIP;
            }
        }
    }

    public boolean autoPlacement(Ship ship) {
        Random r = new Random();
        int widestPosition = ship.getLength() - 1;
        boolean isHorizontal = r.nextDouble() > 0.5;
        boolean isAscending = r.nextDouble() > 0.5;
        int xStart, yStart, xEnd, yEnd;
        if (isHorizontal) {
            xStart = r.nextInt(widestPosition, Field.FIELD_SIZE - widestPosition) + 65;
            yStart = r.nextInt(Field.FIELD_SIZE);

            xEnd = isAscending ? xStart + widestPosition : xStart - widestPosition;
            yEnd = yStart;
        } else {
            xStart = r.nextInt(Field.FIELD_SIZE) + 65;
            yStart = r.nextInt(widestPosition, Field.FIELD_SIZE - widestPosition);

            xEnd = xStart;
            yEnd = isAscending ? yStart + widestPosition : yStart - widestPosition;
        }
        //todo: string builder
        String placementCommand = String.valueOf((char) xStart) + yStart + " " + ((char) xEnd) + yEnd;
        return placementAttempt(ship, placementCommand);
    }
}
