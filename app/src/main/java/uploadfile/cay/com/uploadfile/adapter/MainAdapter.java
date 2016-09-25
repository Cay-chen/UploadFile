package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
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
public class MainAdapter extends BaseQuickAdapter<MainBean> {
    public static List<String> mPos = new ArrayList<>();
    private Boolean isShowCheckBox; //是否显示CheckBox
    private Context context;
    public MainAdapter(int layoutResId, List<MainBean> data, Context context, Boolean isShowCheckBox) {
        super(layoutResId, data);
        this.context = context;
        this.isShowCheckBox = isShowCheckBox;
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
        final CheckBox cb = baseViewHolder.getView(R.id.folder_check_box);
        if (isShowCheckBox) {
            cb.setVisibility(View.VISIBLE);
        } else {
            cb.setVisibility(View.GONE);
        }
        cb.setChecked(false);
        if (mPos.contains(mainBean.getImageName())) {
            cb.setChecked(true);
        }
        baseViewHolder.setOnClickListener(R.id.folder_check_box, new OnItemChildClickListener()); //设置checkbook的监听

    }
}
