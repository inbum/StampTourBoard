package com.thatzit.oib.stamptourboard.http.retrofit;

import com.thatzit.oib.stamptourboard.model.PostsRes;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by inbum on 2017-03-22.
 */

public interface BoardApiPost {
    @GET("posts/{pageno}")
    Call<PostsRes> getPosts(@Path("pageno") String pageno);

    @GET("posts/share/post/{id}")
    Call<PostsRes.PostData> getPost(@Path("id") String id);

    @GET("posts/search/townName/{townname}/{pageno}")
    Call<PostsRes> getTownPosts(@Path("townname") String townname, @Path("pageno") String pageno);

    @POST("posts/")
    Call<ResponseBody> postImage(@Body RequestBody requestBody);

    @GET("posts/post/{id}")
    Call<ResponseBody> updateViewCount(@Path("id") String post_id);

    @FormUrlEncoded
    @HTTP(method = "DELETE",path="posts/post/{id}", hasBody = true)
    Call<ResponseBody> deletePost(@Path("id") String id,
                                  @Field("Nick") String nick,
                                  @Field("AccessToken") String accesstoken,
                                  @Field("applicationId") String applicationId);

}
