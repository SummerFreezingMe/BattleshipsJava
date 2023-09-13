package org.example.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Square {
    int x;
    int y;
    SquareStatus status;

    enum SquareStatus {
        CLOSED,
        SHIP,
        REVEALED,
        SHOT
    }
}
