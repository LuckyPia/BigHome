# 智慧小区APP-邻里  
本APP采用了MVP+Retrofit+RXJAVA的架构。是一款基于**嘉宝生活家APP**进行开发的应用。  


## 需求设计  

在写APP之前，我们应当首先思考下这个APP应该要实现什么功能。查看嘉宝生活家APP，我们能够提取出以下基本需求（不包括API所有需求）。  
   1. 能够实现用户登录/注册，持久化cookies  
   2. 能够获取首页Banner数据并显示  
   3. 能够获取小区附近的商城信息  
   4. 能够查看/修改个人资料  
   5. 能够实现邻里交流  
   
下面是我自己提出的附加需求  
   1. 实现侧滑返回


## 数据API分析  

功能需求设计好后，我们对这个APP也能去规划一个大概的蓝图了。APP都是建立在数据的基础之上的，所以对API的分析是一个很重要的模块。我们来看下后台给了我们什么样的数据。  

以登录API为例：  
>http://www.liuchaun.com/wisdom_newest/login  

```json
{
    "code": 100,
    "msg": "处理成功！",
    "extendMap": {
        "go": "index"
    }
}
```  

从返回的数据我们可以看到处理号code,处理信息msg,扩展集合extendMap等数据，处理号代表服务器是否出现错误，处理信息成功就代表请求成功了，go代表跳转到哪儿去。  

