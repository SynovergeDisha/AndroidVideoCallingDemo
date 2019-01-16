package tokbox.syn.com.androidtokboxex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTokenResponseData {
    @SerializedName("APIKey")
    @Expose
    private Integer aPIKey;
    @SerializedName("SessionId")
    @Expose
    private String sessionId;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("MeetingId")
    @Expose
    private String meetingId;

    public Integer getAPIKey() {
        return aPIKey;
    }

    public void setAPIKey(Integer aPIKey) {
        this.aPIKey = aPIKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
}
