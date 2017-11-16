package com.example.asadullahsansi.lifeadmin;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asadullahsansi.lifeadmin.Util.DividerItemDecoration;
import com.example.asadullahsansi.lifeadmin.Util.Util_Func;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asadullahsansi on 11/9/17.
 */



public class SubCategoryActivity extends AppCompatActivity {

    Spinner spinnerCategory;
    RecyclerView recyclerView;
    EditText editSubCategory;

    DatabaseReference dataRefSubCat;
    DatabaseReference dataRefCat;

    DatabaseReference dataRefItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        dataRefCat = FirebaseDatabase.getInstance().getReference().child(Util_Func.category);
        dataRefCat.keepSynced(true);

        dataRefSubCat = FirebaseDatabase.getInstance().getReference().child(Util_Func.subCategory);
        dataRefSubCat.keepSynced(true);

        dataRefItem = FirebaseDatabase.getInstance().getReference().child(Util_Func.item);
        dataRefItem.keepSynced(true);



        editSubCategory = (EditText) findViewById(R.id.editSubCategory);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSubCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));


        final List<String> list = new ArrayList<String>();
        list.add("Select the Category");
        spinnerCategory.setSelection(0);


        dataRefCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    list.add(data.getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinnerCategory.setAdapter(adapter);


        findViewById(R.id.btnAddSubCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerCategory.getSelectedItemPosition() == 0) {
                    Util_Func.Alert(SubCategoryActivity.this, "No Selected Category", "Select the category First");
                    return;
                }
                if (editSubCategory.getText().equals("")) {
                    Util_Func.Alert(SubCategoryActivity.this, "Empty", "Enter the SubCategory");
                    return;
                }

                dataRefSubCat.child(spinnerCategory.getSelectedItem().toString()).child(editSubCategory.getText().toString()).setValue(editSubCategory.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Util_Func.Alert(SubCategoryActivity.this, "Successfully", "SubCategory Successfully Added");
                            editSubCategory.setText("");

                        }
                    }
                });

            }
        });


        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String category= spinnerCategory.getItemAtPosition(position).toString();
                FirebaseRecyclerAdapter<String, SubCategoryViewHolder> subCategoryAdapter = new FirebaseRecyclerAdapter<String, SubCategoryViewHolder>(
                        String.class, android.R.layout.simple_list_item_1, SubCategoryViewHolder.class, dataRefSubCat.child(category)
                ) {
                    @Override
                    protected void populateViewHolder(final SubCategoryViewHolder viewHolder, String model, int position) {
                        viewHolder.text.setText(model);

                        viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(SubCategoryActivity.this);
                                builder.setTitle("Delete")
                                        .setMessage("Do You want to Delete?")
                                        .setIcon(R.mipmap.ic_launcher)

                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dataRefSubCat.child(category).child(viewHolder.text.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Util_Func.Alert(SubCategoryActivity.this, "Deleted", "SubCategory Deleted Successfully");
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
                recyclerView.setAdapter(subCategoryAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView text;

        public SubCategoryViewHolder(View itemView) {
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
