package uploadfile.cay.com.uploadfile;

/**
 * Created by Cay on 2016/9/20.
 */
public class AllDatas {
    public static final String SHOW_FILES_URL = "http://118.192.157.178:8080/XiaoWei/servlet/CheckImages?username="; //显示地址
    public static final String DOWNLOAD_FILES_URL = "http://118.192.157.178:8080/XiaoWei/servlet/DownloadFile?username=";
    public static final String CREATE_FOLDER_URL = "http://118.192.157.178:8080/XiaoWei/servlet/CreateFolder?folderpath=";
    public static final int SELECT_IMAGE_ACTIVITY_CODE=3;
    public static final int UPLOAD_FILE_MAXNUM=9;  //单次选择图片上传最大数量
    public static final String UPLOAD_FILES_URL ="http://118.192.157.178:8080/XiaoWei/servlet/UploadFile";//上传文件地址
    public static final String INTENT_CODE = "IMAGE_URL";
    public static final String SEND_CODE_URL = "http://118.192.157.178:8080/XiaoWei/servlet/YunSendCode";//发送验证码地址
    public static final String SING_UP_URL = "http://118.192.157.178:8080/XiaoWei/servlet/YunSingUp";//注册地址
    public static final String LOGIN_URL = "http://118.192.157.178:8080/XiaoWei/servlet/YunLogin";//注册地址

}
