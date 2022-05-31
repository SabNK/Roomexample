package ru.polescanner.roomexample.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PoleCollection {
    private Map<UUID, Pole> collection;

    public PoleCollection(Map<UUID, Pole> collection) {
        this.collection = collection;
    }

    public PoleCollection() {
        this.collection = new HashMap<>();
    }

    public PoleCollection(Pole... p){
        this.collection = new HashMap<>();
        for (Pole pole : p)
            addPole(pole);
    }

    public void addPole(Pole p){
        collection.put(p.getId(), p);
    }

    public Pole getBy(Pole p){
        if (collection.containsKey(p.getId()))
            return collection.get(p.getId());
        else {
            addPole(p);
            return p;
        }
    }
}
