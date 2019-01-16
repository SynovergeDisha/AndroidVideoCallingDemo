package tokbox.syn.com.androidtokboxex.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import tokbox.syn.com.androidtokboxex.Model.ConnectionModel;
import tokbox.syn.com.androidtokboxex.Model.EndConnectionModel;
import tokbox.syn.com.androidtokboxex.Model.GetTokenResponse;
import tokbox.syn.com.androidtokboxex.Model.UploadFileModel;

public interface APIInterface {
    @GET("GetTokBoxSettings")
    Call<GetTokenResponse> getTokenResponseCall();

    @FormUrlEncoded
    @POST("StartMeeting")
    Call<ConnectionModel> getConnection(@Field("Initiater") String initiater);

    @FormUrlEncoded
    @POST("StopMeeting")
    Call<EndConnectionModel> stopMeeting(@Field("MeetingId") String meetingId);

    @Multipart
    @POST("InsertImages")
    Call<UploadFileModel> uploadImage(@Part MultipartBody.Part file1, @Part("ImageName") RequestBody ImageName, @Part("meetingId") RequestBody meetingId);
}
