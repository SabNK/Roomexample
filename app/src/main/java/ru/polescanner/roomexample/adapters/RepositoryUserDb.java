package ru.polescanner.roomexample.adapters;

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
    public void add(User u) {
        UserDb udb = this.mapper.mapTo(u);
        dbDao.add(udb);
    }

    @Override
    public List<User> getAll() {
        ListMapper<User, UserDb> listMapper = new ListMapperImpl<>(mapper);
        return listMapper.mapFrom(dbDao.getAll());
    }

}
