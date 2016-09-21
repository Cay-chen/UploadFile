package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import uploadfile.cay.com.uploadfile.AllDatas;
import uploadfile.cay.com.uploadfile.Bean.MainBean;
import uploadfile.cay.com.uploadfile.MainActivity;
import uploadfile.cay.com.uploadfile.R;

/**
 * Created by Cay on 2016/9/21.
 */
public class MainAdapter extends BaseQuickAdapter<MainBean> {
    private Context context;
    public MainAdapter(int layoutResId, List<MainBean> data,Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    public MainAdapter(List<MainBean> data) {
        super(data);
    }

    public MainAdapter(View contentView, List<MainBean> data) {
        super(contentView, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MainBean mainBean) {
        baseViewHolder.setText(R.id.folder_name, mainBean.getImageName())
                .setText(R.id.folder_time, mainBean.getImageTime());
        if (mainBean.getNum() < MainActivity.folderNum) {
            baseViewHolder.setImageResource(R.id.folder_image, R.mipmap.icon_list_folder);
        } else {
            Glide.with(context).load(AllDatas.DOWNLOAD_FILES_URL + MainActivity.name + "&imagename=" + mainBean.getImageName() + "&check=2").into((ImageView) baseViewHolder.getView(R.id.folder_image));
        }

       /* baseViewHolder.setOnClickListener(R.id.folder_image, new OnItemChildClickListener())
                .setOnClickListener(R.id.folder_name, new OnItemChildClickListener());*/
    }
}
