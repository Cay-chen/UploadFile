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

}
