package tokbox.syn.com.androidtokboxex.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectionData {
    @SerializedName("APIKey")
    @Expose
    private Integer aPIKey;
    @SerializedName("SessionId")
    @Expose
    private String sessionId;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("Initiater")
    @Expose
    private String initiater;
    @SerializedName("ExpirationDate")
    @Expose
    private String expirationDate;

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

    public String getInitiater() {
        return initiater;
    }

    public void setInitiater(String initiater) {
        this.initiater = initiater;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }
}
