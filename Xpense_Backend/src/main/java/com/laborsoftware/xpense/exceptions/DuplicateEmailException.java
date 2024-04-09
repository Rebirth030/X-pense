package com.laborsoftware.xpense.exceptions;

//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateEmailException extends Exception  {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
