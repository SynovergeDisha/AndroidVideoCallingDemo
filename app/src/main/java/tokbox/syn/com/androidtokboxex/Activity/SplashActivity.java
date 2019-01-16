package tokbox.syn.com.androidtokboxex.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import tokbox.syn.com.androidtokboxex.R;

public class SplashActivity extends Activity {
    private static final int DURATION = 5 * 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        }, DURATION);
    }
}
