package com.thatzit.oib.stamptourboard.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.thatzit.oib.stamptourboard.R;
import com.thatzit.oib.stamptourboard.StampTourBoardCommentDetailActivity;
import com.thatzit.oib.stamptourboard.StampTourBoardConfig;
import com.thatzit.oib.stamptourboard.StampTourBoardDetailActivity;
import com.thatzit.oib.stamptourboard.helper.ExtraName;
import com.thatzit.oib.stamptourboard.http.retrofit.BoardApiPost;
import com.thatzit.oib.stamptourboard.http.retrofit.ServiceGenerator;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.PostsRes;
import com.thatzit.oib.stamptourboard.util.ProgressWaitDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostBoardRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private BoardListener boardListener;
    private StampTourBoardConfig config;
    private ProgressWaitDialog progressWaitDialog;
    private final SparseBooleanArray mCollapsedStatus;
    private ArrayList<Object> mListData = new ArrayList<Object>();
    private PostBoardListItemImageAdapter postBoardListItemImageAdapter;

    public PostBoardRecyclerAdapter(Context context, StampTourBoardConfig config, BoardListener boardListener) {
        this.context = context;
        this.boardListener = boardListener;
        this.config = config;
        mCollapsedStatus = new SparseBooleanArray();
        progressWaitDialog = new ProgressWaitDialog(context);
    }

    public PostBoardRecyclerAdapter(Context context, StampTourBoardConfig config, BoardListener boardListener, ArrayList<Object> mListData) {
        this.context = context;
        this.boardListener = boardListener;
        this.config = config;
        mCollapsedStatus = new SparseBooleanArray();
        this.mListData = mListData;
        progressWaitDialog = new ProgressWaitDialog(context);
    }
    public Object getmListData(int position) {
        return mListData.get(position);
    }
    public void additem(Object data){
        this.mListData.add(data);
    }
    public void removeitem(int position){
        this.mListData.remove(position);
    }
    public void removelist(){
        this.mListData.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postboardlistitem, parent, false);
        return new PostBoardViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PostsRes.PostData postData = (PostsRes.PostData) mListData.get(position);

        if ( postData.getFiles() != null && !postData.getFiles().isEmpty()){

            postBoardListItemImageAdapter = new PostBoardListItemImageAdapter(context, postData.getFiles());
            ((PostBoardViewHolder)holder).recyclerViewImage.setAdapter(postBoardListItemImageAdapter);
            ((PostBoardViewHolder)holder).recyclerViewImage.setVisibility(View.VISIBLE);
            ((PostBoardViewHolder)holder).imageCount.setVisibility(View.VISIBLE);
            ((PostBoardViewHolder)holder).imageCount.setText(
                    Html.fromHtml( "<strong>1</strong>" + " / " + postData.getFiles().size())
            );

            ((PostBoardViewHolder)holder).imageViewLoadImg.setVisibility(View.GONE);

        } else {
            ((PostBoardViewHolder)holder).recyclerViewImage.setVisibility(View.GONE);
            ((PostBoardViewHolder)holder).imageCount.setVisibility(View.GONE);
            ((PostBoardViewHolder)holder).imageViewLoadImg.setVisibility(View.VISIBLE);
        }


        ((PostBoardViewHolder)holder).townName.setText( postData.getTownName() );


        ((PostBoardViewHolder)holder).applicationName.setText( postData.getApplicationId() );


        ((PostBoardViewHolder)holder).title.setText( postData.getTitle() );


        ((PostBoardViewHolder)holder).nick.setText( postData.getPostedBy().getNick() );
        ((PostBoardViewHolder)holder).createdTime.setText( postData.getCreatedTimeStr() );
        ((PostBoardViewHolder)holder).views.setText(
                String.format("조회 %d", postData.getViews())
        );
        ((PostBoardViewHolder)holder).contents.setText( postData.getContents(), mCollapsedStatus, position );


        if ( postData.getApplicationId().equals(config.getApplicationID()) &&
                postData.getPostedBy().getNick().equals(config.getUserNick())
                ){
            ((PostBoardViewHolder)holder).btnDelete.setVisibility(View.VISIBLE);
            ((PostBoardViewHolder)holder).btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(R.string.delete_dialog_title);
                    alert.setMessage(R.string.delete_dialog_content);
                    alert.setPositiveButton(R.string.delete_dialog_yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                            progressWaitDialog.show();

                            final BoardApiPost boardApiPost = ServiceGenerator.createBoardService(BoardApiPost.class);

                            Call<ResponseBody> postsResCall = boardApiPost.deletePost(
                                    postData.get_id(),
                                    config.getUserNick(), config.getUserAccesstoken(), config.getApplicationID()
                            );
                            postsResCall.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if ( progressWaitDialog != null  ) {
                                        progressWaitDialog.dismiss();
                                    }

                                    if ( response.isSuccessful() ){
                                        //

                                        //
                                        Toast.makeText(context, R.string.postboard_delete_success, Toast.LENGTH_SHORT).show();

                                        if ( boardListener != null ){
                                            boardListener.postDeleteSuccess();
                                        }
                                    } else {
                                        Toast.makeText(context, R.string.server_not_good, Toast.LENGTH_SHORT).show();
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    if ( progressWaitDialog != null  ) {
                                        progressWaitDialog.dismiss();
                                    }

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
            ((PostBoardViewHolder)holder).btnDelete.setVisibility(View.GONE);
        }



        ((PostBoardViewHolder)holder).btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StampTourBoardDetailActivity.class);
                intent.putExtra(ExtraName.POST_DATA.getName(), postData);
                intent.putExtra(StampTourBoardConfig.class.getSimpleName(), config);
                context.startActivity(intent);
            }
        });


        ((PostBoardViewHolder)holder).btnWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StampTourBoardCommentDetailActivity.class);
                intent.putExtra(ExtraName.POST_DATA.getName(), postData);
                intent.putExtra(StampTourBoardConfig.class.getSimpleName(), config);
                context.startActivity(intent);
            }
        });


        ((PostBoardViewHolder)holder).btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareText = context.getResources().getString(R.string.share_string_text_prefix)
                        + config.getApplicationRegion()
                        + context.getResources().getString(R.string.share_string_text_postfix)
                        + "\n"
                        + "https://play.google.com/store/apps/details?id=" + config.getSharePacakgeName();



                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(shareIntent, 0);
                if (resInfo.isEmpty()) {
                    return;
                }

                Collections.sort(resInfo, new ResolveInfo.DisplayNameComparator(
                        context.getPackageManager()
                ));

                List<Intent> targetedShareIntents = new ArrayList<>();

                for (ResolveInfo resolveInfo : resInfo) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    Intent targetedShareIntent = new Intent(Intent.ACTION_SEND);

                    if ( packageName.contains("com.facebook.katana") )
                    {
                        targetedShareIntent.setType("text/plain");
                        ComponentName name = new ComponentName(packageName, resolveInfo.activityInfo.name);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        targetedShareIntent.setComponent(name);
                        targetedShareIntent.setPackage(packageName);
                        targetedShareIntents.add(targetedShareIntent);
                    }
                    else if ( packageName.contains("com.kakao.talk") && resolveInfo.activityInfo.name.equals("com.kakao.talk.activity.SplashConnectActivity")  )
                    {
                        targetedShareIntent.setType("text/plain");
                        ComponentName name = new ComponentName(packageName, resolveInfo.activityInfo.name);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        targetedShareIntent.setComponent(name);
                        targetedShareIntent.setPackage(packageName);
                        targetedShareIntents.add(targetedShareIntent);
                    } else {
                        targetedShareIntent.setType("text/plain");
                        ComponentName name = new ComponentName(packageName, resolveInfo.activityInfo.name);
                        targetedShareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                        targetedShareIntent.setComponent(name);
                        targetedShareIntent.setPackage(packageName);
                        targetedShareIntents.add(targetedShareIntent);
                    }
                }

                if (targetedShareIntents.isEmpty()) {
                    return;
                }

                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), context.getString(R.string.share_title));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                context.startActivity(chooserIntent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData == null ? 0 : mListData.size();
    }

    class PostBoardViewHolder extends RecyclerView.ViewHolder{
        public RecyclerView recyclerViewImage;
        public TextView imageCount;
        public ImageView imageViewLoadImg;
        public TextView townName;
        public TextView applicationName;
        public TextView title;

        public TextView nick;
        public TextView createdTime;
        public TextView views;
        public TextView reviews;
        public ExpandableTextView contents;

        public Button btnDelete;
        public LinearLayout btnViewMore;
        public LinearLayout btnWriteComment;
        public LinearLayout btnShare;

        public PostBoardViewHolder(View itemView) {
            super(itemView);
            recyclerViewImage = (RecyclerView) itemView.findViewById(R.id.recyclerview_postboardlistitem_images);
            recyclerViewImage.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            imageCount = (TextView) itemView.findViewById(R.id.textView_postboardlistitem_imagecount);
            imageViewLoadImg = (ImageView) itemView.findViewById(R.id.imageView_postboardlistitem_loading);



            recyclerViewImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int currentpage = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1;
                    imageCount.setText(
                            Html.fromHtml( "<strong>" + currentpage + "</strong>" + " / " + recyclerView.getAdapter().getItemCount())
                    );
                }
            });


            townName = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_townname);
            applicationName = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_application);
            title = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_title);

            nick = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_nick);
            createdTime = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_created);
            views = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_views);
            reviews = (TextView)itemView.findViewById(R.id.textView_postboardlistitem_reviews);
            contents = (ExpandableTextView)itemView.findViewById(R.id.textView_postboardlistitem_contents);

            btnDelete =(Button) itemView.findViewById(R.id.button_postboardlistitem_delete);
            btnViewMore = (LinearLayout) itemView.findViewById(R.id.btn_postboardlistitem_viewmore);
            btnWriteComment = (LinearLayout) itemView.findViewById(R.id.btn_postboardlistitem_writecomment);
            btnShare = (LinearLayout) itemView.findViewById(R.id.btn_postboardlistitem_share);
        }
    }

}