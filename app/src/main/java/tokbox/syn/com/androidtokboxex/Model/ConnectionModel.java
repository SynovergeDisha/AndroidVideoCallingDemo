package tokbox.syn.com.androidtokboxex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionModel {
    @SerializedName("Data")
    @Expose
    private ConnectionData data;

    public ConnectionData getData() {
        return data;
    }

    public void setData(ConnectionData data) {
        this.data = data;
    }
}
