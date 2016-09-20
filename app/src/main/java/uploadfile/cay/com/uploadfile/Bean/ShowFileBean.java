package uploadfile.cay.com.uploadfile.Bean;

import java.util.Arrays;

/**
 * Created by Cay on 2016/9/20.
 */
public class ShowFileBean {
    public String[] folders;
    public String[] images;
    public String[] foldersTime;
    public String[] imagesTime;

    @Override
    public String toString() {
        return "ShowFileBean{" +
                "folders=" + Arrays.toString(folders) +
                ", images=" + Arrays.toString(images) +
                ", foldersTime=" + Arrays.toString(foldersTime) +
                ", imagesTime=" + Arrays.toString(imagesTime) +
                '}';
    }
}
