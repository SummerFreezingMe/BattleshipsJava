package org.example.domain;

import lombok.Getter;

@Getter
public enum Ship {
    BATTLESHIP(4,1),
    CRUISER(3,2),
    DESTROYER(2,3),
    SPEEDBOAT(1,4);
    private final int length;
    private final int amount;

    Ship(int length, int amount) {
        this.length=length;
        this.amount=amount;

    }
}
