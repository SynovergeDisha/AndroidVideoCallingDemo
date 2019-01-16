package tokbox.syn.com.androidtokboxex.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tokbox.syn.com.androidtokboxex.Model.UploadFileModel;
import tokbox.syn.com.androidtokboxex.R;
import tokbox.syn.com.androidtokboxex.Retrofit.APIClient;
import tokbox.syn.com.androidtokboxex.Retrofit.APIInterface;

public class SuccessActivity extends AppCompatActivity {

    private ArrayList<String> capturedImageList;
    private AppCompatButton btnUploadImage;
    private int posIncr;
    private String meetingId;
    private LinearLayout llProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_success);
        Bundle in = getIntent().getExtras();
        if (in != null) {
            capturedImageList = in.getStringArrayList("capturedImageList");
            meetingId = in.getString("meetingId");
        }
        init();
        listener();
    }

    private void init() {
        btnUploadImage = findViewById(R.id.btn_upload_image);
        llProgress = findViewById(R.id.ll_progress);
        if (capturedImageList != null && capturedImageList.size() > 0) {
            btnUploadImage.setVisibility(View.VISIBLE);
        } else {
            btnUploadImage.setVisibility(View.GONE);
        }
    }

    private void listener() {
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capturedImageList != null && capturedImageList.size() > 0) {
                    posIncr = 0;
                    llProgress.setVisibility(View.VISIBLE);
                    uploadFile(posIncr);
                    btnUploadImage.setVisibility(View.GONE);
                }
            }
        });
    }

    // Uploading Image
    private void uploadFile(int pos) {
        String mediaPath = Environment.getExternalStorageDirectory().toString() + "/" + capturedImageList.get(pos) + ".jpg";
        File file = new File(mediaPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("File", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody meetingID = RequestBody.create(MediaType.parse("text/plain"), meetingId);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<UploadFileModel> call = apiInterface.uploadImage(fileToUpload, filename, meetingID);

        call.enqueue(new Callback<UploadFileModel>() {
            @Override
            public void onResponse(Call<UploadFileModel> call, Response<UploadFileModel> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getStatus().equalsIgnoreCase("ok")) {
                        if (capturedImageList.size() > 1 && (posIncr + 1) < capturedImageList.size()) {
                            posIncr = posIncr + 1;
                            uploadFile(posIncr);
                        } else {
                            llProgress.setVisibility(View.GONE);
                        }
                    } else {
                        llProgress.setVisibility(View.GONE);
                        btnUploadImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    llProgress.setVisibility(View.GONE);
                    btnUploadImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UploadFileModel> call, Throwable t) {
                llProgress.setVisibility(View.GONE);
                btnUploadImage.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

}
