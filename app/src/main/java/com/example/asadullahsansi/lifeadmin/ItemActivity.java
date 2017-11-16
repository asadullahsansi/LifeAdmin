package com.example.asadullahsansi.lifeadmin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.asadullahsansi.lifeadmin.Util.Util_Func;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by asadullahsansi on 11/9/17.
 */


public class ItemActivity extends AppCompatActivity implements View.OnClickListener {


    private int mHour, mMinute;
    private int mYear, mMonth, mDay;
    private String date = "", time = "";


    Spinner spinnerCategory, spinnerSubCategory;

    EditText editQuotes, editPoems;

    EditText editActivityInfo, editActivityDetail;

    EditText editRakCategory, editRakTitle, editRakDetail;
    ImageView imageRak;

    EditText editImageTitle, editImageDesc, editImageLink, editImageSite;
    ImageView imageImage;


    DatabaseReference dataRefSubCat;
    DatabaseReference dataRefCat;
    DatabaseReference dataRef;


    LinearLayout layoutQuotes, layoutPoems, layoutImage, layoutRak, layoutActivity;

    TextView textStartDate, textStartTime, textEndDate, textEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);


        dataRefCat = FirebaseDatabase.getInstance().getReference().child(Util_Func.category);
        dataRefCat.keepSynced(true);

        dataRefSubCat = FirebaseDatabase.getInstance().getReference().child(Util_Func.subCategory);
        dataRefSubCat.keepSynced(true);

        dataRef = FirebaseDatabase.getInstance().getReference().child(Util_Func.item);

        //Quotes//
        editQuotes = (EditText) findViewById(R.id.editQutoes);
        findViewById(R.id.btnQuotes).setOnClickListener(this);


        //Image
        editImageTitle = (EditText) findViewById(R.id.editImageTitle);
        editImageDesc = (EditText) findViewById(R.id.editImageDesc);
        editImageLink = (EditText) findViewById(R.id.editImageLink);
        editImageSite = (EditText) findViewById(R.id.editImageAttribute);
        imageImage = (ImageView) findViewById(R.id.imageImage);
        findViewById(R.id.btnImage).setOnClickListener(this);
        findViewById(R.id.btnImageSelectImage).setOnClickListener(this);


        //Poems
        editPoems = (EditText) findViewById(R.id.editPoems);
        findViewById(R.id.btnPoems).setOnClickListener(this);


        //Rak
        editRakCategory = (EditText) findViewById(R.id.editRakCategory);
        editRakDetail = (EditText) findViewById(R.id.editRakDetail);
        editRakTitle = (EditText) findViewById(R.id.editRakTitle);
        imageRak = (ImageView) findViewById(R.id.imageRak);

        findViewById(R.id.btnRakSelectImage).setOnClickListener(this);
        findViewById(R.id.btnRak).setOnClickListener(this);

        //activity
        editActivityDetail = (EditText) findViewById(R.id.editActivityDetail);
        editActivityInfo = (EditText) findViewById(R.id.editActivityInfo);
        textStartDate = (TextView) findViewById(R.id.textStartDate);
        textStartTime = (TextView) findViewById(R.id.textStartTime);
        textEndDate = (TextView) findViewById(R.id.textEndDate);
        textEndTime = (TextView) findViewById(R.id.textEndTime);
        findViewById(R.id.btnActivity).setOnClickListener(this);


        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spinnerSubCategory = (Spinner) findViewById(R.id.spinnerSubCategory);

        layoutQuotes = (LinearLayout) findViewById(R.id.layoutQuotes);
        layoutPoems = (LinearLayout) findViewById(R.id.layoutPoems);
        layoutImage = (LinearLayout) findViewById(R.id.layoutImage);
        layoutRak = (LinearLayout) findViewById(R.id.layoutRak);
        layoutActivity = (LinearLayout) findViewById(R.id.layoutActivity);


        findViewById(R.id.btnStartDate).setOnClickListener(this);
        findViewById(R.id.btnStartTime).setOnClickListener(this);
        findViewById(R.id.btnEndTime).setOnClickListener(this);
        findViewById(R.id.btnEndDate).setOnClickListener(this);


        final List<String> listCategory = new ArrayList<String>();
        listCategory.add("Select the Category");
        spinnerCategory.setSelection(0);

        final List<String> listSubCategory = new ArrayList<String>();
        listSubCategory.add("Select the SubCategory");
        spinnerSubCategory.setSelection(0);


        dataRefCat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    listCategory.add(data.getValue().toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listCategory);
        spinnerCategory.setAdapter(adapterCategory);


        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinnerCategory.getItemAtPosition(position).toString().toLowerCase().contains("poem")) {
                    layoutPoems.setVisibility(View.VISIBLE);
                    layoutQuotes.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutRak.setVisibility(View.GONE);
                    layoutActivity.setVisibility(View.GONE);


                } else if (spinnerCategory.getItemAtPosition(position).toString().toLowerCase().contains("quote")) {
                    layoutQuotes.setVisibility(View.VISIBLE);
                    layoutPoems.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutRak.setVisibility(View.GONE);
                    layoutActivity.setVisibility(View.GONE);

                } else if (spinnerCategory.getItemAtPosition(position).toString().toLowerCase().contains("rak")) {
                    layoutQuotes.setVisibility(View.GONE);
                    layoutPoems.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutRak.setVisibility(View.VISIBLE);
                    layoutActivity.setVisibility(View.GONE);

                } else if (spinnerCategory.getItemAtPosition(position).toString().toLowerCase().contains("image")) {
                    layoutQuotes.setVisibility(View.GONE);
                    layoutPoems.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.VISIBLE);
                    layoutRak.setVisibility(View.GONE);
                    layoutActivity.setVisibility(View.GONE);

                } else if (spinnerCategory.getItemAtPosition(position).toString().toLowerCase().contains("activity")) {
                    layoutQuotes.setVisibility(View.GONE);
                    layoutPoems.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutRak.setVisibility(View.GONE);
                    layoutActivity.setVisibility(View.VISIBLE);
                } else {
                    layoutQuotes.setVisibility(View.GONE);
                    layoutPoems.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutRak.setVisibility(View.GONE);
                    layoutActivity.setVisibility(View.GONE);
                }
                dataRefSubCat.child(spinnerCategory.getItemAtPosition(position).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            listSubCategory.clear();
                            listSubCategory.add("Select the SubCategory");
                            spinnerSubCategory.setSelection(0);
                        }


                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            listSubCategory.add(data.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> adapterSubCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listSubCategory);
        spinnerSubCategory.setAdapter(adapterSubCategory);
    }


    private void datePicker(final int i) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        if (i == 1)
                            textStartDate.setText(date);
                        else
                            textEndDate.setText(date);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }


    private void timePicker(final int i) {


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        time = getTime(hourOfDay, minute);
                        if (i == 1)
                            textStartTime.setText(time);
                        else
                            textEndTime.setText(time);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();


    }

    private String getTime(int hr, int min) {
        Time time = new Time(hr, min, 0);

        return new SimpleDateFormat("h:mm a").format(time);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    private void addQuotes() {
        if (TextUtils.isEmpty(editQuotes.getText())) {
            Util_Func.Alert(ItemActivity.this, "Empty Quote", "Enter the Quote First");
            return;
        }
        final android.app.AlertDialog dialog = new SpotsDialog(ItemActivity.this, "Quote Adding...");
        dialog.show();

        dataRef.child(spinnerCategory.getSelectedItem().toString()).child(spinnerSubCategory.getSelectedItem().toString()).push().child("value").setValue(editQuotes.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Util_Func.Alert(ItemActivity.this, "Successfully", "Quote Successfully Added");
                    editQuotes.setText("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void addPoems() {
        if (TextUtils.isEmpty(editPoems.getText())) {
            Util_Func.Alert(ItemActivity.this, "Empty Poem", "Enter the Poem First");
            return;
        }
        final android.app.AlertDialog dialog = new SpotsDialog(ItemActivity.this, "Poem Adding...");
        dialog.show();

        dataRef.child(spinnerCategory.getSelectedItem().toString()).child(spinnerSubCategory.getSelectedItem().toString()).push().child("value").setValue(editPoems.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Util_Func.Alert(ItemActivity.this, "Successfully", "Poem Successfully Added");
                    editPoems.setText("");
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });
    }

    private void addActivity() {

        if (TextUtils.isEmpty(editActivityDetail.getText()) || TextUtils.isEmpty(editActivityInfo.getText())
                || TextUtils.isEmpty(textStartDate.getText()) || TextUtils.isEmpty(textEndDate.getText())
                || TextUtils.isEmpty(textStartTime.getText()) || TextUtils.isEmpty(textEndTime.getText())
                ) {
            Util_Func.Alert(ItemActivity.this, "Empty", "Please enter the complete info");
            return;
        }

        final android.app.AlertDialog dialog = new SpotsDialog(ItemActivity.this, "Activity Adding...");
        dialog.show();

        Map<String, String> map = new HashMap<>();
        map.put("info", editActivityInfo.getText().toString());
        map.put("detail", editActivityDetail.getText().toString());
        map.put("startdate", textStartDate.getText().toString());
        map.put("starttime", textStartTime.getText().toString());
        map.put("enddate", textEndDate.getText().toString());
        map.put("endtime", textEndTime.getText().toString());


        dataRef.child(spinnerCategory.getSelectedItem().toString()).child(spinnerSubCategory.getSelectedItem().toString()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Util_Func.Alert(ItemActivity.this, "Successfully", "Activity Successfully Added");
                    editActivityDetail.setText("");
                    editActivityInfo.setText("");

                    textStartTime.setText("");
                    textEndTime.setText("");
                    textStartDate.setText("");
                    textEndDate.setText("");

                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }
            }
        });


    }

    private static FirebaseStorage storage_Ref = FirebaseStorage.getInstance();
    public static FirebaseUser _User = FirebaseAuth.getInstance().getCurrentUser();

    private StorageReference getImageStorageReference(FirebaseUser user, Uri uri) {
        return storage_Ref.getReference().child(user.getUid() + "/" + Calendar.getInstance().getTimeInMillis() + "/" + uri
                .getLastPathSegment());
    }


    private void addRAK() {
        if (TextUtils.isEmpty(editRakCategory.getText()) || TextUtils.isEmpty(editRakTitle.getText()) || TextUtils.isEmpty(editRakDetail.getText())) {
            Util_Func.Alert(ItemActivity.this, "Empty", "Please enter the complete info");
            return;
        }
        if (_uri == null) {
            Util_Func.Alert(ItemActivity.this, "Image Not Selected", "Please Select Image First");
            return;
        }

        final android.app.AlertDialog dialog = new SpotsDialog(ItemActivity.this, "Rak Adding...");
        dialog.show();

        final StorageReference imgReference = getImageStorageReference(_User, _uri);

        UploadTask uploadTask = imgReference.putFile(_uri);
        // failure listener and success Listener
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                Map<String, String> map = new HashMap<>();
                map.put("category", editRakCategory.getText().toString());
                map.put("title", editRakTitle.getText().toString());
                map.put("details", editRakDetail.getText().toString());
                map.put("url", downloadUrl);

                dataRef.child(spinnerCategory.getSelectedItem().toString()).child(spinnerSubCategory.getSelectedItem().toString()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Util_Func.Alert(ItemActivity.this, "Successfully", "Rak Successfully Added");

                            editRakCategory.setText("");
                            editRakTitle.setText("");
                            editRakDetail.setText("");
                            _uri = null;
                            imageRak.setImageURI(null);
                            imageRak.setVisibility(View.GONE);

                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

    }

    private void addImage() {
        if (TextUtils.isEmpty(editImageTitle.getText()) || TextUtils.isEmpty(editImageDesc.getText()) || TextUtils.isEmpty(editImageLink.getText()) || TextUtils.isEmpty(editImageSite.getText())) {
            Util_Func.Alert(ItemActivity.this, "Empty", "Please enter the complete info");
            return;
        }
        if (_uri == null) {
            Util_Func.Alert(ItemActivity.this, "Image Not Selected", "Please Select Image First");
            return;
        }

        final android.app.AlertDialog dialog = new SpotsDialog(ItemActivity.this, "Image Adding...");
        dialog.show();

        final StorageReference imgReference = getImageStorageReference(_User, _uri);

        UploadTask uploadTask = imgReference.putFile(_uri);
        // failure listener and success Listener
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                Map<String, String> map = new HashMap<>();
                map.put("title", editImageTitle.getText().toString());
                map.put("description", editImageDesc.getText().toString());
                map.put("link", editImageLink.getText().toString());
                map.put("attribute", editImageSite.getText().toString());
                map.put("url", downloadUrl);

                dataRef.child(spinnerCategory.getSelectedItem().toString()).child(spinnerSubCategory.getSelectedItem().toString()).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Util_Func.Alert(ItemActivity.this, "Successfully", "Image Successfully Added");

                            editImageTitle.setText("");
                            editImageDesc.setText("");
                            editImageLink.setText("");
                            editImageSite.setText("");
                            _uri = null;
                            imageImage.setImageURI(null);
                            imageImage.setVisibility(View.GONE);

                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartDate:
                datePicker(1);
                break;

            case R.id.btnStartTime:
                timePicker(1);
                break;

            case R.id.btnEndDate:
                datePicker(2);
                break;

            case R.id.btnEndTime:
                timePicker(2);
                break;

            case R.id.btnQuotes:
                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                    Util_Func.Alert(ItemActivity.this, "Select SubCategory", "There is No SubCategory Selected");
                else
                    addQuotes();

                break;

            case R.id.btnPoems:
                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                    Util_Func.Alert(ItemActivity.this, "Select SubCategory", "There is No SubCategory Selected");
                else
                    addPoems();


                break;
            case R.id.btnActivity:
                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                    Util_Func.Alert(ItemActivity.this, "Select SubCategory", "There is No SubCategory Selected");
                else
                    addActivity();

                break;
            case R.id.btnRak:

                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                    Util_Func.Alert(ItemActivity.this, "Select SubCategory", "There is No SubCategory Selected");
                else
                    addRAK();


                break;
            case R.id.btnImage:
                if (spinnerSubCategory.getSelectedItemPosition() == 0)
                    Util_Func.Alert(ItemActivity.this, "Select SubCategory", "There is No SubCategory Selected");
                else
                    addImage();
                break;

            case R.id.btnRakSelectImage:
                rakOrImage = "rak";
                chooseImage();
                break;

            case R.id.btnImageSelectImage:
                rakOrImage = "img";
                chooseImage();
                break;
        }
    }


    public static final int REQUEST_PICK_IMAGE = 1;

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private Uri _uri = null;

    String rakOrImage = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_IMAGE) {
            if (data != null) {
                _uri = data.getData();
                Bitmap _bitMap = getBitmapForUri(this, _uri);
                Bitmap _resizeBitMap = scaleImage(_bitMap);
                if (_bitMap != _resizeBitMap)
                    _uri = savePhotoImage(this, _bitMap);
                if (rakOrImage.equals("rak")) {
                    imageRak.setImageURI(_uri);
                    imageRak.setVisibility(View.VISIBLE);
                } else if (rakOrImage.equals("img")) {
                    imageImage.setImageURI(_uri);
                    imageImage.setVisibility(View.VISIBLE);
                }

            }
        }

    }


    public static Bitmap getBitmapForUri(Context context, Uri imageUri) {
        Bitmap b = null;
        try {
            b = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static final double LINEAR_DIMENSION_MAX = 500.0;


    public static Bitmap scaleImage(Bitmap bitmap) {
        int height_Original = bitmap.getHeight();
        int width_Original = bitmap.getWidth();
        double scaleFactor = LINEAR_DIMENSION_MAX / (double) (height_Original + width_Original);
        if (scaleFactor < 1.0) {
            return Bitmap.createScaledBitmap(bitmap, (int) Math.round(width_Original * scaleFactor), (int) Math.round(height_Original * scaleFactor), true);
        } else {
            return bitmap;
        }
    }

    public static Uri savePhotoImage(Context context, Bitmap imageBitmap) {
        File file_Photo = null;
        try {
            file_Photo = createImageFile(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file_Photo == null) {
            return null;
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file_Photo);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
        }
        return Uri.fromFile(file_Photo);
    }


    public static final String IMAGE_FILE_NAME_PREFIX = "img-";

    protected static File createImageFile(Context context) throws IOException {


        return File.createTempFile(
                IMAGE_FILE_NAME_PREFIX + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()),
                ".jpg",
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        );
    }


}
