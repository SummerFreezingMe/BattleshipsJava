package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter

public enum Ship {
    BATTLESHIP(4, 1),
    CRUISER(3, 2),
    DESTROYER(2, 3),
    SPEEDBOAT(1, 4);
    private final int length;
    private final int amount;
    @Setter
    private List<Square> squares;

    Ship(int length, int amount) {
        this.length = length;
        this.amount = amount;

    }

    @Override
    public String toString() {
        return "Ship{" +
                "length=" + length +
                ", amount=" + amount +
                ", squares=" + squares +
                '}';
    }

    /**
     * Initialize list of ships that builds the fleet
     *
     * @return list of ships
     */
    public static List<Ship> listOfShips() {
        List<Ship> fleet = new ArrayList<>();
        for (Ship ship : Ship.class.getEnumConstants()) {
            for (int i = 0; i < ship.amount; i++) {
                fleet.add(ship);
            }
        }
        return fleet;
    }
}
