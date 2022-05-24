package ru.polescanner.roomexample.adapters;

import java.util.ArrayList;
import java.util.List;

import ru.polescanner.roomexample.adapters.ListMapper;
import ru.polescanner.roomexample.adapters.Mapper;

public class ListMapperImpl<I,O> implements ListMapper<I,O> {
    private Mapper<I, O> mapper;

    public ListMapperImpl(Mapper<I, O> mapper) {
        this.mapper = mapper;
    }

    public List<O> mapTo(List<I> iList) {
        if (null == iList)
            throw new IllegalArgumentException("null list");
        if (null == this.mapper)
            throw new IllegalArgumentException("null mapper");
        List<O> oList = new ArrayList<>();
        for (I i : iList) {
            oList.add(mapper.mapTo(i));
        }
        return oList;
    }

    public List<I> mapFrom(List<O> oList) {
        if (null == oList)
            throw new IllegalArgumentException("null list");
        if (null == this.mapper)
            throw new IllegalArgumentException("null mapper");
        List<I> iList = new ArrayList<>();
        for (O o : oList) {
            iList.add(mapper.mapFrom(o));
        }
        return iList;
    }
}
