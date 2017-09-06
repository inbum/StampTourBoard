package com.thatzit.oib.stamptourboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.oib.stamptourboard.adapter.PostBoardRecyclerAdapter;
import com.thatzit.oib.stamptourboard.helper.Constants;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiPost;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.PostsRes;
import com.thatzit.oib.stamptourboard.util.ProgressWaitDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by inbum on 2017-08-31.
 */

public class StampTourBoardActivity extends AppCompatActivity implements View.OnClickListener{

    private StampTourBoardConfig config;
    private ProgressWaitDialog progressWaitDialog;
    private ImageButton toolbar_btn_close;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PostBoardRecyclerAdapter postBoardRecyclerAdapter;
    private TextView textViewNone;
    private FloatingActionButton fabBoardWrite;

    int pageno = 0;
    int postTotalCount = 0;
    Boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postboard);

        progressWaitDialog = new ProgressWaitDialog(this);

        /* This should not happen */
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null) {
            finish();
            return;
        }

        config = getConfig();

        setLayout(config);
        setItem(config);
    }

    private StampTourBoardConfig getConfig() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        config = bundle.getParcelable(StampTourBoardConfig.class.getSimpleName());
        return config;
    }

    private void setLayout(final StampTourBoardConfig config) {
        toolbar_btn_close = (ImageButton) findViewById(R.id.postboard_toolbar_btn_close);
        recyclerView = (RecyclerView) findViewById(R.id.scrollableview);
        linearLayoutManager = new LinearLayoutManager(StampTourBoardActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        postBoardRecyclerAdapter = new PostBoardRecyclerAdapter(this, config, boardListener);
        recyclerView.setAdapter(postBoardRecyclerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == itemTotalCount) {

                    if ( !isLoading && postTotalCount > recyclerView.getAdapter().getItemCount() ){
                        isLoading = true;

                        getPostData();
                    }
                }

            }
        });

        textViewNone = (TextView) findViewById(R.id.textView_postboard_none);

        fabBoardWrite = (FloatingActionButton)  findViewById(R.id.btn_postboard_write);
        fabBoardWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StampTourBoardActivity.this, StampTourBoardWriteActivity.class);
                intent.putExtra(StampTourBoardConfig.class.getSimpleName(), config);
                startActivityForResult(intent, Constants.REQUEST_CODE_POSTWRITESTART);
            }
        });

        toolbar_btn_close.setOnClickListener(this);
    }

    public void setItem(StampTourBoardConfig config){

        progressWaitDialog.show();
        getPostData();

    }

    public void getPostData(){
        BoardApiPost boardApiPost = ServiceGenerator.createBoardService(BoardApiPost.class);

        Call<PostsRes> postsResCall = null;

        postsResCall = boardApiPost.getPosts( String.valueOf(pageno) );

        postsResCall.enqueue(new Callback<PostsRes>() {
            @Override
            public void onResponse(Call<PostsRes> call, Response<PostsRes> response) {
                isLoading = false;
                if ( progressWaitDialog != null  ) {
                    progressWaitDialog.dismiss();
                }

                if ( response.isSuccessful() ){
                    //
                    //
                    PostsRes postsRes = response.body();

                    if ( pageno == 0 ) {
                        postBoardRecyclerAdapter.removelist();

                        if ( postsRes.getPosts().isEmpty() ){
                            recyclerView.setVisibility(View.GONE);
                            textViewNone.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            textViewNone.setVisibility(View.GONE);
                        }
                    }

                    for(PostsRes.PostData postData: postsRes.getPosts()){
                        postBoardRecyclerAdapter.additem(postData);
                    }

                    postTotalCount = postsRes.getCount();

                    postBoardRecyclerAdapter.notifyDataSetChanged();
                    pageno++;
                } else {
                    Toast.makeText(StampTourBoardActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<PostsRes> call, Throwable t) {
                isLoading = false;

                if ( progressWaitDialog != null  ) {
                    progressWaitDialog.dismiss();
                }

                Toast.makeText(StampTourBoardActivity.this, R.string.server_not_good, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.postboard_toolbar_btn_close){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == Constants.REQUEST_CODE_POSTWRITESTART && resultCode == Constants.RESULT_POSTWRITE_COMPLETED ){
            pageno = 0;
            postTotalCount = 0;

            isLoading = true;

            getPostData();
        } else if ( requestCode == Constants.REQUEST_CODE_POSTWRITESTART && resultCode == Constants.INVALIDACCESSTOKEN ){
            setResult(Constants.INVALIDACCESSTOKEN);
            finish();
        }
    }

    BoardListener boardListener = new BoardListener() {
        @Override
        public void postDeleteSuccess() {

            pageno = 0;
            postTotalCount = 0;

            isLoading = true;

            getPostData();
        }

        @Override
        public void commentDeleteSuccess() {
            return;
        }
    };


    @Override
    protected void onDestroy() {
        if ( progressWaitDialog != null ){
            progressWaitDialog.dismiss();
            progressWaitDialog = null;
        }
        super.onDestroy();
    }
}
