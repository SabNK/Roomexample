package ru.polescanner.roomexample.adapters;

import java.util.Arrays;
import java.util.List;

import ru.polescanner.roomexample.adapters.db.PoleDb;
import ru.polescanner.roomexample.adapters.db.UserDb;
import ru.polescanner.roomexample.domain.Pole;
import ru.polescanner.roomexample.domain.Repository;
import ru.polescanner.roomexample.domain.User;

public class RepositoryUserImpl implements Repository<User> {
    private UserDb.Dao dbDao;
    private Mapper<User,UserDb> mapper;
    protected ListMapper<User,UserDb> listMapper;

    public RepositoryUserImpl(UserDb.Dao dbDao,
                              Mapper<User, UserDb> mapper) {
        this.dbDao = dbDao;
        this.mapper = mapper;
        this.listMapper = new ListMapperImpl<User, UserDb>(mapper);
    }

    @Override
    public void add(User... u) {
        List<UserDb> us = this.listMapper.mapTo(Arrays.asList(u));
        dbDao.add(us.toArray(new UserDb[0]));
    }

    @Override
    public List<User> getAll() {
        return listMapper.mapFrom(dbDao.getAll());
    }

    @Override
    public User getById(String id) {
        return mapper.mapFrom(dbDao.getById(id));
    }
}
