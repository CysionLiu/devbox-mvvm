package com.cysion.usercenter.net

import com.cysion.ktbox.net.BaseCaller
import com.cysion.usercenter.entity.*
import com.cysion.usercenter.helper.UserCache
import okhttp3.OkHttpClient
import retrofit2.http.*

object UserCaller : BaseCaller<UserApi>(UserUrls.HOST, UserApi::class.java) {

    override fun configOkClientBuilder(builder: OkHttpClient.Builder) {
        builder.addInterceptor {
            val b = it.request().newBuilder()
            b.addHeader("userid", UserCache.userId)
            b.addHeader("token", UserCache.token)
            it.proceed(b.build())
        }
    }
}

interface UserApi {

    //    顶部轮播
    @GET("toploopers")
    suspend fun getCarousel(): ApiResult<MutableList<Carousel>>

    //注册
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("password2") password2: String
    ): ApiResult<UserEntity>

    //登录
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResult<UserEntity>


    //更新用户信息
    @FormUrlEncoded
    @POST("updateuser")
    suspend fun updateUserInfo(
        @Field("nickname") nickname: String,
        @Field("desc") desc: String,
        @Field("avatar") avatar: String = ""
    ): ApiResult<UserEntity>

    //获取用户详情
    @POST("userdetail")
    suspend fun getUserInfo():ApiResult<UserEntity>

//    以下，博客相关

    //获取博客列表，时间顺序
    @GET("blog/list")
    suspend fun getBlogList(@Query("page") page: Int = 1): ApiResult<MutableList<Blog>>


    //获取某个用户的博客
    @GET("blog/userlist")
    suspend fun getUserBlogList(@Query("page") page: Int): ApiResult<MutableList<Blog>>

    //获取某个博客详情
    @GET("blog/get/{blogId}")
    suspend fun getBlog(@Path("blogId") blogId: String): ApiResult<Blog>

    //删除博客
    @FormUrlEncoded
    @POST("blog/del")
    suspend fun delBlog(@Field("blogId") blogId: String): ApiResult<Any?>


    //创建博客
    @FormUrlEncoded
    @POST("blog/add")
    suspend fun createBlog(@Field("title") title: String, @Field("text") text: String):ApiResult<Any?>


    //更新博客
    @FormUrlEncoded
    @POST("blog/update")
    suspend fun updateBlog(
        @Field("title") title: String, @Field("text") text: String
        , @Field("blogId") blogId: String
    ): ApiResult<Any?>


    //点赞博客
    @FormUrlEncoded
    @POST("blog/pride")
    suspend fun prideBlog(@Field("blogId") blogId: String): ApiResult<Any?>


    //取消点赞博客
    @FormUrlEncoded
    @POST("blog/unpride")
    suspend fun unPrideBlog(@Field("blogId") blogId: String): ApiResult<Any?>

    //收藏博客
    @FormUrlEncoded
    @POST("blog/collect")
    suspend fun collectBlog(@Field("itemId") blogId: String): ApiResult<Any?>


    //取消收藏博客
    @FormUrlEncoded
    @POST("blog/uncollect")
    suspend fun unCollectBlog(@Field("itemId") blogId: String): ApiResult<Any?>


    //博客收藏列表
    @POST("blog/collections")
    suspend fun getCollectList(@Query("colType") colType: String = "0"): ApiResult<MutableList<CollectEntity>>


    //评论博客
    @FormUrlEncoded
    @POST("blog/comment")
    suspend fun commentBlog(
        @Field("parentId") parentId: String,
        @Field("content") content: String
    ): ApiResult<Any?>


    //获取评论列表，时间顺序
    @GET("blog/comments/list")
    suspend fun getComments(@Query("parentId") parentId: String): ApiResult<MutableList<CommentEntity>>


    //获得某个用户的详情
    @POST("userinfo")
    suspend fun getPeopleInfo(
        @Body jsonBody: Map<String,String>
    ): ApiResult<DetailUserEntity>

}