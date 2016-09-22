package uploadfile.cay.com.uploadfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import uploadfile.cay.com.uploadfile.Bean.UserBean;

/**
 * Created by C on 2016/9/22.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "cay";
    private LinearLayout singUpLayout;//注册账号信息界面
    private EditText singUpMail;//注册邮箱EditText
    private EditText singUpNikeName;//注册昵称EditText
    private EditText singUpPaaword;//注册密码EditText
    private EditText singUpRePassword;//注册确认密码EditText
    private Button singUpNext;//注册下一步Button
    private Button singUpBackLogin;//注册页面返回登陆Button
    private String upMail;//邮箱
    private String upNikeName;//昵称
    private String upPassword;//密码
    private String upRePassword;//确认密码
    private String code;//验证码
    private LinearLayout codeLayout;//验证码信息界面
    private Button codeButton;
    private LinearLayout successLayout;//成功界面
    private Button successBackLoginBtn;//返回登录界面
    private LinearLayout loginLayout;//登录界面
    private Button loginSingUpBtn;//切换到注册页面按钮
    private Button loginButton;//登录按钮
    private String username;//用户名
    private String password;//密码


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign);
        initViews();
        initOnClikListen();

    }

    /**
     * 初始化监听事件
     */
    private void initOnClikListen() {
        singUpNext.setOnClickListener(this);    //  注册页面信息下一步监听
        singUpBackLogin.setOnClickListener(this);//主页页面信息返回登录监听
        codeButton.setOnClickListener(this);//发送验证码进行验证监听
        successBackLoginBtn.setOnClickListener(this);//成功返回登录监听
        loginSingUpBtn.setOnClickListener(this);//切换到注册页面监听
        loginButton.setOnClickListener(this);//登录按钮监听
    }

    /**
     * 初始化所有控件
     */
    private void initViews() {
        /*****************注册信息页面控件*************************/
        singUpLayout = (LinearLayout) findViewById(R.id.sign_up_ll);
        singUpMail = (EditText) findViewById(R.id.sing_up_mail_et);
        singUpNikeName = (EditText) findViewById(R.id.sing_up_nikeName_et);
        singUpPaaword = (EditText) findViewById(R.id.sing_up_password_et);
        singUpRePassword = (EditText) findViewById(R.id.sing_up_rePassword_et);
        singUpNext = (Button) findViewById(R.id.sing_up_next_btn);
        singUpBackLogin = (Button) findViewById(R.id.sing_up_backLogin_btn);
        /*****************验证码信息页面控件*************************/
        codeLayout = (LinearLayout) findViewById(R.id.code_ll);
        codeButton = (Button) findViewById(R.id.code_next_btn);
        /*****************注册成功页面控件*************************/
        successLayout = (LinearLayout) findViewById(R.id.success_ll);
        successBackLoginBtn = (Button) findViewById(R.id.success_back_login_btn);
        /*****************登录页面控件*************************/
        loginLayout = (LinearLayout) findViewById(R.id.login_ll);
        loginSingUpBtn = (Button) findViewById(R.id.login_sing_up_btn);
        loginButton = (Button) findViewById(R.id.login_btn);
    }

    /**
     * 注册逻辑
     */
    private void singUp() {
        upMail = singUpMail.getText().toString();
        upNikeName = singUpNikeName.getText().toString();
        upPassword = singUpPaaword.getText().toString();
        upRePassword = singUpRePassword.getText().toString();

        if (!upMail.isEmpty() && !upNikeName.isEmpty() && !upPassword.isEmpty() && !upRePassword.isEmpty()) {
            if (upPassword.equals(upRePassword)) {
                if (upPassword.length() < 6) {
                    Toast.makeText(Login.this, "密码长度为6到25", Toast.LENGTH_SHORT).show();
                } else {
                    sendCode();
                    Log.i(TAG, "密码长度: "+upPassword.length());
                }

            } else {
                Toast.makeText(Login.this, "两次密码不一样", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (upMail.isEmpty()) {
                Toast.makeText(Login.this, "请输入邮箱", Toast.LENGTH_SHORT).show();
            } else {
                if (upNikeName.isEmpty()) {
                    Toast.makeText(Login.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                } else {
                    if (upPassword.isEmpty()) {
                        Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    } else {
                        if (upRePassword.isEmpty()) {
                            Toast.makeText(Login.this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "其他错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }


    }

    /**
     * 发送验证请求并判断账号是否注册
     */
    private void sendCode() {
        OkHttpUtils.post().url(AllDatas.SEND_CODE_URL).addParams("singUpMail", upMail).addParams("nikeName", upNikeName).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(Login.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                UserBean userBean = JSON.parseObject(response, UserBean.class);
                switch (userBean.resCode) {
                    case "10001":
                        ((TextView) findViewById(R.id.code_mail_tv)).setText(upMail);
                        codeLayout.setVisibility(View.VISIBLE);
                        singUpLayout.setVisibility(View.GONE);
                        break;
                    case "10002":
                        Toast.makeText(Login.this, "该邮箱已被注册", Toast.LENGTH_SHORT).show();
                        break;
                    case "10004":
                        Toast.makeText(Login.this, "昵称已被使用", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Login.this, "系统异常！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 验证码发送成功进行注册信息
     */

    private void singUpMsg() {

        code = ((EditText) findViewById(R.id.code_et)).getText().toString();
        if (code.isEmpty()) {
            Toast.makeText(Login.this, "请填写验证码", Toast.LENGTH_SHORT).show();

        } else {
            if (code.length() < 6) {
                Toast.makeText(Login.this, "请填6位数验证码", Toast.LENGTH_SHORT).show();
            } else {
                OkHttpUtils.post().url(AllDatas.SING_UP_URL).addParams("mail", upMail).addParams("password", upPassword).addParams("nikeName", upNikeName).addParams("yCode", code).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(Login.this, "网络异常", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        UserBean userBean = JSON.parseObject(response, UserBean.class);
                        switch (userBean.resCode) {
                            case "30001":
                                successLayout.setVisibility(View.VISIBLE);
                                codeLayout.setVisibility(View.GONE);
                                break;
                            case "30002":
                                Toast.makeText(Login.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                break;
                            case "30003":
                                Toast.makeText(Login.this, "验证码过期", Toast.LENGTH_SHORT).show();
                                break;
                            case "30004":
                                Toast.makeText(Login.this, "账号已经注册成功", Toast.LENGTH_SHORT).show();
                                break;
                            case "30005":
                                Toast.makeText(Login.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Login.this, "系统异常", Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "resCode: " + userBean.resCode);
                        Log.i(TAG, "resMsg: " + userBean.resMsg);

                    }
                });
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sing_up_next_btn:
                singUp();
                break;
            case R.id.sing_up_backLogin_btn:
                singUpLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.code_next_btn:
                singUpMsg();
                break;
            case R.id.success_back_login_btn:
                successLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.login_sing_up_btn:
                loginLayout.setVisibility(View.GONE);
                singUpLayout.setVisibility(View.VISIBLE);
            case R.id.login_btn:
                singIn();
                break;

        }
    }

    /**
     * 登录逻辑
     */
    private void singIn() {
        username = ((EditText) findViewById(R.id.login_username_et)).getText().toString();
        password = ((EditText) findViewById(R.id.login_password_et)).getText().toString();
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
        OkHttpUtils.post().url(AllDatas.LOGIN_URL).addParams("password",password).addParams("username",username).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                UserBean userBean = JSON.parseObject(response, UserBean.class);
                //TODO  写登录逻辑
                Log.i(TAG, "userBean: resCode"+userBean.resCode);
                Log.i(TAG, "userBean: resHeadUrl"+userBean.resHeadUrl);
                Log.i(TAG, "userBean: resMsg"+userBean.resMsg);
                Log.i(TAG, "userBean: resNikeName"+userBean.resNikeName);
            }
        });
        }

    }
}
