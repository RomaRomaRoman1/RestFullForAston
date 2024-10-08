package org.example.exceptions;

import java.util.UUID;

public class ClientNotFound extends RuntimeException{
    public ClientNotFound(UUID clientID) {
        super(clientID + " not found");
    }
}
