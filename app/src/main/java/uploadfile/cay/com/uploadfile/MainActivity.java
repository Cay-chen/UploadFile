package uploadfile.cay.com.uploadfile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okhttpserver.download.DownloadInfo;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.download.DownloadService;
import com.lzy.okhttpserver.download.db.DownloadRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.Call;
import okhttp3.Request;
import uploadfile.cay.com.uploadfile.Bean.MainBean;
import uploadfile.cay.com.uploadfile.Bean.ShowFileBean;
import uploadfile.cay.com.uploadfile.Bean.UploadBean;
import uploadfile.cay.com.uploadfile.adapter.MainAdapter;
import uploadfile.cay.com.uploadfile.adapter.UploadFileAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AAA";
    private List<String> selectImagesPath;  //上传所选图片的路径集合
    private List<MainBean> datas = new ArrayList<>();//Adapter 数据集合
    private RecyclerView mRecyclerView;     //显示的RecyclerView
    private MainAdapter mainAdapter; //显示列表的Adapter
    private String createFolderName;//获取创建新文件夹的名称
    private int uploadFileProgress = 0;//统计上传完成的文件个数
    private LinearLayout createFolderButton;//创建文件夹按钮
    private LinearLayout uploadFileButton;//上传文件按钮
    public static List<String> pathList = new ArrayList<>();//re显示的路径集合（显示那个文件夹内容的集合）
    private static Boolean isExit = false;//判断双击退出系统记录相册
    private Boolean isDuoxuan = false;
    private RelativeLayout topLayout;
    private Button topCancelButton;
    private Button topAllCheckButton;
    private TextView topText;
    private TextView folderNameTextView;

    private String imageName; //根据路径截取出来的图片名称
    private ProgressDialog mProgress;//Dialog 进度条
    private List<UploadBean> upList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        pathList.add(MyApplication.name);
        initViews();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        showRecyclerView(false);
        createFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFolder();
            }
        });
        uploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFileProgress = 0;
                MultiImageSelector.create().showCamera(true).count(AllDatas.UPLOAD_FILE_MAXNUM).multi().start(MainActivity.this, AllDatas.SELECT_IMAGE_ACTIVITY_CODE);
            }
        });
    }

    private void initViews() {
        uploadFileButton = (LinearLayout) findViewById(R.id.upload_file_ll);
        createFolderButton = (LinearLayout) findViewById(R.id.create_folder_ll);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        topLayout = (RelativeLayout) findViewById(R.id.folder_top_ll);
        topLayout.getBackground().setAlpha(160);
        topCancelButton = (Button) findViewById(R.id.folder_top_cancel_btn);
        topText = (TextView) findViewById(R.id.folder_top_text);
        topAllCheckButton = (Button) findViewById(R.id.folder_top_all_btn);
        folderNameTextView = (TextView) findViewById(R.id.folder_name);
        folderNameTextView.setText(MyApplication.name);

    }

    /**
     * item的点击事件
     */
    private void onCk(boolean isShowCheckBox) {
        mainAdapter = new MainAdapter(R.layout.folder_item, datas, MainActivity.this, isShowCheckBox);
        mRecyclerView.setAdapter(mainAdapter);
        mainAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (i < MyApplication.folderNum) {
                    String nextPath = pathList.get(pathList.size() - 1) + "\\" + datas.get(i).getImageName();
                    pathList.add(nextPath);
                    isDuoxuan = false;
                    topLayout.setVisibility(View.GONE);
                    showRecyclerView(false);
                    Log.i(TAG, "isDuoxuan: " + isDuoxuan);
                } else {
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    String[] xinx = {MainActivity.pathList.get(MainActivity.pathList.size() - 1), datas.get(i).getImageName(), "1"};
                    intent.putExtra(AllDatas.INTENT_CODE, xinx);
                    startActivityForResult(intent, 5);
                }
            }
        });
        mainAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                mainAdapter.mPos.add(datas.get(i).getImageName());
                isDuoxuan = true;
                folderNameTextView.setVisibility(View.GONE);
                topLayout.setVisibility(View.VISIBLE);
                showRecyclerView(true);
                mainAdapter.mPos.clear();
                return true;
            }
        });
        mainAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MainBean status = (MainBean) baseQuickAdapter.getItem(i);
                switch (view.getId()) {
                    case R.id.folder_check_box:
                        CheckBox cb = (CheckBox) view;
                        if (cb.isChecked())
                            mainAdapter.mPos.add(status.getImageName());
                        else
                            mainAdapter.mPos.remove(status.getImageName());
                        topText.setText(R.string.select + mainAdapter.mPos.size() + R.string.ge);
                        break;
                    case R.id.folder_name:
                        // content = "name:" + status.getImageTime();
                        break;
                }

            }
        });
    }

    /**
     * 创建文件夹
     */
    private void createFolder() {

        final EditText editText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("新建文件夹").setIcon(R.mipmap.icon_list_folder).setView(editText).setNegativeButton("取消", null).setPositiveButton("创建", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createFolderName = editText.getText().toString();
                String path = AllDatas.CREATE_FOLDER_URL + pathList.get((pathList.size()) - 1) + "\\" + createFolderName;
                OkHttpUtils.get().url(path).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                        showRecyclerView(false);
                    }
                });
            }
        });
        builder.show();

    }

    /**
     * RecyclerView 显示的内容和数据
     */
    private void showRecyclerView(final boolean isShowBockBox) {
        Log.i(TAG, "最后路径: " + pathList.get(pathList.size() - 1));
        datas.clear();
        OkHttpUtils.get().url(AllDatas.SHOW_FILES_URL + pathList.get(pathList.size() - 1)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ShowFileBean showFileBean = JSON.parseObject(response, ShowFileBean.class);
                MyApplication.folderNum = showFileBean.folders.length;
                for (int i = 0; i < showFileBean.folders.length; i++) {
                    datas.add(new MainBean(showFileBean.folders[i], showFileBean.foldersTime[i], datas.size()));
                }
                for (int i = 0; i < showFileBean.images.length; i++) {
                    datas.add(new MainBean(showFileBean.images[i], showFileBean.imagesTime[i], datas.size()));
                }

                onCk(isShowBockBox);


            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (isDuoxuan) {
                isDuoxuan = false;
                topLayout.setVisibility(View.GONE);
                showRecyclerView(false);
            } else {
                if (pathList.size() > 1) {
                    pathList.remove(pathList.size() - 1);
                    showRecyclerView(false);
                } else {
                    exitBy2Click();
                }
            }
        }
        return false;
    }

    /**
     * 双击退出函数
     */

    private void exitBy2Click() {
        Timer tExit;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }

    /**
     * 上传文件
     */
    private void uploadFile() {
        progressDialog(selectImagesPath.size());
        for (int i = 0; i < selectImagesPath.size(); i++) {
            String[] names = selectImagesPath.get(i).split("\\/"); //按照/ 截取数组
            imageName = names[(names.length) - 1];//取出文件名
            String uploadFilePath = pathList.get(pathList.size() - 1);
            final File file = new File(selectImagesPath.get(i));
            OkHttpUtils.post().addFile(uploadFilePath, imageName, file).url(AllDatas.UPLOAD_FILES_URL).build().execute(new StringCallback() {
                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    uploadFileProgress++;
                    mProgress.setProgress(uploadFileProgress);
                }

                @Override
                public void onResponse(String response, int id) {
                    uploadFileProgress++;
                    mProgress.setProgress(uploadFileProgress);
                    if (uploadFileProgress == selectImagesPath.size()) {
                        mProgress.dismiss();
                        showRecyclerView(false);
                    }
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AllDatas.SELECT_IMAGE_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                upList.clear();
                selectImagesPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
               /* for (int i = 0; i < selectImagesPath.size(); i++) {
                    String[] names = selectImagesPath.get(i).split("\\/"); //按照/ 截取数组
                    String upImageName = names[(names.length) - 1];//取出文件名
                    String uploadFilePath = pathList.get(pathList.size() - 1);
                    upList.add(new UploadBean(selectImagesPath.get(i), upImageName, uploadFilePath));
                }*/
                AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
                builder.setTitle("是否上传"); //设置标题
                builder.setMessage("你选择了" + selectImagesPath.size() + "张照片"); //设置内容
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        uploadFile();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }

        }
        if (resultCode == 5) {
            //删除图片后刷新recycle
            showRecyclerView(false);

        }
    }

    /**
     * 进度条Dialog
     */
    private void progressDialog(int maxNum) {
        mProgress = new ProgressDialog(this);
        mProgress.setMax(maxNum);
        mProgress.setIcon(R.mipmap.ic_launcher);
        mProgress.setTitle("正在上传中");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setButton("后台上传", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgress.dismiss();
                // TODO Auto-generated method stub
                //   Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        });
       /* mProgress.setButton2("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });*/
        mProgress.setCancelable(false);
        mProgress.show();

    }


}
