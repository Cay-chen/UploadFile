package uploadfile.cay.com.uploadfile.Bean;

/**
 * Created by C on 2016/9/22.
 */
public class UserBean {
    public String resCode;
    public String resMsg;
    public String resNikeName;
    public String resHeadUrl;

    @Override
    public String toString() {
        return "UserBean{" +
                "resCode='" + resCode + '\'' +
                ", resMsg='" + resMsg + '\'' +
                ", resNikeName='" + resNikeName + '\'' +
                ", resHeadUrl='" + resHeadUrl + '\'' +
                '}';
    }
}
