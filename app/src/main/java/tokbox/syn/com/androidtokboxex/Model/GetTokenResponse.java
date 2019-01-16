package tokbox.syn.com.androidtokboxex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tokbox.syn.com.androidtokboxex.Model.GetTokenResponseData;

public class GetTokenResponse {
    @SerializedName("data")
    @Expose
    private GetTokenResponseData data;

    public GetTokenResponseData getData() {
        return data;
    }

    public void setData(GetTokenResponseData data) {
        this.data = data;
    }
}
