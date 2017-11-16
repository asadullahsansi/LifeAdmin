package com.example.asadullahsansi.lifeadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asadullahsansi.lifeadmin.Util.Util_Func;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

import static android.R.attr.password;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dataRef = FirebaseDatabase.getInstance().getReference().child("ChatRoom");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.chatmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuchat) {
           // createChat();
            startActivity(new Intent(ChatActivity.this,CreateChatActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    /*
    private void createChat() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChatActivity.this);
        alertDialog.setTitle("New Chat Room");
        final EditText input = new EditText(ChatActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setHint("Enter Title");
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setPositiveButton("Create",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        final android.app.AlertDialog alert = new SpotsDialog(ChatActivity.this, "Chat Room Creating...");
                        alert.show();
                        dataRef.child(input.getText().toString()).child("title").setValue(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Util_Func.Alert(ChatActivity.this, "Successfully", "New Chat Room Created Successfully");
                                    alert.dismiss();
                                    dialog.dismiss();
                                } else {
                                    alert.dismiss();
                                }
                            }
                        });

                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }
*/

}
