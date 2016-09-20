package uploadfile.cay.com.uploadfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
      //  PhotoView mPhotoView = new PhotoView(this);
        Glide.with(this).load("http://hiphotos.baidu.com/%B3%F5%BC%B6%BE%D1%BB%F7%CA%D6/pic/item/929b56443840bfc6b3b7dc64.jpg").into(viewPager);
        viewAttacher= new PhotoViewAttacher(viewPager);
       // viewAttacher.update();
    }
}
