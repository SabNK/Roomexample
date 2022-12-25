package ru.polescanner.roomexample.adapters;

import java.util.Arrays;
import java.util.List;

import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.adapters.db.UserDb;
import ru.polescanner.roomexample.domain.User;

public class RepositoryUserDb extends RepositoryDb<User, UserDb> {
    public RepositoryUserDb(EntityDb.Dao<UserDb> dbDao,
                            Mapper<User, UserDb> mapper) {
        super(dbDao, mapper);
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
