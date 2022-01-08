package com.givaudan.galaxy.exception;

import javax.faces.FacesException;

public class NoEntityException extends FacesException {
    
    public NoEntityException() {
        super();
    }
    
    public NoEntityException(String message) {
        super(message);
    }
}
