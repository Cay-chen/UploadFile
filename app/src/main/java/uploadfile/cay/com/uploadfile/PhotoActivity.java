package uploadfile.cay.com.uploadfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.bumptech.glide.Glide;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by C on 2016/9/20.
 */
public class PhotoActivity extends AppCompatActivity {
    private PhotoViewAttacher viewAttacher;
    private PhotoView viewPager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.photo);
       viewPager = (PhotoView) findViewById(R.id.phone);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        String url = bundle.getString(AllDatas.INTENT_CODE);
    //   PhotoView mPhotoView = new PhotoView(this);
       Glide.with(this).load(url).into(viewPager);
     viewAttacher= new PhotoViewAttacher(viewPager);
      viewAttacher.update();
    }
}