这里要推荐下一款好用的网页调试软件Postman。当你在postman发送相关的API后，Postman就会返回可读性强的结果。  
>https://www.getpostman.com/  
![Postman](https://github.com/LuckyPia/BigHome/blob/master/postman截图.png "Postman")  

## 界面UI设计  

**LauncherIcon**  
你可以在Android Asset Studio上生成自己喜欢的Launcher Icon图标  
>https://romannurik.github.io/AndroidAssetStudio/index.html  

**界面里的图标Icon**  
为了遵循Material Design， 尽量在AS自带的图标里选取  
在本APP的设计中，我用BottomNavigationView作为顶级导航。界面的设计效果如下。  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/1.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/2.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/3.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/4.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/5.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/6.jpg "截图")  
![软件截图](https://github.com/LuckyPia/BigHome/blob/master/7.jpg "截图")  

## 代码编写(以登录为例）

**Bean类代码编写（只显示关键代码）**  

根据API我们可以获取相应的JSON数据，现在做的就是把这些数据转化成Bean类  
LoginData  

```java
public class LoginData {


    public LoginDetailData getExtendMap() {
        return extendMap;
    }

    public void setExtendMap(LoginDetailData extendMap) {
        this.extendMap = extendMap;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Expose
    @SerializedName("extendMap")
    private LoginDetailData extendMap;
    @Expose
    @SerializedName("code")
    private int code;
    @Expose
    @SerializedName("msg")
    private String msg;
    
}
```  

LoginDetailData  

```java
public class LoginDetailData extends RealmObject {

    @Expose
    @SerializedName("go")
    private String go;

    public String getGo() {
        return go;
    }

    public void setGo(String go) {
        this.go = go;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Expose
    @SerializedName("username")
    private String username;

    @Expose
    @SerializedName("password")
    private String password;

}
```  

**网络请求库代码编写（只显示关键代码）**  

在网络请求框架上我选择的是Retrofit+RxJava。  

首先就是写API  
```java
public class Api {

    //This is the base API.
    public static  String API_BASE = "http://www.liuchaun.com/";

    //Get the banner
    public static  String BANNER = "wisdom_newest/img/slide2.jpg";

    //Login
    public static  String LOGIN = "wisdom_newest/login";

    //Register
    public static  String REGISTER = "wisdom_newest/saveuser";

    public static  String PROFILE = "wisdom_newest/profile";
    public static  String SVAE_USER = "wisdom_newest/saveuser";
    
    public static final String SHOP_URL="http://api.map.baidu.com/place/v2/search";
    
}
```  
接下来是创建相关的Retrofit文件。  

RetrofitClient  
```java
public class RetrofitClient {
    private RetrofitClient() {
    }

    private static class ClientHolder{

        private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieManger(MyApp.getContext()))
                .build();

        private static Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.API_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static Retrofit getInstance(){
        return ClientHolder.retrofit;
    }
}
```  
RetrofitService  
```java
public interface RetrofitService {

    @FormUrlEncoded
    @POST()
    Observable<LoginData> login(@Url String path, @Field("name") String username, @Field("password") String password);

    @Multipart
    @POST()
    Observable<LoginData> profile(@Url String path, @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST()
    Observable<LoginData> saveUser(@Url String path,@Field("name") String username, @Field("password") String password,@Field("iconpath") String iconpath);

    @GET()
    Observable<BannerData> getBanner(@Url String path);

    @GET()
    Observable<ResponseBody> getShop(@Url String path, @Query("query") String query, @Query("region") String region, @Query(value="output",encoded = true) String output, @Query(value="ak",encoded = true) String ak);
}
```


你可能会问什么是Rxjava，Rxjava就是在观察者模式的骨架下，通过丰富的操作符和便捷的异步操作来完成对于复杂业务的处理的框架。可以参考扔物线写的《给 Android 开发者的 RxJava 详解》 https://gank.io/post/560e15be2dca930e00da1083 ，需要指出的是里面有些内容是过时的，不过这篇文章对RxJava讲的很浅显易懂，所以还是推荐它用于入门。  

**Model层构建（只显示关键代码）**

LoginDataSource  
```java
public interface LoginDataSource {

    Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType);

    Observable<LoginDetailData> getLocalLoginData(@NonNull int userId);
    
}
```  
LoginDataRemoteSource  
```java
public class LoginDataRemoteSource implements LoginDataSource {
    @NonNull
    private static LoginDataRemoteSource INSTANCE;

    private LoginDataRemoteSource(){

    }

    public static LoginDataRemoteSource getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRemoteSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        Observable<LoginData> loginDataObservable = null;


        if (loginType==LoginType.TYPE_REGISTER){
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)//表单类型
                    .addFormDataPart("uname",userName);
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), encodeImage());
            builder.addFormDataPart("file", "img", imageBody);//imgfile 后台接收图片流的参数名

            List<MultipartBody.Part> parts = builder.build().parts();

            loginDataObservable = RetrofitClient.getInstance()
                    .create(RetrofitService.class)
                    .profile(Api.PROFILE,parts);
            return loginDataObservable;

        }else{

            loginDataObservable=RetrofitClient.getInstance()
                    .create(RetrofitService.class)
                    .login(Api.LOGIN,userName, password);

            return loginDataObservable
                    .subscribeOn(Schedulers.io())
                    .doOnNext(new Consumer<LoginData>() {
                        @Override
                        public void accept(LoginData loginData) {
                /*if (loginData.getCode()!=200||loginData.getExtendMap() != null) {
                    // It is necessary to build a new realm instance
                    // in a different thread.
                    Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                            .name(RealmHelper.DATABASE_NAME)
                            .deleteRealmIfMigrationNeeded()
                            .build());

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(loginData.getExtendMap());
                    realm.commitTransaction();
                    realm.close();
                }*/
                        }
                    });
        }

    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        //Not required because the {@link LoginDataLocalSource} has handled it
        return null;
    }

    public static String encodeImage(){
        Bitmap bitmap=BitmapFactory.decodeResource(MyApp.getContext().getResources(), R.drawable.bg_messages);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //读取图片到ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos); //参数如果为100那么就不压缩
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

}
```  
LoginDataRepository  
```java
public class LoginDataRepository implements LoginDataSource{
    @NonNull
    private LoginDataSource localDataSource;
    @NonNull
    private LoginDataSource remoteDataSource;

    private LoginDataRepository(@NonNull LoginDataSource localDataSource, @NonNull LoginDataSource remoteDataSource){
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }
    @NonNull
    private static LoginDataRepository INSTANCE;

    public static LoginDataRepository getInstance(@NonNull LoginDataSource localDataSource,@NonNull LoginDataSource remoteDataSource){
        if (INSTANCE == null) {
            INSTANCE = new LoginDataRepository(localDataSource, remoteDataSource);
        }
        return INSTANCE;
    }

    @Override
    public Observable<LoginData> getRemoteLoginData(@NonNull String userName, @NonNull String password, @NonNull LoginType loginType) {
        return remoteDataSource.getRemoteLoginData(userName, password,loginType);
    }

    @Override
    public Observable<LoginDetailData> getLocalLoginData(@NonNull int userId) {
        return localDataSource.getLocalLoginData(userId);
    }
    
}
```  

**界面代码编写（只显示关键代码）**  
子界面xml  
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical">

    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        app:contentInsetLeft="0dp"
        app:contentInsetStart="0dp"
        android:background="@color/colorPrimary"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light">
        <TextView
            android:id="@+id/tv_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_horizontal"
            android:textSize="18sp"
            android:textColor="@color/colorPrimaryText"
            android:gravity="center"/>
        <TextView
            android:id="@+id/tv_right"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="right"
            android:visibility="invisible"
            android:textSize="14sp"
            android:gravity="center"/>
    </android.support.v7.widget.Toolbar>

    <FrameLayout
        android:id="@+id/frame_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```  

**View层和Present层**  
既然采用的是MVP+RXJAVA，如果你对此不是很懂的话，可以看下谷歌的官方项目todo‑mvp‑rxjava https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava/ 。  

MVP就指的是Mpdel-View-Present。MVP模式的核心思想就是把视图中的UI逻辑抽象成View接口，把业务逻辑抽象成Presenter接口。也就是视图就只负责显示，其它的逻辑都交给了Presenter。这样就大大降低了代码的耦合，提高代码的可阅读性。  
![Modle-View-Presenter](https://github.com/LuckyPia/BigHome/blob/master/640.webp "Modle-View-Presenter")  

Model层我们前面已经写好了，那么就剩下View层和Present层了。首先就是写BasePresenter和BaseView。  
BasePresenter  
```java
public interface BasePresenter {
    void subscribe();
    void unSubscribe();
}
```  
BaseView  
```java
public interface BaseView<T> {
    void initViews(View view);
    void setPresenter(T presenter);
}
```  
LoginContract,契约类  
```java
public interface LoginContract {
    interface Presenter extends BasePresenter {
        void login(String username, String password, @NonNull LoginType loginType);

        void clearReadLaterData();

    }

    interface View extends BaseView<Presenter> {
        void showLoginError(String errorMsg);

        boolean isActive();

        void saveUserPreference(LoginDetailData loginDetailData);

        void showNetworkError();
    }
}
```  
LoginPresenter,从Model层获取数据  
```java
public class LoginPresenter implements LoginContract.Presenter{

    @NonNull
    private LoginContract.View view;
    @NonNull
    private LoginDataRepository repository;

    private CompositeDisposable compositeDisposable;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull LoginDataRepository loginDataRepository) {
        this.view = view;
        this.repository = loginDataRepository;
        this.view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String username, String password, @NonNull LoginType loginType) {
        getLoginData(username, password,loginType);
    }

    private void getLoginData(final String username, final String password, @NonNull final LoginType loginType){
        Disposable disposable=repository.getRemoteLoginData(username, password,loginType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LoginData>() {

                    @Override
                    public void onNext(LoginData value) {
                        if (!view.isActive()) {
                            return;

                        }
                        if (value.getCode()==200){
                            view.showLoginError(value.getMsg());
                        }else {
                            value.getExtendMap().setUsername(username);
                            value.getExtendMap().setPassword(password);

                            view.saveUserPreference(value.getExtendMap());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view.isActive()) {
                            view.showNetworkError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void clearReadLaterData() {
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
```  

LoginFragment  
>代码量过大这里只展示一部分 
```java
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);

        terms();
        cbTerms.setChecked(sp.getBoolean(SettingsUtil.TERMS_OF_SERVICE, false));
        cbAutoLogin.setChecked(sp.getBoolean(SettingsUtil.AUTO_LOGIN, false));
        cbRememberPassword.setChecked(sp.getBoolean(SettingsUtil.REMEMBER_PASSWORD, false));

        if(cbTerms.isChecked()){
            btnLogin.setBackgroundDrawable(ContextCompat.getDrawable(loginActivity,R.drawable.button_selector));
            linkSignUp.setTextColor(ContextCompat.getColor(loginActivity,R.color.colorPrimaryText));
            tvTerms.setTextColor(ContextCompat.getColor(loginActivity,R.color.iron));
        }

        if(sp.getBoolean(SettingsUtil.REMEMBER_PASSWORD, false)){
            if (sp.getBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE, false)){
                String username=sp.getString(SettingsUtil.USERNAME, null);
                String password=sp.getString(SettingsUtil.PASSWORD, null);
                editUserName.setText(username);
                editPassword.setText(password);
            }
        }

        linearLayout.setOnClickListener(this);
        linearLayout.setOnTouchListener(this);
        linkSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        cbTerms.setOnCheckedChangeListener(this);
        cbAutoLogin.setOnCheckedChangeListener(this);
        cbRememberPassword.setOnCheckedChangeListener(this);
        tvTerms.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity = (LoginActivity) getActivity();
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        if(sp.getBoolean(SettingsUtil.AUTO_LOGIN, false)){
            if (sp.getBoolean(SettingsUtil.KEY_SKIP_LOGIN_PAGE, false)) {
                if(sp.getBoolean(SettingsUtil.TERMS_OF_SERVICE, false)){
                    showProgressDialog();
                    final String username=sp.getString(SettingsUtil.USERNAME, null);
                    final String password=sp.getString(SettingsUtil.PASSWORD, null);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            //Login
                            presenter.login(username,password, LoginType.TYPE_LOGIN);
                        }
                    }, 1000);
                }
            }
        }

    }
```  

至此，我们就完成登录功能的基本搭建了。没有细写显示数据是因为每一个人的设计理念都不一样，每个人都有不同的实现方式。  

接下来的获取商城信息等操作与这部分操作没有多大差异，就不一一细说了，大致的流程如下  
![流程图](https://github.com/LuckyPia/BigHome/blob/master/640(1).webp "流程")  

**侧滑返回的实现**  

关于侧滑返回，我认为这是一个非常有必要的功能，毕竟它能够很好的提升用户体验。  
这里我们使用了第三方开源库BGASwipeBackLayout-Android。  
导入就能使用，非常方便。  
>implementation 'cn.bingoogolapple:bga-swipebacklayout:1.2.0'  


## 结语  
本次邻里项目时间非常紧张，加之中间一段时间去忙其他项目，所以邻里还有很多功能未能实现，注册时上传头像实在是没时间做了，再加本身对智慧小区的一些功能未能掌握，所以本次的项目未能达到预期效果，非常抱歉！  

## 致谢  
本次项目用了很多第三方库，特别鸣谢它们提供的库！  
感谢我的三位队友，没有他们，我的结题报告真的不知道怎么写（笑哭）！  

## License
```
Copyright 2018 CoderLengary

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```  






