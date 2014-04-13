package com.spacebottle.controllers;import java.util.Date;import java.util.List;import java.util.Timer;import java.util.TimerTask;import android.app.AlertDialog;import android.app.Dialog;import android.content.Context;import android.content.DialogInterface;import android.content.Intent;import android.content.SharedPreferences;import android.location.Criteria;import android.location.LocationManager;import android.os.AsyncTask;import android.os.Bundle;import android.os.Handler;import android.view.LayoutInflater;import android.view.View;import android.view.Window;import android.widget.EditText;import android.widget.ImageView;import android.widget.ProgressBar;import android.widget.TextView;import com.example.spacebottle.R;import com.example.spacebottle.SendMessageActivity;import com.google.android.gms.gcm.GoogleCloudMessaging;import com.microsoft.windowsazure.messaging.NotificationHub;import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;import com.microsoft.windowsazure.mobileservices.TableOperationCallback;import com.microsoft.windowsazure.mobileservices.TableQueryCallback;import com.microsoft.windowsazure.notifications.NotificationsManager;import com.spacebottle.helper.SBAuthenticateCallback;import com.spacebottle.models.Device;import com.spacebottle.models.Devices;import com.spacebottle.models.Message;import com.spacebottle.models.Messages;import com.spacebottle.models.Position;import com.spacebottle.models.Positions;import com.spacebottle.models.Tickets;public class PostActivity extends SBActivity {	public static final String PREFERENCES_FILE_NAME = "preference";	private Tickets mTickets;	private long limit;	private Timer timer = null;	private Handler handle = new Handler();	private double latitude;	private double longitude;	private Position mPosition;	private SharedPreferences pref;	private String message;	private Intent intent;	private Devices mDevices;	private Positions mPositions;	private LocationManager mLocationManager;	private Criteria mCriteria;	private GoogleCloudMessaging gcm;	private NotificationHub hub;	private TextView messageTextView;	private ImageView postButton;	private TextView limitClock;	public PostActivity() {		// TODO Auto-generated constructor stub	}	@Override	public void onCreate(Bundle savedInstanceState) {		super.onCreate(savedInstanceState);		requestWindowFeature(Window.FEATURE_NO_TITLE);		setContentView(R.layout.activity_post);		messageTextView = (TextView) findViewById(R.id.message_text_view);		postButton = (ImageView) findViewById(R.id.post_button);		limitClock = (TextView) findViewById(R.id.limit_clock);		self = this;		setProgressBar((ProgressBar) findViewById(R.id.loadingProgressBar));		hideProgressBar();		NotificationsManager.handleNotifications(this, getString(R.string.sender_id), PushHandler.class);		intent = getIntent();		mPosition = (Position)intent.getSerializableExtra("position");		message = (String)intent.getStringExtra("message_text");		if(message == ""){			messageTextView.setText("メッセージはありません");		} else {			messageTextView.setText(message);		}		initializeLimitClock();		postButton.setOnClickListener(new View.OnClickListener() {			@Override			public void onClick(View v) {				// TODO Auto-generated method stub				showDialog(0);			}		});		connect(new SBAuthenticateCallback(){			@Override			public void success() {				hideProgressBar();				initialize();			}			@Override			public void error(Exception exception) {				// TODO Auto-generated method stub			}});	}	private void initialize(){		NotificationsManager.handleNotifications(this, "879711313152", PushHandler.class);		gcm = GoogleCloudMessaging.getInstance(this);		hub = new NotificationHub(getString(R.string.hub_name), getString(R.string.connection_string), this);		registerWithNotificationHubs();		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);		mCriteria = new Criteria();		mCriteria.setAccuracy(Criteria.ACCURACY_COARSE); // �ｽ瘰ｸ�ｽx		mCriteria.setPowerRequirement(Criteria.POWER_LOW); // �ｽ�ｽ�ｽ�ｽ�ｽd�ｽ�ｽ		mPositions = new Positions(mClient);		mPositions.read(new TableQueryCallback<Position>(){			@Override			public void onCompleted(List<Position> positions, int count,					Exception exception, ServiceFilterResponse response) {				if(positions.isEmpty()){					mPosition = new Position();				} else {					mPosition = positions.get(0);				}			}} );	}	private void initializeLimitClock() {		if (timer == null) {			Date now = new Date();			timer = new Timer();			pref = getSharedPreferences(PREFERENCES_FILE_NAME, 0);			limit = (Long)pref.getLong("limit", (new Date()).getTime());			long ms = limit + 600000 - now.getTime();			int second = (int) ms / 1000;			int minute = (int) second / 60;			second = second - 60 * minute;			limitClock.setText(minute + ":" + second);			timer.schedule(new MyTimer(), 1000,1000); // ミリ秒でセット		}	}	class MyTimer extends TimerTask {		@Override		public void run() {			handle.post(new Runnable() {				@Override				public void run() {					Date now = new Date();					if(now.getTime() < limit + 600000){						long ms = limit + 600000 - now.getTime();						int second = (int) ms / 1000;						int minute = (int) second / 60;						second = second - 60 * minute;						if(second < 10){							second = '0' + second;						}						limitClock.setText(minute + ":" + second);					} else {						Intent intent = new Intent(getApplicationContext(),ReceiveMessageActivity.class);						startActivity(intent);					}				}			});		}	}	@Override	protected Dialog onCreateDialog(int id) {	        //レイアウトの呼び出し	        LayoutInflater factory = LayoutInflater.from(this);	        final View inputView = factory.inflate(R.layout.edit_dialog, null);	        //ダイアログの作成(AlertDialog.Builder)	        return new AlertDialog.Builder(PostActivity.this)	            .setIcon(android.R.drawable.ic_dialog_alert)	            .setTitle("タイトル")	            .setView(inputView)	            .setPositiveButton("OK", new DialogInterface.OnClickListener() {	                public void onClick(DialogInterface dialog, int whichButton) {	                	EditText edit = (EditText)inputView.findViewById(R.id.dialog_edittext);	                	sendMessage(edit.getText().toString());	                }	            })	            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {	                public void onClick(DialogInterface dialog, int whichButton) {	                    /* キャンセル処理 */	                }	            })	            .create();	}	@SuppressWarnings("unchecked")	private void registerWithNotificationHubs() {	   new AsyncTask() {	      @Override	      protected Object doInBackground(Object... params) {	         try {	            String regid = gcm.register("879711313152");	            //hub.register(regid);	            mDevices = new Devices(mClient);	    		mDevices.add(new Device(hub.register(regid).getRegistrationId()));	         } catch (Exception e) {	            return e;	         }	         return null;	     }	   }.execute(null, null, null);	}	public void sendMessage(String message){		Message msg = new Message();		msg.setText(message);		latitude = mPosition.getLatitude();		longitude = mPosition.getLogitude();		msg.setLatitude(latitude);		msg.setLongitude(longitude);		msg.setSatelliteId(intent.getStringExtra("satellite_id"));		msg.setTicketId(intent.getStringExtra("ticket_id"));		msg.setAnonymous(false);		Messages msgs = new Messages(mClient);		msgs.add(msg,new TableOperationCallback<Message>(){			@Override			public void onCompleted(Message arg0, Exception arg1,					ServiceFilterResponse arg2) {				// TODO 自動生成されたメソッド・スタブ				Intent intent3 = new Intent(getApplicationContext(),SendMessageActivity.class);				startActivity(intent3);			}		});	}}