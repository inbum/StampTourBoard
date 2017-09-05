package com.thatzit.oib.stamptourboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.oib.stamptourboard.adapter.PostBoardListItemImageAdapter;
import com.thatzit.oib.stamptourboard.adapter.ViewType;
import com.thatzit.oib.stamptourboard.adapter.multiadapter.MultiItemAdapter;
import com.thatzit.oib.stamptourboard.adapter.multiadapter.PostBoardCommentAdapter;
import com.thatzit.oib.stamptourboard.helper.ExtraName;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiComment;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiPost;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.CommentsModel;
import com.thatzit.oib.stamptourboard.model.PostsRes;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StampTourBoardDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private StampTourBoardConfig config;

    BoardApiComment boardApiComment;
    BoardApiPost boardApiPost;

    private Toolbar toolbar;
    private ImageView backBtn;

    public RecyclerView recyclerViewImage;
    private PostBoardListItemImageAdapter postBoardListItemImageAdapter;
    private TextView textViewImageCount;
    public ImageView imageViewLoadImg;

    public TextView townName;
    public TextView applicationName;
    public TextView title;

    public TextView nick;
    public TextView createdTime;
    public TextView views;
    public TextView reviews;
    public TextView contents;

    private RecyclerView recyclerViewComments;
    private PostBoardCommentAdapter postBoardCommentAdapter;

    public EditText editTextReview;
    public Button btnReviewSave;

    private PostsRes.PostData postData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postboarddetail);

        /* This should not happen */
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }

        config = getConfig();

        postData = (PostsRes.PostData) intent.getSerializableExtra(ExtraName.POST_DATA.getName());
        boardApiComment = ServiceGenerator.createBoardService(BoardApiComment.class);

        setLayout();

        if ( postData == null ){
            Uri uri = intent.getData();
            String post_id = uri.getQueryParameter("post_id");

            boardApiPost = ServiceGenerator.createBoardService(BoardApiPost.class);
            Call<PostsRes.PostData> getPostCall = boardApiPost.getPost(
                    post_id
            );
            getPostCall.enqueue(new Callback<PostsRes.PostData>() {
                @Override
                public void onResponse(Call<PostsRes.PostData> call, Response<PostsRes.PostData> response) {

                    if ( response.isSuccessful() ){
                        //
                        //
                        postData = response.body();
                        setDataLayout(config);
                        setData();
                        callViewCount();
                    } else {
                        Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Call<PostsRes.PostData> call, Throwable t) {
                    Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            setDataLayout(config);
            setData();
            callViewCount();
        }





    }

    private StampTourBoardConfig getConfig() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        config = bundle.getParcelable(StampTourBoardConfig.class.getSimpleName());
        return config;
    }

    public void setLayout(){
        toolbar = (Toolbar) findViewById(R.id.image_fragment_toolbar);
        backBtn = (ImageView) findViewById(R.id.detail_toolbar_back_button);

        backBtn.setOnClickListener(this);

        recyclerViewImage = (RecyclerView) findViewById(R.id.recyclerview_postboarddetail_images);
        recyclerViewImage.setLayoutManager(new LinearLayoutManager(StampTourBoardDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));

        textViewImageCount = (TextView) findViewById(R.id.textView_postboarddetail_imagecount);
        imageViewLoadImg = (ImageView) findViewById(R.id.imageView_postboarddetail_loading);

        townName = (TextView)findViewById(R.id.textView_postboarddetail_townname);
        applicationName = (TextView)findViewById(R.id.textView_postboarddetail_application);
        title = (TextView)findViewById(R.id.textView_postboarddetail_title);

        nick = (TextView)findViewById(R.id.textView_postboarddetail_nick);
        createdTime = (TextView)findViewById(R.id.textView_postboarddetail_created);
        views = (TextView)findViewById(R.id.textView_postboarddetail_views);
        reviews = (TextView)findViewById(R.id.textView_postboarddetail_reviews);
        contents = (TextView)findViewById(R.id.textView_postboarddetail_contents);

        recyclerViewComments = (RecyclerView) findViewById(R.id.recyclerview_postboarddetail_reviews);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(StampTourBoardDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewComments.setNestedScrollingEnabled(false);

        postBoardCommentAdapter = new PostBoardCommentAdapter(StampTourBoardDetailActivity.this, config, boardListener, postData);

        editTextReview = (EditText) findViewById(R.id.edittext_postboarddetail_review);
        btnReviewSave = (Button) findViewById(R.id.btn_postboarddetail_writecomment_save);

    }

    public void setDataLayout(final StampTourBoardConfig config){

        recyclerViewImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentpage = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1;
                textViewImageCount.setText(
                        Html.fromHtml( "<strong>" + currentpage + "</strong>" + " / " + postData.getFiles().size())
                );
            }
        });


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
                                Toast.makeText(StampTourBoardDetailActivity.this, R.string.postboard_comments_write_complete, Toast.LENGTH_SHORT).show();
                                getComments();
                            } else {
                                Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                        }
                    });



                } else {
                    Toast.makeText(StampTourBoardDetailActivity.this, R.string.write_comments, Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void setData(){
        if ( postData.getFiles() != null && !postData.getFiles().isEmpty()){
            postBoardListItemImageAdapter = new PostBoardListItemImageAdapter(StampTourBoardDetailActivity.this, postData.getFiles());
            recyclerViewImage.setAdapter(postBoardListItemImageAdapter);
            recyclerViewImage.setVisibility(View.VISIBLE);
            textViewImageCount.setVisibility(View.VISIBLE);
            textViewImageCount.setText(
                    Html.fromHtml( "<strong>1</strong>" + " / " + postData.getFiles().size())
            );
            imageViewLoadImg.setVisibility(View.GONE);
        } else {
            recyclerViewImage.setVisibility(View.GONE);
            textViewImageCount.setVisibility(View.GONE);
            imageViewLoadImg.setVisibility(View.VISIBLE);
        }

        townName.setText( postData.getTownName() );

        applicationName.setText( postData.getApplicationId() );

        title.setText( postData.getTitle() );

        nick.setText( postData.getPostedBy().getNick() );
        createdTime.setText( postData.getCreatedTimeStr() );
        views.setText(
                String.format(getString(R.string.views_format), postData.getViews())
        );
        contents.setText( postData.getContents() );

        getComments();
        boardApiComment.getComments(postData.get_id());

    }

    public void getComments(){

        Call<List<CommentsModel>> getCommentsCall = boardApiComment.getComments(
                postData.get_id()
        );
        getCommentsCall.enqueue(new Callback<List<CommentsModel>>() {
            @Override
            public void onResponse(Call<List<CommentsModel>> call, Response<List<CommentsModel>> response) {


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
                    Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<List<CommentsModel>> call, Throwable t) {

                Toast.makeText(StampTourBoardDetailActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void callViewCount(){
        BoardApiPost boardApiPost = ServiceGenerator.createBoardService(BoardApiPost.class);
        Call<ResponseBody> updateViewCountCall = boardApiPost.updateViewCount( postData.get_id() );
        updateViewCountCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
