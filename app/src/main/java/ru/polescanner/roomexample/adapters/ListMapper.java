package ru.polescanner.roomexample.adapters;

import java.util.List;

import ru.polescanner.roomexample.adapters.Mapper;
import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.domain.Aggregate;

public interface ListMapper<I, O> extends Mapper<List<I>, List<O>> {}
