package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Square {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Square square = (Square) o;

        if (getX() != square.getX()) return false;
        if (getY() != square.getY()) return false;
        return getStatus() == square.getStatus();
    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }

    private int x;
    private int y;
    private SquareStatus status;

}
