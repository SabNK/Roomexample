package ru.polescanner.roomexample.domain;

import java.util.List;

public interface Repository<E> {

    void add(E e);

    List<E> getAll();
}
