package uploadfile.cay.com.uploadfile.Util;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by Cay on 2016/9/29.
 */
public class MyCountTimer extends CountDownTimer {
    public static final int TIME_COUNT = 61000;//防止从119s开始显示（以倒计时120s为例子）
    private Button btn;
    int endStrRid;
    private int normalDrawable, timingDrawable;//未计时的文字颜色，计时期间的文字颜色

    /**
     * 参数 millisInFuture         倒计时总时间（如60S，120s等）
     * 参数 countDownInterval    渐变时间（每次倒计1s）
     * <p/>
     * 参数 btn               点击的按钮(因为Button是TextView子类，为了通用我的参数设置为TextView）
     * <p/>
     * 参数 endStrRid   倒计时结束后，按钮对应显示的文字
     */
    public MyCountTimer(long millisInFuture, long countDownInterval, Button btn, int endStrRid) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.endStrRid = endStrRid;
    }

    public MyCountTimer(long millisInFuture, long countDownInterval, Button btn, int normalDrawable, int timingDrawabl) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.normalDrawable = normalDrawable;
        this.timingDrawable = timingDrawabl;

    }


    // 计时完毕时触发
    @Override
    public void onFinish() {
        if (normalDrawable > 0) {
            // btn.setTextColor(normalColor);
            // Drawable drawable = new
            btn.setBackgroundResource(normalDrawable);
        }
        // btn.setText(endStrRid);
        btn.setText("发送验证码");
        btn.setEnabled(true);
    }

    // 计时过程显示
    @Override
    public void onTick(long millisUntilFinished) {
        if (timingDrawable > 0) {
            //btn.setTextColor(timingColor);
            btn.setBackgroundResource(timingDrawable);
        }
        btn.setEnabled(false);
        btn.setText(millisUntilFinished / 1000 + "s后再发送");
    }

}
