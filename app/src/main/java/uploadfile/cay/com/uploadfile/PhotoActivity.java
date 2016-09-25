package uploadfile.cay.com.uploadfile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uploadfile.cay.com.uploadfile.Bean.UserBean;

/**
 *
 * Created by C on 2016/9/20.
 */
public class PhotoActivity extends AppCompatActivity {
    private PhotoView mPhotoView;
    private RelativeLayout deleteRl;
    private boolean isShow = false;
    private Button delButton;
    private Button downloadFileButton;
    private String[] xinxi;
    private ProgressBar progressBar;
    private String downPath = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo);
        initViews();
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        xinxi = bundle.getStringArray(AllDatas.INTENT_CODE);
        Glide.with(this).load(AllDatas.DOWNLOAD_FILES_URL + xinxi[0] + "&imagename=" + xinxi[1] + "&check=" + xinxi[2]).into(mPhotoView);
        PhotoViewAttacher   viewAttacher = new PhotoViewAttacher(mPhotoView);
        viewAttacher.update();
        viewAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteRl.setVisibility(View.VISIBLE);
                isShow = true;
                return false;
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpUtils.get().url(AllDatas.DELETE_FILE_URL).addParams("deletePath", xinxi[0]).addParams("imagename", xinxi[1]).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        UserBean userBean = JSON.parseObject(response, UserBean.class);
                        if (userBean.resCode.equals("40001")) {
                            PhotoActivity.this.setResult(5, getIntent()); //让MainActivity 刷新Rec
                            PhotoActivity.this.finish();
                        } else {
                            Toast.makeText(PhotoActivity.this, "删除失败", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
        downloadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);//显示下载进度条
                /**************创建下载的文件夹***************************/
                String seletePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
                if ((new File(seletePath)).exists()) {
                    String pathDMIC = seletePath + "/小微云相册";
                    if (!(new File(pathDMIC)).exists()) {
                        (new File(pathDMIC)).mkdirs();
                        //广播通知创建的相册
                        (PhotoActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(pathDMIC))));
                    }
                    downPath = pathDMIC;
                } else {
                    downPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/小微云相册";
                    File file = new File(downPath);
                    if (!file.exists()) {
                        file.mkdirs();
                        //广播通知创建的相册
                        (PhotoActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(downPath))));
                    }
                }
                    /*********************** 开始下载*******************************/
                OkHttpUtils.get().url(AllDatas.DOWNLOAD_FILES_URLAA).addParams("username", xinxi[0]).addParams("imagename", xinxi[1]).addParams("check", xinxi[2]).build().execute(new FileCallBack(downPath, xinxi[1]) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(PhotoActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        //通知相册更新图片
                        (PhotoActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                Uri.fromFile(new File(downPath + "/" + xinxi[1]))));

                        progressBar.setVisibility(View.GONE);
                        deleteRl.setVisibility(View.GONE);
                        Toast.makeText(PhotoActivity.this, "成功下载到:"+downPath, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        //进度条显示
                        progressBar.setMax(100);
                        progressBar.setProgress((int) (100 * progress));
                    }
                });
            }
        });
    }

    /**
     * 初始化所有控件
     */
    private void initViews() {
        deleteRl = (RelativeLayout) findViewById(R.id.photo_del_down_ll);
        mPhotoView = (PhotoView) findViewById(R.id.phone);
        delButton = (Button) findViewById(R.id.photo_del_btn);
        downloadFileButton = (Button) findViewById(R.id.photo_download);
        progressBar = (ProgressBar) findViewById(R.id.progress11);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShow) {
                isShow = false;
                deleteRl.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
        return false;
    }

}
