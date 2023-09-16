package org.example.domain;

import java.util.Random;
import java.util.Scanner;

public class Bot extends Player{
    public Bot(String name, boolean isAutoFill, boolean isBot, Field field) {
        super(name, isAutoFill, isBot, field);
    }

    @Override
    public boolean shootInit(Field enemy, Scanner scanner) {
        Random r = new Random();
        int randomX  = r.nextInt(Field.FIELD_SIZE);
        int randomY  = r.nextInt(Field.FIELD_SIZE);
        return super.shoot(randomX,randomY,enemy,scanner);
    }
}
