package uploadfile.cay.com.uploadfile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.security.KeyStore;
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
import uploadfile.cay.com.uploadfile.Bean.UserBean;
import uploadfile.cay.com.uploadfile.adapter.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AAA";
    private List<String> selectImagesPath;  //上传所选图片的路径集合
    private List<MainBean> datas = new ArrayList<>();//Adapter 数据集合
    private RecyclerView mRecyclerView;     //显示的RecyclerView
    private RecyclerViewAdapter recyclerViewAdapter; //显示列表的Adapter
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
    private int num = 0;
    private String imageName; //根据路径截取出来的图片名称
    private ProgressDialog mProgress;//Dialog 进度条
    private List<UploadBean> upList = new ArrayList<>();
    private boolean isAllCheck = true;
    private RelativeLayout tabLayout;
    private Button deleteBtn;
    private Button downLoadBtn;
    private List<MainBean> isSeceltList = new ArrayList<>();
    private int selectFolderNum;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        pathList.add(MyApplication.name);
        initViews();//初始化所有控件
        initOnCK();//初始化监听事件
        updataDatas(true);//初始化数
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 初始化所有监听
     */
    private void initOnCK() {
        createFolderButton.setOnClickListener(this);
        uploadFileButton.setOnClickListener(this);
        topCancelButton.setOnClickListener(this);
        topAllCheckButton.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        downLoadBtn.setOnClickListener(this);
    }

    /**
     * 初始化所有控件
     */
    private void initViews() {
        uploadFileButton = (LinearLayout) findViewById(R.id.upload_file_ll);
        createFolderButton = (LinearLayout) findViewById(R.id.create_folder_ll);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        topLayout = (RelativeLayout) findViewById(R.id.folder_top_ll);
         topLayout.getBackground().setAlpha(160);
        topCancelButton = (Button) findViewById(R.id.folder_top_cancel_btn);
        topText = (TextView) findViewById(R.id.folder_top_text);
        folderNameTextView = (TextView) findViewById(R.id.folder_name);
        folderNameTextView.setText(MyApplication.name);
        topCancelButton = (Button) findViewById(R.id.folder_top_cancel_btn);
        topAllCheckButton = (Button) findViewById(R.id.folder_top_all_btn);
        tabLayout = (RelativeLayout) findViewById(R.id.folder_tab_ll);
        deleteBtn = (Button) findViewById(R.id.folder_delete_btn);
        downLoadBtn = (Button) findViewById(R.id.folder_dowanload_btn);

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
                        updataDatas(false);
                    }
                });
            }
        });
        builder.show();

    }

    /**
     * RecyclerView 显示的内容和数据
     */
    private void updataDatas(final boolean init) {
        Log.i(TAG, "最后路径: " + pathList.get(pathList.size() - 1));
        datas.clear();//清楚之前下载的内容
        OkHttpUtils.get().url(AllDatas.SHOW_FILES_URL + pathList.get(pathList.size() - 1)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                ShowFileBean showFileBean = JSON.parseObject(response, ShowFileBean.class);
                MyApplication.folderNum = showFileBean.folders.length;
                for (int i = 0; i < showFileBean.folders.length; i++) {
                    datas.add(new MainBean(showFileBean.folders[i], showFileBean.foldersTime[i], datas.size(), false, View.GONE));
                }
                for (int i = 0; i < showFileBean.images.length; i++) {
                    datas.add(new MainBean(showFileBean.images[i], showFileBean.imagesTime[i], datas.size(), false, View.GONE));
                }
                if (init) {
                    initRecyclerView();
                } else {
                    recyclerViewAdapter.notifyDataSetChanged();
                }

            }

        });


    }

    /**
     * 重写返回键功能
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (isDuoxuan) {
                cancledMultiselect();
            } else {
                if (pathList.size() > 1) {
                    pathList.remove(pathList.size() - 1);
                    updataDatas(false);
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
        progressDialog(selectImagesPath.size(), "正在上传中");
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
                        Toast.makeText(MainActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                        updataDatas(false);
                        isAllCheck = true;
                    }
                }
            });

        }
    }

    /**
     * 接收Activity返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AllDatas.SELECT_IMAGE_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                upList.clear();
                selectImagesPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
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
                        selectImagesPath.clear();//清楚数据
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }

        }
        if (resultCode == 5) {
            updataDatas(false);

        }
    }

    /**
     * 上传进度条Dialog
     */
    private void progressDialog(int maxNum, String tital) {
        mProgress = new ProgressDialog(this);
        mProgress.setMax(maxNum);
        mProgress.setIcon(R.mipmap.ic_launcher);
        mProgress.setTitle(tital);
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setButton("后台运行", new DialogInterface.OnClickListener() {

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

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        recyclerViewAdapter = new RecyclerViewAdapter(R.layout.folder_item, datas, MainActivity.this);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (i < MyApplication.folderNum) {
                    String nextPath = pathList.get(pathList.size() - 1) + "\\" + datas.get(i).getImageName();
                    pathList.add(nextPath);
                    isDuoxuan = false;
                    topLayout.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    topText.setText("");
                    updataDatas(false);
                } else {
                    Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
                    String[] xinx = {MainActivity.pathList.get(MainActivity.pathList.size() - 1), datas.get(i).getImageName(), "1"};
                    intent.putExtra(AllDatas.INTENT_CODE, xinx);
                    startActivityForResult(intent, 5);
                }
            }
        });
        recyclerViewAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                selectFolderNum = 0;
                num = 0;
                isDuoxuan = true;
                folderNameTextView.setVisibility(View.GONE);
                topLayout.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                //  datas.get(i).setCheckBox(true);
                for (MainBean showCB : datas) {
                    showCB.setIsShowCheckBox(View.VISIBLE);
                }
                recyclerViewAdapter.notifyDataSetChanged();
                isSeceltList.clear();//清楚之前选中的数据
                return true;
            }
        });
        recyclerViewAdapter.setOnRecyclerViewItemChildClickListener(new BaseQuickAdapter.OnRecyclerViewItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MainBean mainBean = (MainBean) baseQuickAdapter.getItem(i);
                switch (view.getId()) {
                    case R.id.folder_check_box:
                        CheckBox cb = (CheckBox) view;
                        if (cb.isChecked()) {
                            if (i < MyApplication.folderNum) {
                                selectFolderNum++;
                            }
                            mainBean.setCheckBox(true);
                            isSeceltList.add(mainBean);
                            num++;
                            if (num > 0) {
                                topText.setText("已选定" + num + "个");
                                deleteBtn.setEnabled(true);
                                downLoadBtn.setEnabled(true);
                            } else {
                                downLoadBtn.setEnabled(false);
                                deleteBtn.setEnabled(false);
                                topText.setText("");
                            }
                        } else {
                            if (i < MyApplication.folderNum) {
                                selectFolderNum--;
                            }
                            mainBean.setCheckBox(false);
                            isSeceltList.remove(mainBean);
                            num--;
                            if (num > 0) {
                                topText.setText("已选定" + num + "个");
                                deleteBtn.setEnabled(true);
                                downLoadBtn.setEnabled(true);
                            } else {
                                downLoadBtn.setEnabled(false);
                                topText.setText("");
                                deleteBtn.setEnabled(false);
                            }
                        }
                        break;
                    case R.id.folder_name:
                        break;
                }

            }
        });
    }

    /**
     * 取消多选界面
     */
    public void cancledMultiselect() {
        isDuoxuan = false;
        isAllCheck = true;
        topAllCheckButton.setText("全选");
        topText.setText("");
        topLayout.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        folderNameTextView.setVisibility(View.VISIBLE);
        for (MainBean showCB : datas) {
            showCB.setIsShowCheckBox(View.GONE);
            showCB.setCheckBox(false);
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_folder_ll:
                createFolder();
                break;
            case R.id.upload_file_ll:
                uploadFileProgress = 0;
                MultiImageSelector.create().showCamera(true).count(AllDatas.UPLOAD_FILE_MAXNUM).multi().start(MainActivity.this, AllDatas.SELECT_IMAGE_ACTIVITY_CODE);
                break;
            case R.id.folder_top_cancel_btn:
                cancledMultiselect();
                break;
            case R.id.folder_top_all_btn:
                selectFolderNum = 0;
                isSeceltList.clear();
                if (isAllCheck) {
                    isAllCheck = false;
                    topAllCheckButton.setText("全不选");
                    selectFolderNum = MyApplication.folderNum;
                    topText.setText("已选定" + datas.size() + "个");
                    for (MainBean showCB : datas) {
                        showCB.setCheckBox(true);
                        isSeceltList.add(showCB);
                    }
                    recyclerViewAdapter.notifyDataSetChanged();

                } else {
                    topText.setText("");
                    isAllCheck = true;
                    topAllCheckButton.setText("全选");

                    for (MainBean showCB : datas) {
                        showCB.setCheckBox(false);
                    }
                    recyclerViewAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.folder_dowanload_btn:
                uploadFileProgress = 0;
                if (selectFolderNum > 0) {
                    Toast.makeText(MainActivity.this, "不能下载文件夹", Toast.LENGTH_SHORT).show();
                } else {
                    downloadFile();
                }
                break;
            case R.id.folder_delete_btn:
                uploadFileProgress = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
                builder.setTitle("确认删除"); //设置标题
                Log.i(TAG, "onClick: "+selectFolderNum);
                if (selectFolderNum > 0) {
                    builder.setMessage("将删除" + selectFolderNum + "个文件夹," + (isSeceltList.size() - selectFolderNum) + "张照片"); //设置内容
                } else {
                    builder.setMessage("将删除" + isSeceltList.size() + "张照片"); //设置内容
                }
                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        deleteFolder();
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
                break;

        }

    }

    /**
     * 删除文件
     */

    public void deleteFolder() {
        progressDialog(isSeceltList.size(), "正在删除");
        Log.i(TAG, "deleteFolder: "+System.currentTimeMillis());
        for (final MainBean mainBean : isSeceltList) {
            OkHttpUtils.get().url(AllDatas.DELETE_FILE_URL).addParams("deletePath", MainActivity.pathList.get(MainActivity.pathList.size() - 1)).addParams("imagename", mainBean.getImageName()).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    uploadFileProgress++;

                }

                @Override
                public void onResponse(String response, int id) {

                    UserBean userBean = JSON.parseObject(response, UserBean.class);
                    if (userBean.resCode.equals("40001")) {
                      //  MainActivity.this.setResult(5, getIntent()); //让MainActivity 刷新Rec
                       // MainActivity.this.finish();
                    } else {
                        Toast.makeText(MainActivity.this, "删除失败", Toast.LENGTH_SHORT).show();

                    }
                    uploadFileProgress++;
                    mProgress.setProgress(uploadFileProgress);
                    if (uploadFileProgress == isSeceltList.size()) {
                       mProgress.dismiss();
                        Toast.makeText(MainActivity.this, "删除完成", Toast.LENGTH_SHORT).show();
                        cancledMultiselect();
                        updataDatas(false);
                    }

                }
            });


        }

    }


    /**
     * 下载文件
     */
    private void downloadFile() {
        progressDialog(isSeceltList.size(), "正在下载");
        Log.i(TAG, "downloadFile: " + isSeceltList.size());
        for (final MainBean mainBean : isSeceltList) {
            final String downPath;
            /**************创建下载的文件夹***************************/
            String seletePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM";
            if ((new File(seletePath)).exists()) {
                String pathDMIC = seletePath + "/小微云相册";
                if (!(new File(pathDMIC)).exists()) {
                    (new File(pathDMIC)).mkdirs();
                    //广播通知创建的相册
                    (MainActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(pathDMIC))));
                }
                downPath = pathDMIC;
            } else {
                downPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/小微云相册";
                File file = new File(downPath);
                if (!file.exists()) {
                    file.mkdirs();
                    //广播通知创建的相册
                    (MainActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(downPath))));
                }
            }
            /*********************** 开始下载*******************************/
            OkHttpUtils.get().url(AllDatas.DOWNLOAD_FILES_URLAA).addParams("username", MainActivity.pathList.get(MainActivity.pathList.size() - 1)).addParams("imagename", mainBean.getImageName()).addParams("check", "1").build().execute(new FileCallBack(downPath, mainBean.getImageName()) {
                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);


                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                    uploadFileProgress++;
                }

                @Override
                public void onResponse(File response, int id) {
                    //通知相册更新图片
                    (MainActivity.this).sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(new File(downPath + "/" + mainBean.getImageName()))));
                    uploadFileProgress++;
                    mProgress.setProgress(uploadFileProgress);
                    if (uploadFileProgress == isSeceltList.size()) {
                        mProgress.dismiss();
                        Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                        cancledMultiselect();
                    }


                }

            });
        }

    }
}
