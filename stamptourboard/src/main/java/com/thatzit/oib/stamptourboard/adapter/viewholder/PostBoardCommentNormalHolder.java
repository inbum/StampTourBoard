package com.thatzit.oib.stamptourboard.adapter.viewholder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thatzit.oib.stamptourboard.R;
import com.thatzit.oib.stamptourboard.StampTourBoardConfig;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiComment;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.CommentsModel;
import com.thatzit.oib.stamptourboard.model.PostsRes;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by inbum on 2017-02-02.
 */

public class PostBoardCommentNormalHolder extends BaseViewHolder<CommentsModel> {

    private Context context;
    private PostsRes.PostData postData;
    private BoardListener boardListener;

    private StampTourBoardConfig config;

    private Button btnDelete;
    private TextView nick;
    private TextView contents;
    private TextView created;


    public static PostBoardCommentNormalHolder newInstance(Context context, ViewGroup parent, PostsRes.PostData postData, StampTourBoardConfig config, BoardListener boardListener) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_postboardcomment_normal, parent, false);
        return new PostBoardCommentNormalHolder(context, itemView, postData, config, boardListener);
    }

    public PostBoardCommentNormalHolder(Context context, View itemView, PostsRes.PostData postData, StampTourBoardConfig config, BoardListener boardListener) {
        super(itemView);
        this.context = context;
        this.postData = postData;
        this.config = config;
        this.boardListener = boardListener;
        btnDelete = (Button) itemView.findViewById(R.id.button_item_postboardcomment_delete);
        nick = (TextView) itemView.findViewById(R.id.textView_item_postboardcomment_nick);
        contents = (TextView) itemView.findViewById(R.id.textView_item_postboardcomment_content);
        created = (TextView) itemView.findViewById(R.id.textView_item_postboardcomment_created);
    }

    @Override
    public void onBindView(final CommentsModel commentsModel, final int position) {

        if ( commentsModel.getPostedBy().getApplicationId().equals( config.getApplicationID() ) &&
                commentsModel.getPostedBy().getNick().equals( config.getUserNick() )
                ){
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.delete_dialog_title);
                    alert.setMessage(R.string.delete_dialog_commentcontent);
                    alert.setPositiveButton(R.string.delete_dialog_yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();


                            final BoardApiComment boardApiComment = ServiceGenerator.createBoardService(BoardApiComment.class);

                            Call<ResponseBody> postsResCall = boardApiComment.deleteComment(
                                    postData.get_id(), commentsModel.get_id(),
                                    config.getUserNick(), config.getUserAccesstoken(), config.getApplicationID()
                            );
                            postsResCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if ( response.isSuccessful() ){
                                        //
                                        //
                                        Toast.makeText(context, R.string.postboard_deletecomment_success, Toast.LENGTH_SHORT).show();

                                        if ( boardListener != null ){
                                            boardListener.commentDeleteSuccess();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    Toast.makeText(context, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    });

                    alert.setNegativeButton(R.string.delete_dialog_no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();


                }
            });
        } else {
            btnDelete.setVisibility(View.GONE);
        }



        nick.setText(commentsModel.getPostedBy().getNick());
        contents.setText(commentsModel.getContents());
        created.setText(commentsModel.getCreatedTimeStr());
    }

}
