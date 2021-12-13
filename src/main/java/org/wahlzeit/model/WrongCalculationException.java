package org.wahlzeit.model;


public class WrongCalculationException extends Exception {
    public WrongCalculationException() {
        super("Something went wrong with the calculation.");
    }
    public WrongCalculationException(String message) {
        super(message);
    }
}
