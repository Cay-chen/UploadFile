package uploadfile.cay.com.uploadfile.Bean;

/**
 *
 * Created by Cay on 2016/9/24.
 */
public class UploadBean {
   private String path;
    private String name;
    private String namePath;

    public UploadBean(String path, String name, String namePath) {
        this.path = path;
        this.name = name;
        this.namePath = namePath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePath() {
        return namePath;
    }

    public void setNamePath(String namePath) {
        this.namePath = namePath;
    }
}
