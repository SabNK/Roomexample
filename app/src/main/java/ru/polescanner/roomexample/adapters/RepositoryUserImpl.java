package ru.polescanner.roomexample.adapters;

import java.util.List;

import ru.polescanner.roomexample.adapters.db.UserDb;
import ru.polescanner.roomexample.domain.Repository;
import ru.polescanner.roomexample.domain.User;

public class RepositoryUserImpl implements Repository<User> {
    private UserDb.Dao dbDao;
    private Mapper<User,UserDb> mapper;

    public RepositoryUserImpl(UserDb.Dao dbDao,
                              Mapper<User, UserDb> mapper) {
        this.dbDao = dbDao;
        this.mapper = mapper;
    }

    @Override
    public void add(User i) {
        UserDb o = mapper.mapTo(i);
        dbDao.add(o);
    }

    @Override
    public List<User> getAll() {
        ListMapper<User, UserDb> listMapper = new ListMapperImpl<>(mapper);
        return listMapper.mapFrom(dbDao.getAll());
    }
}
