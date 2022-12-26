package ru.polescanner.roomexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import ru.polescanner.roomexample.adapters.db.AppDatabase;
import ru.polescanner.roomexample.adapters.db.UserDb;
import ru.polescanner.roomexample.adapters.ListMapperImpl;
import ru.polescanner.roomexample.domain.User;

public class MainActivity extends AppCompatActivity {
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addUserButton = findViewById(R.id.addNewUserButton);
        Button addPoleButton = findViewById(R.id.addNewUserButton);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,
                                                  AddNewUserActivity.class), 100);
            }
        });

        addPoleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,
                                                  AddPoleActivity.class), 200);
            }
        });

        //initRecyclerView();
        //loadUserList();
    }
/*
    private void loadUserList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        List<UserDb> userDbList = db.userDbDao().getAll();
        List<User> userList = new ListMapperImpl<User, UserDb>(new UserDb.Mapper()).mapFrom(userDbList);
        userListAdapter.setUserList(userList);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        userListAdapter = new UserListAdapter(this);
        recyclerView.setAdapter(userListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            loadUserList();
        }
        if (requestCode == 200) {
            loadUserList();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
}