package org.example.exceptions;

public class WrongCommandException extends RuntimeException {

    public WrongCommandException(int command) {
        super("Incorrect option command: " + command);
    }

    public WrongCommandException(String command) {
        super("Incorrect option command: " + command);
    }
}
