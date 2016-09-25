package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import uploadfile.cay.com.uploadfile.AllDatas;
import uploadfile.cay.com.uploadfile.Bean.MainBean;
import uploadfile.cay.com.uploadfile.MainActivity;
import uploadfile.cay.com.uploadfile.MyApplication;
import uploadfile.cay.com.uploadfile.R;

/**
 *
 * Created by Cay on 2016/9/21.
 */
public class RecyclerViewAdapter extends BaseQuickAdapter<MainBean> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context context;
    public RecyclerViewAdapter(int layoutResId, List<MainBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }
    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final MainBean mainBean) {
        baseViewHolder.setText(R.id.folder_name, mainBean.getImageName())
                .setText(R.id.folder_time, mainBean.getImageTime());

        if (mainBean.getNum() < MyApplication.folderNum) {
            baseViewHolder.setImageResource(R.id.folder_image, R.mipmap.icon_list_folder);
        } else {
            Glide.with(context).load(AllDatas.DOWNLOAD_FILES_URL + MainActivity.pathList.get(MainActivity.pathList.size() - 1) + "&imagename=" + mainBean.getImageName() + "&check=2").into((ImageView) baseViewHolder.getView(R.id.folder_image));
        }
       CheckBox cb = baseViewHolder.getView(R.id.folder_check_box);
        cb.setVisibility(mainBean.getIsShowCheckBox());
        cb.setChecked(mainBean.isCheckBox());
        baseViewHolder.setOnClickListener(R.id.folder_check_box, new OnItemChildClickListener()); //设置checkbook的监听

    }
}
