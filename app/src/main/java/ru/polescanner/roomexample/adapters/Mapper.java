package ru.polescanner.roomexample.adapters;

public interface Mapper <I, O> {
    O mapTo(I i);

    I mapFrom(O o);
}