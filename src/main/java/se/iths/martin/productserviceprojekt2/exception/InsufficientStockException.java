package se.iths.martin.productserviceprojekt2.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productName, int requested, int available) {
        super("Insufficient stock for product '" + productName + "': requested " + requested + ", available " + available);
    }
}
