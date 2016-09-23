package uploadfile.cay.com.uploadfile;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by C on 2016/9/22.
 */
public class MyApplication extends Application {
    public static  String name ="测试账号"; //登录用户名
    public static String url=null;
    public static int folderNum = 0; //目录线面的文件个数
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext());
        Log.i("TAG", "onCreate:QQQQ ");
    }
}
