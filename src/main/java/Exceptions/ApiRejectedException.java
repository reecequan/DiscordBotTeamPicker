package Exceptions;

public class ApiRejectedException extends Exception {

    public ApiRejectedException() {
    }

    public ApiRejectedException(String message) {
        super(message);
    }

}
