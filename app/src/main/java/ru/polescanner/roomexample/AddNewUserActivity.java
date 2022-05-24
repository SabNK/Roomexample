package ru.polescanner.roomexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import ru.polescanner.roomexample.adapters.RepositoryUserDb;
import ru.polescanner.roomexample.adapters.db.AppDatabase;
import ru.polescanner.roomexample.adapters.db.EntityDb;
import ru.polescanner.roomexample.adapters.db.UserDb;
import ru.polescanner.roomexample.domain.Repository;
import ru.polescanner.roomexample.domain.User;

public class AddNewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewUser(firstNameInput.getText().toString(),
                            lastNameInput.getText().toString());
            }
        });

    }

    private void saveNewUser(String firstName, String lastName){
        //ToDo remove from UI
        AppDatabase db = AppDatabase.getDbInstance((this.getApplicationContext()));
        User user = new User(firstName, lastName);
        Repository<User> repo = new RepositoryUserDb((EntityDb.Dao<UserDb>) db.userDbDao(),
                                                     new UserDb.Mapper());
        Log.d("WE TRUST!!!", "Repo is ready to add");
        repo.add(user);
        finish();
    }
}