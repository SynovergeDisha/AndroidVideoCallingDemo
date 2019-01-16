package tokbox.syn.com.androidtokboxex.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tokbox.syn.com.androidtokboxex.Model.ConnectionModel;
import tokbox.syn.com.androidtokboxex.Model.GetTokenResponse;
import tokbox.syn.com.androidtokboxex.R;
import tokbox.syn.com.androidtokboxex.Retrofit.APIClient;
import tokbox.syn.com.androidtokboxex.Retrofit.APIInterface;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

//    private AppCompatButton btnJoinSession;
    private AppCompatButton btnCreateSession;
    private static final int RC_VIDEO_APP_PERM = 124;
    private APIInterface apiInterface;
    private ProgressBar progress;
    private TextView tvWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        requestPermissions();

        btnCreateSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

//        btnJoinSession.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progress.setVisibility(View.VISIBLE);
//                tvWait.setVisibility(View.VISIBLE);
//                callApi();
////                btnJoinSession.setVisibility(View.GONE);
////                String tk = Const.SESSION_ID;
////                String sTk = Const.SUBSCRIBER_TOKEN;
////                if (true) {
////                    Intent in = new Intent(MainActivity.this, SessionActivity.class);
////                    in.putExtra("_CreateOrJoinSession", "Join");
////                    in.putExtra("_Token", tk);
////                    in.putExtra("_SessionToken", sTk);
////                    startActivity(in);
////                } else {
////                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
////                }
////                startActivity(new Intent(v.getContext(), JoinSessionAct.class));
//            }
//        });
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_name);
        dialog.setTitle(getResources().getString(R.string.enter_name_text));
        final EditText etName = dialog.findViewById(R.id.et_name);
        Button dialogButton = dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etName.getText().toString())) {
                    progress.setVisibility(View.VISIBLE);
                    tvWait.setVisibility(View.VISIBLE);
                    getConnection(etName.getText().toString());

//                    Intent in = new Intent(MainActivity.this, SessionActivity.class);
//                    in.putExtra("_CreateOrJoinSession", "Create");
//                    in.putExtra("_Token", Const.SESSION_ID);
//                    in.putExtra("_SessionToken", Const.SUBSCRIBER_TOKEN);
////                    in.putExtra("MeetingId", response.body().getData().getMeetingId());
//                    startActivity(in);

                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.text_title), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    private void getConnection(String name) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        /**
         GET Token
         **/
        Call<ConnectionModel> call = apiInterface.getConnection(name);
        call.enqueue(new Callback<ConnectionModel>() {
            @Override
            public void onResponse(Call<ConnectionModel> call, Response<ConnectionModel> response) {
                progress.setVisibility(View.GONE);
                tvWait.setVisibility(View.GONE);
                Log.d("TAG", response.toString());
                btnCreateSession.setVisibility(View.GONE);
                if (response != null && response.isSuccessful() && response.body().getData() != null) {
                    Log.d("TAG", response.body().getData().getToken() + "");
                    Intent in = new Intent(MainActivity.this, SessionActivity.class);
                    in.putExtra("_CreateOrJoinSession", "Create");
                    in.putExtra("_Token", response.body().getData().getSessionId());
                    in.putExtra("_SessionToken", response.body().getData().getToken());
                    in.putExtra("_MeetingId", response.body().getData().getMeetingId());
                    startActivity(in);
                } else {
                    btnCreateSession.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ConnectionModel> call, Throwable t) {
                progress.setVisibility(View.GONE);
                tvWait.setVisibility(View.GONE);
                call.cancel();
                btnCreateSession.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
            }

        });
    }


    private void callApi() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        /**
         GET Token
         **/
        Call<GetTokenResponse> call = apiInterface.getTokenResponseCall();
        call.enqueue(new Callback<GetTokenResponse>() {
            @Override
            public void onResponse(Call<GetTokenResponse> call, Response<GetTokenResponse> response) {
                progress.setVisibility(View.GONE);
                tvWait.setVisibility(View.GONE);
                if (response != null && response.isSuccessful() && response.body().getData() != null) {
                    Intent in = new Intent(MainActivity.this, SessionActivity.class);
                    in.putExtra("_CreateOrJoinSession", "Join");
                    in.putExtra("_Token", response.body().getData().getSessionId());
                    in.putExtra("_SessionToken", response.body().getData().getToken());
                    in.putExtra("_MeetingId", response.body().getData().getMeetingId());
                    startActivity(in);
                } else {
//                    btnJoinSession.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                }
                Log.d("TAG", response.body().getData().getToken() + "");
            }

            @Override
            public void onFailure(Call<GetTokenResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                tvWait.setVisibility(View.GONE);
                call.cancel();
//                btnJoinSession.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Invalid", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCreateSession.setVisibility(View.VISIBLE);
    }

    private void initView() {
//        btnJoinSession = findViewById(R.id.btnJoinSession);
        btnCreateSession = findViewById(R.id.btnCreateSession);
        progress = findViewById(R.id.progress);
        tvWait = findViewById(R.id.tv_wait);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic and storage to make video calls", RC_VIDEO_APP_PERM, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }
}
