## PangJiao ##

### compile ###
1.at project  `build.gradle`  add



2.at app `build.gradle`  add

```javascript
dependencies {
	...
    compile 'ljk.android.pangjiao:pangjiao:1.0.2'
    annotationProcessor 'ljk.android.pangjiao:pangjiao:1.0.2'
    }
```

```javascript
 android {
  
    compileOptions {
		...
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```



###initialize ###

```java
  public class TESTApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PangJiao.init(this);
    }
}
```
### use injectview###
1.use in activity

 

```java
 public class MainActivity extends PJAppCompatActivity  {

    @InitView(R.id.tv_title)
    public TextView tvTitle;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        tvTitle.setText("hello ");
    }

    @OnClick(R.id.tv_title)
    public void tvClick() {
        Toast.makeText(this, tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
    }
    
    @OnClick(R.id.tv_title)
    public View.OnClickListener tvTitle_Click=v -> {
        Toast.makeText(this, tvTitle.getText().toString(), Toast.LENGTH_SHORT).show();
    };
    
    @OnClick({R.id.tv_title,R.id.tv_text})
    public void onClick(View view){
    
    
    }
    

```
Or
```java

 PangJiao.inject(activity);

```
2.use in fragment

```java

 public class TESTFragment extends PJFragment {

    @InitView(id = R.id.btn_test)
    public Button btnTest;


    @Override
    protected int initView() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {
        btnTest.setText("hello pangjiao");
    }
}

```
 Or

```java

 PangJiao.inject(view, object);

```

### use MVP###

&nbsp;&nbsp;in pangjiao framwork hava **@Service** **@Presenter** annotation,People who know about spring know @Autowire.at pangjiao hava **@Autowire** and **@AutowireProxy**.
&nbsp;&nbsp;pangjiao recommend that you subcontract your code to the following directory.

- ui (package)
-activity (package)
-fragment (package)


- service
-interface(.java)
-imp (package)


- application
-interface(.java)
-imp (package)

----------
Begin to use pangjia

 application tier interface new interface
```java

public interface IMemberPresent extends IPresenter {

    void login(String name, String pwd);
}

```
add imp

```java

@Presenter
public class MemberPresent implements IMemberPresent {


    @Autowire
    public IAppService appService;

    @TargetView
    public IMemberView memberView;

   
    @Override
    public void onDestroy() {
        this.memberView = null;
    }


    @Override
    public void login(String name, String pwd) {
    	appService.login(name, pwd);
    }
}
```

ui tier 

```java

public class MainActivity extends PJAppCompatActivity implements IMemberView {
   
    @AutowireProxy
    public IMemberPresent memberPresent;


    @Override
    public int initView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        memberPresent.login("Tom", "123456");
    }

    @Override
    public void refresh(Object o) {

    }

}

``` 

### net module ###
Default POST violation,Request body JSON format.The underlying layer is the HttpURLConnection implementation.

```java

@Net(api = "http://www.baidu.com", connectTimeOut = 10000)
public class RequestActive extends NetModel<ResponseActive> {

    private String ActiveCode;

    public String getActiveCode() {
        return ActiveCode;
    }

    public void setActiveCode(String ActiveCode) {
        this.ActiveCode = ActiveCode;
    }
}


public class ResponseActive extends ResponseBase {

    private boolean issuccess;
    private String msg;
    private String data;

    public boolean isIssuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

``` 

how use?
xx.excute();//Thread synchronization

```java
   
    public ResponseActive active(String activeCode) {
        RequestActive requestActive = new RequestActive();
        requestActive.setActiveCode(activeCode);
        return requestActive.execute();
    }
    
```

Use @Autowireproxy when injecting objects into the UI layer.

The @autowireproxy will inject a proxy object that will be executed **in the thread**.

So the network requests in the UI tier Application layer do not need to start the threads themselves.

The object that needs to be managed by the pangjiao container needs to be added to **@service** or **@Presenter**.

