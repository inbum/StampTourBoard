package com.thatzit.oib.stamptourboard;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.thatzit.oib.stamptourboard.adapter.PostWriteImagesAdapter;
import com.thatzit.oib.stamptourboard.helper.Constants;
import com.thatzit.oib.stamptourboard.helper.Utils;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiPost;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StampTourBoardWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private StampTourBoardConfig config;

    private Button toolbar_btn_cancel;
    private Button toolbar_btn_write;

    private Spinner spinnerTownName;
    ArrayList<String> spinnerSource = new ArrayList<String>();
    ArrayAdapter<String> spinnerAdapter;

    private EditText editTextTitle;
    private RecyclerView recyclerViewPhoto;
    private PostWriteImagesAdapter postWriteImagesAdapter;
    private EditText editTextContents;

    private FloatingActionButton fabGetImage;


    String imageEncoded;
    List<File> imagesEncodedList = new ArrayList<File>();

    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;
    private ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postwrite);

        /* This should not happen */
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }

        config = getConfig();

        setLayout();
    }

    private StampTourBoardConfig getConfig() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        config = bundle.getParcelable(StampTourBoardConfig.class.getSimpleName());
        return config;
    }

    private void setLayout() {
        toolbar_btn_cancel = (Button)findViewById(R.id.cancel_btn);
        toolbar_btn_cancel.setOnClickListener(this);
        toolbar_btn_write = (Button)findViewById(R.id.write_btn);
        toolbar_btn_write.setOnClickListener(this);

        spinnerTownName = (Spinner) findViewById(R.id.spinner_postwrite_townname);
        spinnerAdapter = new ArrayAdapter<String>(StampTourBoardWriteActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerSource) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setTextColor(
                            ContextCompat.getColor(StampTourBoardWriteActivity.this, R.color.colorPrimary)
                    );
                } else {
                    ((TextView)v.findViewById(android.R.id.text1)).setTextColor(
                            ContextCompat.getColor(StampTourBoardWriteActivity.this, R.color.stdColorBlack)
                    );
                }

                return v;
            }

            @Override
            public int getCount() {
                return super.getCount()-1; // you dont display last item. It is used as hint.
            }

        };
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editTextTitle = (EditText) findViewById(R.id.editText_postwrite_title);
        recyclerViewPhoto = (RecyclerView) findViewById(R.id.recyclerview_postwrite_images);
        recyclerViewPhoto.setLayoutManager(new LinearLayoutManager(StampTourBoardWriteActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewPhoto.setHasFixedSize(true);
        recyclerViewPhoto.setNestedScrollingEnabled(false);
        postWriteImagesAdapter = new PostWriteImagesAdapter();
        recyclerViewPhoto.setAdapter(postWriteImagesAdapter);
        editTextContents = (EditText) findViewById(R.id.editText_postwrite_contents);

        fabGetImage = (FloatingActionButton) findViewById(R.id.btn_postwrite_addimage);
        fabGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImagePicker imagePicker = ImagePicker.create(StampTourBoardWriteActivity.this)
                        .folderMode(true) // set folder mode (false by default)
                        .folderTitle("Folder") // folder selection title
                        .imageTitle("Tap to select"); // image selection title

                imagePicker.limit(5) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                        .start(RC_CODE_PICKER); // start image picker activity with request code

            }
        });

        setSpinnerTownName();
    }

    public void setSpinnerTownName(){

        for(String townName:config.getSelectTownNames()){
            spinnerAdapter.add(townName);

        }

        Collections.sort(spinnerSource);
        spinnerAdapter.add(config.getHintSelectTwonName());
        spinnerTownName.setAdapter(spinnerAdapter);
        spinnerTownName.setSelection(spinnerAdapter.getCount());
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.cancel_btn) {
            finish();
        } else if (i == R.id.write_btn) {
            writePost();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            imagesEncodedList.clear();
            images = (ArrayList<Image>) ImagePicker.getImages(data);

            for (int i = 0; i < images.size(); i++) {
                imagesEncodedList.add(new File(images.get(i).getPath()));
            }

            postWriteImagesAdapter.setSelectedImages(imagesEncodedList);
            postWriteImagesAdapter.notifyDataSetChanged();

            return;
        }


        if( requestCode == Constants.REQUEST_CODE_GETPHOTO && resultCode == Activity.RESULT_OK && data != null ) {


            // Get the Image from data

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            imagesEncodedList = new ArrayList<File>();
            if(data.getData() != null){

                Uri mImageUri=data.getData();

                // Get the cursor
                Cursor cursor = getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageEncoded  = cursor.getString(columnIndex);
                cursor.close();

            }else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        imagesEncodedList.add(new File(Utils.getImagePath(StampTourBoardWriteActivity.this, uri)));
                    }
                }
            }

            postWriteImagesAdapter.setSelectedImages(imagesEncodedList);
            postWriteImagesAdapter.notifyDataSetChanged();

        }
    }



    private void writePost(){

        if ( editTextTitle.getText().toString().isEmpty() ){
            Toast.makeText(StampTourBoardWriteActivity.this, R.string.input_post_title, Toast.LENGTH_SHORT).show();
            return;
        }

        if ( editTextContents.getText().toString().isEmpty() ){
            Toast.makeText(StampTourBoardWriteActivity.this, R.string.input_post_contents, Toast.LENGTH_SHORT).show();
            return;
        }

        if ( imagesEncodedList == null || imagesEncodedList.isEmpty() ) {
            Toast.makeText(StampTourBoardWriteActivity.this, R.string.select_photos, Toast.LENGTH_SHORT).show();
            return;
        }

        BoardApiPost boardApiPost = ServiceGenerator.createBoardService(BoardApiPost.class);


        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("Nick", config.getUserNick() );
        builder.addFormDataPart("applicationId", config.getApplicationID() );
        builder.addFormDataPart("AccessToken", config.getUserAccesstoken() );
        builder.addFormDataPart("contents", editTextContents.getText().toString() );
        builder.addFormDataPart("title", editTextTitle.getText().toString() );
        if ( spinnerTownName.getSelectedItem().toString().equals(config.getHintSelectTwonName()) ) {
            builder.addFormDataPart("townName", config.getApplicationRegion());
        } else {
            builder.addFormDataPart("townName", spinnerTownName.getSelectedItem().toString());
        }
        if ( imagesEncodedList != null && !imagesEncodedList.isEmpty() ) {
            for (File imageFile : imagesEncodedList) {
                builder.addFormDataPart("img", imageFile.getName(), RequestBody.create(MediaType.parse("image/*"), imageFile));
            }
        }

        MultipartBody requestBody = builder.build();

        Call<ResponseBody> postsResCall = boardApiPost.postImage( requestBody );
        postsResCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if ( response.isSuccessful() ){
                    //
                    //
                    Toast.makeText(StampTourBoardWriteActivity.this, R.string.postboard_write_success, Toast.LENGTH_SHORT).show();
                    setResult(Constants.RESULT_POSTWRITE_COMPLETED);
                    finish();
                } else {

                    if ( response.raw().code() == 400 ){
                        // not enough data
                        Toast.makeText(StampTourBoardWriteActivity.this, R.string.not_enough_data, Toast.LENGTH_SHORT).show();
                        setResult(Constants.NOTENOUGHDATA);
                        finish();
                    } else if ( response.raw().code() == 401 ){
                        // invalid accesstoken
                        Toast.makeText(StampTourBoardWriteActivity.this, R.string.invalid_accesstoken, Toast.LENGTH_SHORT).show();
                        setResult(Constants.INVALIDACCESSTOKEN);
                        finish();
                    } else {
                        Toast.makeText(StampTourBoardWriteActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(StampTourBoardWriteActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
            }
        });



    }


}
