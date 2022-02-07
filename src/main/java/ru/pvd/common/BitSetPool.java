package ru.pvd.common;

public interface BitSetPool {

    boolean setBitTrueIfFalse(long bitIndex);

    void clear();
}
