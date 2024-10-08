package org.example.exceptions;

public class InvalidPinCode extends RuntimeException{
    public InvalidPinCode() {
        super("Pin code is wrong");
    }
}
