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
public class Login extends AppCompatActivity {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sign);
        initViews();
        //发送验证码
        singUpNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUp();

            }
        });
      //  singUpBackLogin.setOnClickListener();
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singUpMsg();
            }
        });
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
                Log.i(TAG, "发送验证码逻辑: ");
                sendCode();
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
        OkHttpUtils.post().url(AllDatas.SEND_CODE_URL).addParams("singUpMail",upMail).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(Login.this, "网络异常", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                UserBean userBean = JSON.parseObject(response, UserBean.class);
                switch (userBean.resCode) {
                    case "10001":
                        codeLayout.setVisibility(View.VISIBLE);
                        singUpLayout.setVisibility(View.GONE);
                        break;
                    case "10002":
                        Toast.makeText(Login.this, "该账号已被注册", Toast.LENGTH_SHORT).show();
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

        ((TextView)findViewById(R.id.code_mail_tv)).setText(upMail);
        code = ((EditText) findViewById(R.id.code_et)).getText().toString();
        OkHttpUtils.post().url(AllDatas.SING_UP_URL).addParams("mail",upMail).addParams("password",upPassword).addParams("nikeName",upNikeName).addParams("yCode",code).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(Login.this, "网络异常", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onResponse(String response, int id) {
                UserBean userBean = JSON.parseObject(response, UserBean.class);
                Log.i(TAG, "resCode: "+userBean.resCode);
                Log.i(TAG, "resMsg: "+userBean.resMsg);

            }
        });

    }
}
