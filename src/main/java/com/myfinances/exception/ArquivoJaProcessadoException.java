package com.myfinances.exception;

public class ArquivoJaProcessadoException extends RuntimeException {

    public ArquivoJaProcessadoException() {
        super("Este arquivo já foi processado.");
    }
}
