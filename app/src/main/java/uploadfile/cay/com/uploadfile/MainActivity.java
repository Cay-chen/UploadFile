package uploadfile.cay.com.uploadfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import uploadfile.cay.com.uploadfile.adapter.FolderRcAdapter;

public class MainActivity extends AppCompatActivity {
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
        FolderRcAdapter folderRcAdapter = new FolderRcAdapter(folders, times, this, names);
        mRecyclerView.setAdapter(folderRcAdapter);
    }
}
