package uploadfile.cay.com.uploadfile.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import uploadfile.cay.com.uploadfile.AllDatas;
import uploadfile.cay.com.uploadfile.Bean.UploadBean;
import uploadfile.cay.com.uploadfile.R;

/**
 * Created by Cay on 2016/9/24.
 */
public class UploadFileAdapter extends BaseQuickAdapter<UploadBean> {
    private Context context;
    public UploadFileAdapter(int layoutResId, List<UploadBean> data,Context context) {
        super(layoutResId, data);
        this.context = context;

    }

    public UploadFileAdapter(List<UploadBean> data) {
        super(data);
    }

    public UploadFileAdapter(View contentView, List<UploadBean> data) {
        super(contentView, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, UploadBean uploadBean) {
        baseViewHolder.setText(R.id.upload_file_name_item,uploadBean.getName());
        OkHttpUtils.post(AllDatas.UPLOAD_FILES_URL).params(uploadBean.getNamePath(),new File(uploadBean.getPath())).execute(new FileCallback() {

            @Override
            public void onSuccess(File file, Call call, Response response) {

            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                baseViewHolder.setProgress(R.id.upload_file_progress, (int) (100 * progress), 100);

            }
        });
    }
}
