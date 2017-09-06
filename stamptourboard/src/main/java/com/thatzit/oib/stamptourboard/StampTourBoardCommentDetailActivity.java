package com.thatzit.oib.stamptourboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.thatzit.oib.stamptourboard.adapter.ViewType;
import com.thatzit.oib.stamptourboard.adapter.multiadapter.MultiItemAdapter;
import com.thatzit.oib.stamptourboard.adapter.multiadapter.PostBoardCommentAdapter;
import com.thatzit.oib.stamptourboard.helper.ExtraName;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiComment;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.CommentsModel;
import com.thatzit.oib.stamptourboard.model.PostsRes;
import com.thatzit.oib.stamptourboard.util.ProgressWaitDialog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StampTourBoardCommentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private StampTourBoardConfig config;
    private ProgressWaitDialog progressWaitDialog;

    BoardApiComment boardApiComment;

    private Toolbar toolbar;
    private ImageView backBtn;


    private RecyclerView recyclerViewComments;
    private PostBoardCommentAdapter postBoardCommentAdapter;

    public EditText editTextReview;
    public Button btnReviewSave;

    private PostsRes.PostData postData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postboardcommentdetail);
        progressWaitDialog = new ProgressWaitDialog(this);

        /* This should not happen */
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }

        StampTourBoardConfig config = getConfig();

        postData = (PostsRes.PostData) intent.getSerializableExtra(ExtraName.POST_DATA.getName());
        boardApiComment = ServiceGenerator.createBoardService(BoardApiComment.class);

        setLayout(config);
        setData(config);

    }


    private StampTourBoardConfig getConfig() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        config = bundle.getParcelable(StampTourBoardConfig.class.getSimpleName());
        return config;
    }

    public void setLayout(final StampTourBoardConfig config){
        toolbar = (Toolbar) findViewById(R.id.image_fragment_toolbar);
        backBtn = (ImageView) findViewById(R.id.detail_toolbar_back_button);

        backBtn.setOnClickListener(this);

        recyclerViewComments = (RecyclerView) findViewById(R.id.recyclerview_postboardcommentdetail_reviews);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(StampTourBoardCommentDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewComments.setNestedScrollingEnabled(false);

        postBoardCommentAdapter = new PostBoardCommentAdapter(StampTourBoardCommentDetailActivity.this, config, boardListener, postData);

        editTextReview = (EditText) findViewById(R.id.edittext_postboardcommentdetail_review);
        btnReviewSave = (Button) findViewById(R.id.btn_postboardcommentdetail_writecomment_save);


        btnReviewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ( !editTextReview.getText().toString().isEmpty() ){
                    String comments = editTextReview.getText().toString();
                    editTextReview.setText("");

                    Call<ResponseBody> postsCommentsCall = boardApiComment.postComments(
                            postData.get_id(),
                            config.getUserNick(),
                            config.getUserAccesstoken(),
                            config.getApplicationID(),
                            comments
                            );
                    postsCommentsCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if ( response.isSuccessful() ){
                                //
                                //
                                Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.postboard_comments_write_complete, Toast.LENGTH_SHORT).show();
                                getComments();
                            } else {
                                Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                        }
                    });



                } else {
                    Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.write_comments, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void setData(StampTourBoardConfig config){
        progressWaitDialog.show();
        getComments();
    }

    public void getComments(){

        Call<List<CommentsModel>> getCommentsCall = boardApiComment.getComments(
                postData.get_id()
        );
        getCommentsCall.enqueue(new Callback<List<CommentsModel>>() {
            @Override
            public void onResponse(Call<List<CommentsModel>> call, Response<List<CommentsModel>> response) {

                if ( progressWaitDialog != null  ) {
                    progressWaitDialog.dismiss();
                }

                if ( response.isSuccessful() ){
                    //
                    //
                    recyclerViewComments.setAdapter(null);
                    postBoardCommentAdapter.clear();
                    List<MultiItemAdapter.Row<?>> rows = new ArrayList<>();
                    List<CommentsModel> commentsModelsRes = response.body();

                    if ( commentsModelsRes.isEmpty() ){
                        // items add
                        rows.add(
                                MultiItemAdapter.Row.create( null, ViewType.POSTBOARD_COMMENT_NONE.getCode()
                                )
                        );
                    } else {
                        for ( CommentsModel commentsModel : commentsModelsRes ){
                            rows.add(
                                    MultiItemAdapter.Row.create( commentsModel, ViewType.POSTBOARD_COMMENT_NORMAL.getCode()
                                    )
                            );
                        }
                    }
                    postBoardCommentAdapter.setRows(rows);
                    recyclerViewComments.setAdapter(postBoardCommentAdapter);
                } else {
                    Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<CommentsModel>> call, Throwable t) {

                if ( progressWaitDialog != null  ) {
                    progressWaitDialog.dismiss();
                }

                Toast.makeText(StampTourBoardCommentDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.detail_toolbar_back_button){
            finish();
        }
    }

    BoardListener boardListener = new BoardListener() {
        @Override
        public void postDeleteSuccess() {
            return;
        }

        @Override
        public void commentDeleteSuccess() {
            getComments();
        }
    };
}
