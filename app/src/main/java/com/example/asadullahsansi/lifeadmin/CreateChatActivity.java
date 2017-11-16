package com.example.asadullahsansi.lifeadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTitle, editBlockWord;
    private RecyclerView recyclerViewBlockUser, recyclerViewBlockWord;
    private Spinner spinnerBlocksUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editBlockWord = (EditText) findViewById(R.id.editBlockWord);

        findViewById(R.id.btnBlockUser).setOnClickListener(this);
        findViewById(R.id.btnBlockWord).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        spinnerBlocksUser = (Spinner) findViewById(R.id.spinnerBlockUser);

        recyclerViewBlockUser = (RecyclerView) findViewById(R.id.recyclerBlockUser);
        recyclerViewBlockWord = (RecyclerView) findViewById(R.id.recyclerBlockWord);


    }

    @Override
    public void onClick(View v) {

    }
}
