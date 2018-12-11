# BigHome  
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
![Postman](图片地址 "Postman")  

## 界面UI设计  

**LauncherIcon**  
你可以在Android Asset Studio上生成自己喜欢的Launcher Icon图标  
>https://romannurik.github.io/AndroidAssetStudio/index.html  

**界面里的图标Icon**  
为了遵循Material Design， 尽量在AS自带的图标里选取  
在本APP的设计中，我用BottomNavigationView作为顶级导航。界面的设计效果如下。  
![软件截图](图片地址 "截图")  

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
