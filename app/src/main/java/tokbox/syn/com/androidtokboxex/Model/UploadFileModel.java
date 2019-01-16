package tokbox.syn.com.androidtokboxex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadFileModel {

    @SerializedName("Data")
    @Expose
    private Object data;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("ErrorInfo")
    @Expose
    private List<Object> errorInfo = null;
    @SerializedName("Message")
    @Expose
    private Object message;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Object> getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(List<Object> errorInfo) {
        this.errorInfo = errorInfo;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
