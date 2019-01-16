package tokbox.syn.com.androidtokboxex.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.opentok.android.BaseVideoRenderer;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tokbox.syn.com.androidtokboxex.Model.EndConnectionModel;
import tokbox.syn.com.androidtokboxex.R;
import tokbox.syn.com.androidtokboxex.Retrofit.APIClient;
import tokbox.syn.com.androidtokboxex.Retrofit.APIInterface;
import tokbox.syn.com.androidtokboxex.Utills.BasicCustomVideoRenderer;
import tokbox.syn.com.androidtokboxex.Utills.Const;

public class SessionActivity extends AppCompatActivity implements Session.SessionListener,
        PublisherKit.PublisherListener,
        SubscriberKit.SubscriberListener {

    private static final String LOG_TAG = SessionActivity.class.getSimpleName();
    private FrameLayout subscriberContainer;
    private FrameLayout publisherContainer;
    private ImageView ivPhotoCapture;
    private ImageView ivEndCall;
    private Session mSession;
    private Subscriber mSubscriber;
    private Publisher mPublisher;
    private TextView tvConnecting;
    private LinearLayout llCapture;
    private int currentCameraId;
    private ImageView ivSwipe;
    private APIInterface apiInterface;
    private LinearLayout llProgress;
    private String meetingId;
    private ArrayList<String> capturedImageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_session);
        initView();

        Bundle in = getIntent().getExtras();
        if (in != null) {
            String sessionType = in.getString("_CreateOrJoinSession");
            String sessionId = in.getString("_SessionToken");
            String subId = in.getString("_SessionId");
            String sessionToken = in.getString("_Token");
            meetingId = in.getString("_MeetingId");

            if (sessionType != null && sessionType.equals("Join")) {
                //Const.SESSION_ID = sessionId;
                //Const.SUBSCRIBER_TOKEN = subId;
                if (mSubscriber == null) {
                    ivPhotoCapture.setVisibility(View.GONE);
                    llCapture.setVisibility(View.GONE);
                } else {
                    ivPhotoCapture.setVisibility(View.VISIBLE);
                    llCapture.setVisibility(View.VISIBLE);
                }
                initializeSession(Const.SESSION_ID, Const.SUBSCRIBER_TOKEN);
            } else {
                if (mSubscriber == null) {
                    ivPhotoCapture.setVisibility(View.GONE);
                    llCapture.setVisibility(View.GONE);
                } else {
                    ivPhotoCapture.setVisibility(View.VISIBLE);
                    llCapture.setVisibility(View.VISIBLE);
                }
                Const.SESSION_ID = sessionToken;
                Const.SUBSCRIBER_TOKEN = sessionId;
                Log.d("TAG sessionid", Const.SESSION_ID);
                Log.d("TAG sessiontoken", Const.SUBSCRIBER_TOKEN);
                initializeSession(Const.SESSION_ID, Const.SUBSCRIBER_TOKEN);
            }
        }
        listener();
    }

    private void listener() {
        ivEndCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llProgress.setVisibility(View.VISIBLE);
                getEndCall();
            }
        });
        ivPhotoCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (capturedImageList.size() < 5) {
                    if (mSubscriber == null) {
                        ivPhotoCapture.setVisibility(View.GONE);
                        return;
                    }
                    String imageName;
                    if (capturedImageList == null || capturedImageList.size() <= 0) {
                        imageName = meetingId + "_Image1_" + ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).getCurrentTimeStamp();
                        capturedImageList.add(imageName);
                    } else {
                        imageName = meetingId + "_Image" + (capturedImageList.size() + 1) + "_" + ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).getCurrentTimeStamp();
                        capturedImageList.add(imageName);
                    }
                    ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).saveScreenshot(true);
                    ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).setMeetingId(meetingId);
                    ((BasicCustomVideoRenderer) mSubscriber.getRenderer()).setImageName(imageName);

                    Toast.makeText(SessionActivity.this, "Photo Captured.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SessionActivity.this, "You can capture maximum 5 photos in single call.", Toast.LENGTH_LONG).show();
                }
            }
        });
        ivSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPublisher != null)
                    mPublisher.cycleCamera();
            }
        });
    }


    private void getEndCall() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        /**
         GET Token
         **/
        Call<EndConnectionModel> call = apiInterface.stopMeeting(meetingId);
        call.enqueue(new Callback<EndConnectionModel>() {
            @Override
            public void onResponse(Call<EndConnectionModel> call, Response<EndConnectionModel> response) {
                llProgress.setVisibility(View.GONE);
                Log.d("TAG", response.toString());

                if (response != null && response.isSuccessful() && response.body().getStatus().toString().equalsIgnoreCase("ok")) {
                    onPause();
//                    onBackPressed();
                    Intent intent = new Intent(SessionActivity.this, SuccessActivity.class);
                    intent.putStringArrayListExtra("capturedImageList", capturedImageList);
                    intent.putExtra("meetingId", meetingId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SessionActivity.this, "Invalid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EndConnectionModel> call, Throwable t) {
                llProgress.setVisibility(View.GONE);
                Toast.makeText(SessionActivity.this, "Invalid", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        subscriberContainer = findViewById(R.id.subscriber_container);
        publisherContainer = findViewById(R.id.publisher_container);
        ivPhotoCapture = findViewById(R.id.act_session_iv_capture_photo);
        ivEndCall = findViewById(R.id.iv_end_call);
        tvConnecting = findViewById(R.id.tv_connecting);
        llCapture = findViewById(R.id.act_session_ll_capture);
        ivSwipe = findViewById(R.id.act_session_iv_switch_camera);
        llProgress = findViewById(R.id.ll_progress);
    }

    private void initializeSession(String sessionId, String token) {
        Log.d("TAG session id", sessionId);
        Log.d("TAG token", token);
        mSession = new Session.Builder(this, String.valueOf(Const.PROJECT_API_KEY), sessionId).build();
        mSession.setSessionListener(this);
        mSession.connect(token);
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");

        super.onPause();

        if (mSession == null) {
            return;
        }
        mSession.onPause();

        if (isFinishing()) {
            disconnectSession();
        }
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if (mSession != null) {
            mSession.onResume();
        } else {
            return;
        }
    }

    @Override
    public void onStreamCreated(PublisherKit publisherKit, Stream stream) {
        Log.d(LOG_TAG, "onStreamCreated: Publisher Stream Created. Own stream " + stream.getStreamId());
    }

    @Override
    public void onStreamDestroyed(PublisherKit publisherKit, Stream stream) {
        Log.d(LOG_TAG, "onStreamDestroyed: Publisher Stream Destroyed. Own stream " + stream.getStreamId());
    }

    @Override
    public void onError(PublisherKit publisherKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " + opentokError.getErrorCode() + " - " + opentokError.getMessage());
        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(Session session) {
        Log.d(LOG_TAG, "onConnected: Connected to session: " + session.getSessionId());
        ivEndCall.setVisibility(View.VISIBLE);
        tvConnecting.setVisibility(View.GONE);
        // initialize Publisher and set this object to listen to Publisher events
        mPublisher = new Publisher.Builder(this).build();
        mPublisher.setPublisherListener(this);
        // set publisher video style to fill view
        mPublisher.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
        publisherContainer.addView(mPublisher.getView());
        if (mPublisher.getView() instanceof GLSurfaceView) {
            ((GLSurfaceView) mPublisher.getView()).setZOrderOnTop(true);
        }
        mSession.publish(mPublisher);
    }

    @Override
    public void onDisconnected(Session session) {
        Log.d(LOG_TAG, "onDisconnected: Disconnected from session: " + session.getSessionId());
    }

    @Override
    public void onStreamReceived(Session session, Stream stream) {
        Log.d(LOG_TAG, "onStreamReceived: New Stream Received " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber == null) {
            mSubscriber = new Subscriber.Builder(this, stream).build();
            mSubscriber.setRenderer(new BasicCustomVideoRenderer(this));
            mSubscriber.getRenderer().setStyle(BaseVideoRenderer.STYLE_VIDEO_SCALE, BaseVideoRenderer.STYLE_VIDEO_FILL);
            mSubscriber.setSubscriberListener(this);
            mSession.subscribe(mSubscriber);
            subscriberContainer.addView(mSubscriber.getView());
            ivPhotoCapture.setVisibility(View.VISIBLE);
            llCapture.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStreamDropped(Session session, Stream stream) {
        Log.d(LOG_TAG, "onStreamDropped: Stream Dropped: " + stream.getStreamId() + " in session: " + session.getSessionId());

        if (mSubscriber != null) {
            mSubscriber = null;
            subscriberContainer.removeAllViews();
        }
    }

    @Override
    public void onError(Session session, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage() + " in session: " + session.getSessionId());

        showOpenTokError(opentokError);
    }

    @Override
    public void onConnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onConnected: Subscriber connected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onDisconnected(SubscriberKit subscriberKit) {
        Log.d(LOG_TAG, "onDisconnected: Subscriber disconnected. Stream: " + subscriberKit.getStream().getStreamId());
    }

    @Override
    public void onError(SubscriberKit subscriberKit, OpentokError opentokError) {
        Log.e(LOG_TAG, "onError: " + opentokError.getErrorDomain() + " : " +
                opentokError.getErrorCode() + " - " + opentokError.getMessage());

        showOpenTokError(opentokError);
    }

    private void showOpenTokError(OpentokError opentokError) {
        new AlertDialog.Builder(this)
                .setTitle(opentokError.getErrorDomain().name())
                .setMessage(opentokError.getMessage() + " Please, see the logcat.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        disconnectSession();
        super.onDestroy();
    }

    private void disconnectSession() {
        if (mSession == null) {
            return;
        }

        if (mSubscriber != null) {
            subscriberContainer.removeView(mSubscriber.getView());
            mSession.unsubscribe(mSubscriber);
            mSubscriber.destroy();
            mSubscriber = null;
        }

        if (mPublisher != null) {
            subscriberContainer.removeView(mPublisher.getView());
            mSession.unpublish(mPublisher);
            mPublisher.destroy();
            mPublisher = null;
        }

        mSession.disconnect();
    }
}
