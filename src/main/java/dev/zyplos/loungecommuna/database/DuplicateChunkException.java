package dev.zyplos.loungecommuna.database;

public class DuplicateChunkException extends Exception {
    public DuplicateChunkException(String errorMessage) {
        super(errorMessage);
    }
}
