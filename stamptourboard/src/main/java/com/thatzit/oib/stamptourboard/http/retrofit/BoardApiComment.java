package com.thatzit.oib.stamptourboard.http.retrofit;


import com.thatzit.oib.stamptourboard.model.CommentsModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by inbum on 2017-03-22.
 */

public interface BoardApiComment {
    @GET("comments/{id}")
    Call<List<CommentsModel>> getComments(@Path("id") String _id);

    @FormUrlEncoded
    @POST("comments/")
    Call<ResponseBody> postComments(@Field("_id") String _id,
                                    @Field("Nick") String nick,
                                    @Field("AccessToken") String accesstoken,
                                    @Field("applicationId") String applicationId,
                                    @Field("contents") String contents);



    @FormUrlEncoded
    @HTTP(method = "DELETE",path="comments", hasBody = true)
    Call<ResponseBody> deleteComment(@Field("_id") String _id,
                                     @Field("comment_id") String comment_id,
                                     @Field("Nick") String nick,
                                     @Field("AccessToken") String accesstoken,
                                     @Field("applicationId") String applicationId);

}
