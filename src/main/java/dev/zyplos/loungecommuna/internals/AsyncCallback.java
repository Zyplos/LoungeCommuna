package dev.zyplos.loungecommuna.internals;

public interface AsyncCallback<T> {
    public void thenDo(T result);
}
