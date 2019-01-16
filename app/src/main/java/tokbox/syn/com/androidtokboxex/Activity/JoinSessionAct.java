package tokbox.syn.com.androidtokboxex.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import tokbox.syn.com.androidtokboxex.R;
import tokbox.syn.com.androidtokboxex.Utills.Const;

public class JoinSessionAct extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final int RC_VIDEO_APP_PERM = 124;
    protected AppCompatButton btnSessionTokenClear;
    protected AppCompatButton btnTokenClear;
    private AppCompatButton btnStart;
    private AppCompatEditText edToken;
    private AppCompatEditText edSessionToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_join_session);
        initView();
        requestPermissions();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tk = Const.SESSION_ID;
                String sTk = Const.SUBSCRIBER_TOKEN;
                //!TextUtils.isEmpty(tk.trim()) && !TextUtils.isEmpty(sTk.trim())
                if (true) {
                    Intent in = new Intent(JoinSessionAct.this, SessionActivity.class);
                    in.putExtra("_CreateOrJoinSession", "Join");
                    in.putExtra("_Token", tk);
                    in.putExtra("_SessionToken", sTk);
                    startActivity(in);
                } else {
                    Toast.makeText(v.getContext(), "Please Enter Session And Subscriber Token", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSessionTokenClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSessionToken.setText("");
            }
        });

        btnTokenClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edToken.setText("");
            }
        });
    }

    private void initView() {
        btnStart = findViewById(R.id.btnStart);
        edToken = findViewById(R.id.edToken);
        edSessionToken = findViewById(R.id.edSessionToken);
        btnSessionTokenClear = findViewById(R.id.btnSessionTokenClear);
        btnTokenClear = findViewById(R.id.btnTokenClear);
    }

    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions() {
        String[] perms = {Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to your camera and mic to make video calls", RC_VIDEO_APP_PERM, perms);
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
