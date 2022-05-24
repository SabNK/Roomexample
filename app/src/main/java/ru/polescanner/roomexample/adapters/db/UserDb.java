package ru.polescanner.roomexample.adapters.db;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

import ru.polescanner.roomexample.adapters.Mapper;
import ru.polescanner.roomexample.domain.User;

@Entity(tableName = "users")
public class UserDb /*extends EntityDb*/ {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="first_name")
    public String firstName;

    @ColumnInfo(name="last_name")
    public String lastName;

    public UserDb(){}

    @Ignore
    public UserDb(int uid, String firstName, String lastName) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Ignore
    public UserDb(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @androidx.room.Dao
    public static interface Dao {

        @Query("SELECT * FROM users")
        List<UserDb> getAll();

        @Insert
        void add(UserDb... uDb);
    }

    public static class Mapper implements ru.polescanner.roomexample.adapters.Mapper<User, UserDb> {

        @Override
        public UserDb mapTo(User user) {
            return new UserDb(user.firstName, user.lastName);
        }

        @Override
        public User mapFrom(UserDb userDb) {
            return new User(userDb.firstName, userDb.lastName);
        }
    }
}
