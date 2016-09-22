package uploadfile.cay.com.uploadfile;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by C on 2016/9/22.
 */
public class MyApplication extends Application {
    public static  String name =null; //登录用户名
    public static String url=null;
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext());
        Log.i("TAG", "onCreate:QQQQ ");
    }
}
