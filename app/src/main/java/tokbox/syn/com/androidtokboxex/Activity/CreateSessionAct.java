package tokbox.syn.com.androidtokboxex.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import tokbox.syn.com.androidtokboxex.R;

public class CreateSessionAct extends AppCompatActivity {

    private AppCompatTextView tvSessionId;
    private AppCompatTextView tvToken;
    private AppCompatButton btnStartSession;
    private String sessionId = "";
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_create_session);
        initView();

        try {
            /*OpenTok sdk = new OpenTok(Const.PROJECT_API_KEY, Const.PROJECT_SECRET);
            SessionProperties sessionProperties = new SessionProperties.Builder()
                    .mediaMode(MediaMode.ROUTED)
                    .build();
            Session session = sdk.createSession(sessionProperties);
            sessionId = session.getSessionId();
            token = sdk.generateToken(sessionId);*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tvSessionId.setText(sessionId);
            tvToken.setText(token);
        }

        btnStartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(sessionId.trim()) && !TextUtils.isEmpty(token.trim())) {
                    Intent in = new Intent(CreateSessionAct.this, SessionActivity.class);
                    in.putExtra("_CreateOrJoinSession", "Create");
                    in.putExtra("_SessionToken", sessionId);
                    startActivity(in);
                } else {
                    Toast.makeText(v.getContext(), "Session And Subscriber Token Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initView() {
        tvSessionId = findViewById(R.id.tvSessionId);
        tvToken = findViewById(R.id.tvtoken);
        btnStartSession = findViewById(R.id.btnStartSession);
    }
}
