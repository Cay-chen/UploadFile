package uploadfile.cay.com.uploadfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import uploadfile.cay.com.uploadfile.Bean.ShowFileBean;
import uploadfile.cay.com.uploadfile.adapter.FolderRcAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "AAA";
    private RecyclerView mRecyclerView;
    private int[] folders = {R.mipmap.icon_list_folder, R.mipmap.icon_list_folder, R.mipmap.icon_list_folder, R.mipmap.icon_list_folder,R.mipmap.ic_launcher};
    private String[] names = {"先建文件夹asdasd爱如风的放大阿", "先建文件夹", "先建文件夹", "先建文件夹", "先建文件夹"};
    private String[] times = {"2016-6-15 20:36", "2016-6-15 20:36", "2016-6-15 20:36", "2016-6-15 20:36", "2016-6-15 20:36"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        init();
    }

    private void init() {
        String url = AllDatas.SHOW_FILES_URL + "?filename=chenwei";
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                ShowFileBean showFileBean = JSON.parseObject(response, ShowFileBean.class);
                Log.i(TAG, "onResponAAse: "+showFileBean.folders.toString());
                Log.i(TAG, "onResponAAse: " + showFileBean.images[0]);
                List<String> text= new ArrayList<String>();
                List<String> time = new ArrayList<String>();
                for (int i=0;i<showFileBean.folders.length;i++) {
                    text.add(showFileBean.folders[i]);
                    time.add(showFileBean.foldersTime[i]);
                }
                for (int i=0;i<showFileBean.images.length;i++) {
                    text.add(showFileBean.images[i]);
                    time.add(showFileBean.imagesTime[i]);
                }

                FolderRcAdapter folderRcAdapter = new FolderRcAdapter(folders, time, MainActivity.this, text,showFileBean.folders.length);
                mRecyclerView.setAdapter(folderRcAdapter);


            }
        });

    }


}
