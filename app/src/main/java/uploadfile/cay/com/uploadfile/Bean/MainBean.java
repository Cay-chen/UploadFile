package uploadfile.cay.com.uploadfile.Bean;

/**
 *
 * Created by Cay on 2016/9/21.
 */
public class MainBean {
    private String imageName;
    private String imageTime;
    private int num;
    private boolean isCheckBox;

    public MainBean(String imageName, String imageTime, int num, boolean isCheckBox) {
        this.imageName = imageName;
        this.imageTime = imageTime;
        this.num = num;
        this.isCheckBox = isCheckBox;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageTime() {
        return imageTime;
    }

    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isCheckBox() {
        return isCheckBox;
    }

    public void setCheckBox(boolean checkBox) {
        isCheckBox = checkBox;
    }
}
