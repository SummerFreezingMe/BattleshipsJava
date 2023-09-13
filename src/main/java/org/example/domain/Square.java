package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Square {
    int x;
    int y;
    SquareStatus status;

}
