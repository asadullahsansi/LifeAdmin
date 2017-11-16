package com.example.asadullahsansi.lifeadmin;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asadullahsansi.lifeadmin.Util.DividerItemDecoration;
import com.example.asadullahsansi.lifeadmin.Util.Util_Func;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

/**
 * Created by asadullahsansi on 11/9/17.
 */


public class CategoryActivity extends AppCompatActivity {

    EditText editCategory;

    RecyclerView recylerView;

    DatabaseReference dataRef;

    DatabaseReference dataRefSubCat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        dataRef = FirebaseDatabase.getInstance().getReference().child(Util_Func.category);
        dataRef.keepSynced(true);

        dataRefSubCat = FirebaseDatabase.getInstance().getReference().child(Util_Func.subCategory);


        editCategory = (EditText) findViewById(R.id.editCategory);
        recylerView = (RecyclerView) findViewById(R.id.recyclerViewCategory);
        recylerView.setLayoutManager(new LinearLayoutManager(this));


        //recylerView.addItemDecoration(new DividerItemDecoration(this));

        recylerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));


        findViewById(R.id.btnAddCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editCategory.getText())) {
                    Util_Func.Alert(CategoryActivity.this, "Empty Category", "Enter the Category First");
                    return;
                }

                dataRef.child(editCategory.getText().toString()).setValue(editCategory.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Util_Func.Alert(CategoryActivity.this, "Successfully", "Category Successfully Added");
                            editCategory.setText("");

                        }


                    }
                });

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<String, CategoryViewHolder> categoryAdapter = new FirebaseRecyclerAdapter<String, CategoryViewHolder>(
                String.class, android.R.layout.simple_list_item_1, CategoryViewHolder.class, dataRef
        ) {
            @Override
            protected void populateViewHolder(final CategoryViewHolder viewHolder, String model, int position) {
                viewHolder.text.setText(model);

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(CategoryActivity.this);
                        builder.setTitle("Delete")
                                .setMessage("Your SubCategory also be Deleted! Do You want to Delete?")
                                .setIcon(R.mipmap.ic_launcher)

                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataRef.child(viewHolder.text.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    dataRefSubCat.child(viewHolder.text.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful())
                                                                Util_Func.Alert(CategoryActivity.this, "Deleted", "Category Deleted Successfully");
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                        return false;
                    }
                });
            }

        };

        recylerView.setAdapter(categoryAdapter);
    }


    private static class CategoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView text;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            text = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }


}
