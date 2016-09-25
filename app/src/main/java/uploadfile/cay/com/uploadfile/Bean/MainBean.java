package uploadfile.cay.com.uploadfile.Bean;

/**
 *
 * Created by Cay on 2016/9/21.
 */
public class MainBean {
    private String imageName; //图片名称
    private String imageTime;// 图片时间
    private int num;// 选择数量
    private boolean isCheckBox; //CheckBox是否选中
    private int isShowCheckBox;//是否显示CheckBox

    public MainBean(String imageName, String imageTime, int num, boolean isCheckBox, int isShowCheckBox) {
        this.imageName = imageName;
        this.imageTime = imageTime;
        this.num = num;
        this.isCheckBox = isCheckBox;
        this.isShowCheckBox = isShowCheckBox;
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

    public int getIsShowCheckBox() {
        return isShowCheckBox;
    }

    public void setIsShowCheckBox(int isShowCheckBox) {
        this.isShowCheckBox = isShowCheckBox;
    }
}
