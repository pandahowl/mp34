package com.vreader.mp4;

import java.util.Timer;
import java.util.TimerTask;






import com.vreader.mp4.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

public class SVideoPlayer extends Activity {
    
    VideoView video;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private ImageView mPercent;
    private ImageView mOperationfull;
    private ImageView mfull;
    //聲音設定
    private AudioManager mAudioManager;
    /** 最大聲音 */
    private int mMaxVolume;
    /** 當前聲音 */
    private int mVolume = -1;
    /** 當前亮度 */
    private float mBrightness = -1f;
    /** 當前縮放模式 */
    private GestureDetector mGestureDetector;
	RelativeLayout topView = null;
	LinearLayout contr = null;
	ImageButton back;
	Builder MyAlertDialog;
	Button backc;
	Button timec;
	Button chanal;
	Button list;
	String tValue;
	String righturi;
	String lefturi;
	String videourl;
	String name;
	String movieId;
	long lastClick;
	ImageButton time;
	DisplayMetrics dm;
	int ScreenWidth, ScreenHeight;
	int oldOrientation = -1;
	int langu;
	proportionDialog dialogpo;
	ListDialog dialogli;
	topDialog dialog;
	loadingDialog loadingp;
	SettingDialog dialogset;
	ControllerDialog controllerDialog;
	String usertoken;
	String quality = "auto";
	//private static final String DB_FILE = "movie.db",
	//							DB_TABLE = "movie";
	//private SQLiteDatabase movieDbRW;
	Handler handler;
 	Runnable runnable;
 	int o=1;
 	Builder MyDialog;
 	String movieurl;
 	//int lay=VideoView.VIDEO_LAYOUT_ProportionThree;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

		.detectDiskReads()

		.detectDiskWrites()

		.detectNetwork()

		.penaltyLog()

		.build());
    	
        super.onCreate(savedInstanceState);
        //if (!LibsChecker.checkVitamioLibs(this))
		//	return;
        dm = getResources().getDisplayMetrics();
		ScreenWidth = dm.widthPixels;
		ScreenHeight = dm.heightPixels;
		Log.d("Howard", "onCreate..........");
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		movieurl = bundle.getString("movieURL");
//		righturi = bundle.getString("righturl");
//		lefturi = bundle.getString("lefturl");
//		usertoken = bundle.getString("usertoken");
//		movieId = bundle.getString("movieId");
//		name = bundle.getString("movieName");
//		langu = bundle.getInt("language");
		
		//語言更改設定
//		if(langu==1){
//			Resources res = getResources();
//			Configuration conf = res.getConfiguration();
//			conf.locale = Locale.getDefault();
//			DisplayMetrics dm = res.getDisplayMetrics();
//			res.updateConfiguration(conf, dm);
//		}
//		else if(langu==2){
//			Resources res = getResources();
//			Configuration conf = res.getConfiguration();
//			conf.locale = new Locale("vi");
//			DisplayMetrics dm = res.getDisplayMetrics();
//			res.updateConfiguration(conf, dm);
//		}
		
        setContentView(R.layout.smovie);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
        video = new VideoView(this);
        video = (VideoView) findViewById(R.id.surface_view);
        contr = (LinearLayout)findViewById(R.id.contr);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);
        mPercent = (ImageView) findViewById(R.id.opercent);
        mOperationfull = (ImageView) findViewById(R.id.operation_full);
        mfull = (ImageView) findViewById(R.id.ofull);
        topView = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        backc = (Button) findViewById(R.id.backw2);
        timec = (Button) findViewById(R.id.listw2);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        showloading();
        
        backc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				SVideoPlayer.this.finish();
			}
		});
        timec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sortButtonClick();
			}
		});
        
        
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){	
    		final View decor = getWindow().getDecorView();
    		decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
    			public void onSystemUiVisibilityChange(int visibility) {
    				new Handler().postDelayed(new Runnable() {
    					public void run() {
    						decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    								| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    								| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    								| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide
    								| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status
    								| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    						
    					}
    				}, 3000);
    			}
    		});
    		decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
    				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
    				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    		}
        
        
        
        mGestureDetector = new GestureDetector(this, new MyGestureListener());
     //   MovieDbHelper movDbHp = new MovieDbHelper(
       // 		getApplicationContext(), DB_FILE,
        //		null, 1);
        
      //  movDbHp.sCreateTableCommand = "CREATE TABLE " + DB_TABLE + "(" +
        //							"_id INTEGER PRIMARY KEY," +
        //							"position INTEGER,"+
        //							"url TEXT,"+
        //							"movieid TEXT);";
       
      //  movieDbRW = movDbHp.getWritableDatabase();
        
        
        loadActivity(movieurl);
        
        
        handler = new Handler();
		runnable = new Runnable() {
		public void run ( ) {
			
			topView.setVisibility(View.INVISIBLE);
			if(controllerDialog.isShowing()){
				controllerDialog.hide();
			}
		};};
		
		this.showController();
		topView.setVisibility(View.VISIBLE);
		handler.postDelayed(runnable,5000);
		
			//video.setVideoLayout(VideoView.VIDEO_LAYOUT_ProportionThreew, 0);
		
    }


	private void loadActivity(String movieurl) {
		Log.d("Howard", "loadActivity..........");
//		if (tValue2.equals("")) {
//			Toast toast2 = Toast.makeText(getApplicationContext(), righturi2,
//					Toast.LENGTH_LONG);
//			toast2.setGravity(Gravity.CENTER_HORIZONTAL
//					| Gravity.CENTER_VERTICAL, 0, 0);
//			toast2.show();
//		} else {
			
		//movieurl = "http://s3-ap-northeast-1.amazonaws.com/lendyteam/0020/0020.m3u8";
			
//			if(!videourl.equals("error")){
			//controller = new MediaController(this);
//			Cursor c = null;
//	        c = movieDbRW.query(true, DB_TABLE, new String[]{"position","url","movieid"},
//					"movieid=" + "\"" + movieId + "\"",
//					null, null, null, null, null); 
			video.setVideoURI(Uri.parse(movieurl));
			video.requestFocus();
			
			video.start();
			loadingp.dismiss();
//			 if(c==null || c.getCount() == 0){
//			 }else{
//				 ShowMsgDialog();
//			 }
			/*video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mediaPlayer) {
					mediaPlayer.setPlaybackSpeed(1.0f);
					controller.setFileName(name);
					
					video.setOnInfoListener(new OnInfoListener(){

						@Override
						public boolean onInfo(MediaPlayer mp, int what,
								int extra) {
							// TODO 自動產生的方法 Stub
							if (what==702){
								loadingp.dismiss();
								video.start();
							}
							
							return false;
						}});
				}
			});*/
//			TimerTask task = new TimerTask(){ 
//			    public void run(){
//			    	if(video.isPlaying()){
//			    	long posi=video.getCurrentPosition();
//			    	Log.d("Howard", "positionSave.........."+posi);
//			    	Cursor c = null;
//			        c = movieDbRW.query(true, DB_TABLE, new String[]{"position","url","movieid"},
//			        		"movieid=" + "\"" + movieId + "\"",
//							null, null, null, null, null);  
//			        if(posi>10){
//			        if(c==null || c.getCount() == 0){
//			        	ContentValues newRow = new ContentValues();
//			        	newRow.put("position", posi);
//			        	newRow.put("url", videourl);
//			        	newRow.put("movieid", movieId);
//			        	movieDbRW.insert(DB_TABLE, null, newRow);
//			        }else{
//			        	 ContentValues newRow = new ContentValues();
//				        	newRow.put("position", posi);
//				        	newRow.put("url", videourl);
//				        	newRow.put("movieid", movieId);
//				        	movieDbRW.update(DB_TABLE,newRow,"movieid=" + "\"" + movieId + "\"",null);
//			        }
//			    }
//			    }}
//			};
			
			//video.setMediaController(controller);
//			Timer timer = new Timer();
//			timer.schedule(task, 10000,10000);
		}
//	}
//}
	
   
//    private void ShowMsgDialog()
//	{
//    	Log.d("Howard", "ShowMsgDialog..........");
//	MyAlertDialog = new AlertDialog.Builder(this);
//	MyAlertDialog.setTitle(string.confirm);
//	MyAlertDialog.setMessage(string.beginingorcontinue);
//	MyAlertDialog.setPositiveButton(string.fbegining,new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int whichButton) {
//        }
//    });
//	MyAlertDialog.setNeutralButton(string.conti,new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int whichButton) {
//        	Log.d("Howard", "ShowMsgDialog....onClick......");
//        	Cursor c = null;
//	        c = movieDbRW.query(true, DB_TABLE, new String[]{"position","url","movieid"},
//					"movieid=" + "\"" + movieId + "\"",
//					null, null, null, null, null); 
//			video.setVideoURI(Uri.parse(videourl));
//			video.requestFocus();
//        	c.moveToFirst();
//			 video.seekTo(c.getLong(0));
//        }
//    });
//	MyAlertDialog.show();
//	
//	}
    
    public int px(int dp) {
		return getPX(this, dp);
	}

	static public int getPX(Context context, float dp) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int density = metrics.densityDpi;
		float factor = (float) density / 160.f;
		int px = (int) (dp * factor);
		return px;
	}
    	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	
    	
    	
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 處理手勢結束
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_UP:
            endGesture();
            break;
        }

        return super.onTouchEvent(event);
    }

    /** 手勢結束 */
    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隱藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /*public String takeurl(String url, String usertoken, String quality)
			throws JSONException {
    	Log.d("Howard", "takeurl..........");
		JSONObject json = this.getJSONData(url, usertoken, quality);
		String contentLink="";
		try {
			int returnCode = json.getInt("returnCode");
			contentLink = json.getString("contentLink");
			if (returnCode == 0) {
				return contentLink;
			} else if (returnCode == -2) {
				ShowMsgDialog("This account has been signed in on another device.",returnCode);
				
			}else if (returnCode == -3) {
				ShowMsgDialog("Your account has been locked.",returnCode);
				
			} else if (returnCode == -4) {
				ShowMsgDialog("Your account balance is insufficent.",returnCode);
				
			} else if (returnCode == -5) {
				Toast toast = Toast.makeText(getApplicationContext(), string.novideofeeds,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_HORIZONTAL
						| Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
			}
		} catch (Exception e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
			ShowErDialog(string.networkstatus,-6);
		}
		return "error";
	}

	public JSONObject getJSONData(String url, String usertoken, String quality) {
		Log.d("Howard", "getJSONData..........");
		String url2 = url + "/" + quality + "/" + usertoken;
		HttpGet httpget = new HttpGet(url2);
		try {
			HttpResponse httpresponse = CustomHttpClient.getHttpClient()
					.execute(httpget);
			String result = EntityUtils.toString(httpresponse.getEntity());
			Log.e("result", result);
			JSONObject jsonarr = new JSONObject(result);
			return jsonarr;
		} catch (Exception e) {
			e.printStackTrace();

			return null;

		}
	}*/
    
	
	private void ShowErDialog(int Msg,final int returnCode)
	{
	MyAlertDialog = new AlertDialog.Builder(this);
	MyAlertDialog.setTitle(string.error);
	MyAlertDialog.setMessage(string.networkstatus);
	DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener()
	{
	public void onClick(DialogInterface dialog, int which) {
	//如果不做任何事情 就會直接關閉 對話方塊
		onBackPressed();
		SVideoPlayer.this.finish();
	}
	};;
	MyAlertDialog.setNeutralButton(string.ok,OkClick );
	MyAlertDialog.show();
	
	}
	
	
    private class MyGestureListener extends SimpleOnGestureListener {
    
    	
    	
    
    	@Override
    	public boolean onSingleTapUp (MotionEvent e){
    		
			if(topView.isShown()){
				topView.setVisibility(View.INVISIBLE);
				handler.removeCallbacks(runnable);
				if(controllerDialog.isShowing()){
					controllerDialog.hide();
				}
			}
			else{
				topView.setVisibility(View.VISIBLE);
				handler.postDelayed(runnable,5000); // 开始Timer
				controllerDialog.show();
			}
    		return true;
    	}
    	
    	
        /** 滑動 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)// 右邊滑動
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)// 左邊滑動
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /** 定時隱藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    /**
     * 滑動改變聲音大小
     * 
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 顯示
            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            mfull.setVisibility(View.INVISIBLE);
            mPercent.setVisibility(View.INVISIBLE);
            mOperationPercent.setVisibility(View.VISIBLE);
            mOperationfull.setVisibility(View.VISIBLE);
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 變更聲音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        
        
       
        
        // 變更進度條
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.height = findViewById(R.id.operation_full).getLayoutParams().height-findViewById(R.id.operation_full).getLayoutParams().height
                * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }
  
    /**
     * 滑動改變亮度
     * 
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 顯示
            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
            mOperationPercent.setVisibility(View.INVISIBLE);
            mOperationfull.setVisibility(View.INVISIBLE);
            mfull.setVisibility(View.VISIBLE);
            mPercent.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mPercent.getLayoutParams();
        lp.height = (int) (findViewById(R.id.ofull).getLayoutParams().height-findViewById(R.id.ofull).getLayoutParams().height * lpa.screenBrightness);
        mPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
		if (oldOrientation != newConfig.orientation) {
			if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.d("Howard", "horizontal..........");
				// 橫向
				//video.setVideoLayout(lay, 0);
				//top();
				topView.setVisibility(View.VISIBLE);
				handler.postDelayed(runnable,5000);
				controllerDialog.show();
				
			} else {
				Log.d("Howard", "Vertical..........");
				// 直向
				//video.setVideoLayout(lay, 0);
				//top();
				controllerDialog.show();
			}
			oldOrientation = newConfig.orientation;
		}
    }
    
    public void sortButtonClick() {
    	Log.d("Howard", "sortButtonClick..........");
		dialogli = new ListDialog(this);
		Window dialogWindow = dialogli.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
		lp.y = 20;
		lp.alpha = 100; // Transparency

		dialogWindow.setAttributes(lp);

		dialogli.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialogli.show();
		final Timer v2 = new Timer();
		v2.schedule(new TimerTask() {
			public void run() {

				dialogli.dismiss();
				v2.cancel(); 
			}
		}, 4000);
	}
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (handler != null) {
        	handler.removeCallbacks(runnable);
        }
    }
    public void showloading() {
    	Log.d("Howard", "showloading..........");
		loadingp = new loadingDialog(this);
		Window dialogWindow = loadingp.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dm = getResources().getDisplayMetrics();
		ScreenWidth = dm.widthPixels;
		ScreenHeight = dm.heightPixels;
		dialogWindow.setGravity(Gravity.CENTER);
		//lp.y = -200; // The new position of the Y coordinates
		lp.height=ScreenHeight;
		lp.width=ScreenWidth;
		lp.alpha = 1.0f; // Transparency
		dialogWindow.setAttributes(lp);
		loadingp.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		loadingp.show();
	}
    
    public void showController() {
    	Log.d("Howard", "showController..........");
    	controllerDialog = new ControllerDialog(this);
		Window dialogWindow = loadingp.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		
		controllerDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		controllerDialog.show();
	}
    
    public void quiteyButtonClick() {
    	Log.d("Howard", "quiteyButtonClick..........");
		dialogset = new SettingDialog(this);
		Window dialogWindow = dialogset.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.y = -200; // The new position of the Y coordinates
		lp.alpha = 100; // Transparency
		dialogWindow.setAttributes(lp);
		dialogset.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialogset.show();
	}

	public void ratioButtonClick() {
		Log.d("Howard", "ratioButtonClick..........");
		dialogpo = new proportionDialog(this);
		Window dialogWindow = dialogpo.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.y = -200; // The new position of the Y coordinates
		lp.alpha = 100; // Transparency
		dialogWindow.setAttributes(lp);
		dialogpo.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		dialogpo.show();
	}
	
	private void ShowMsgDialog(String Msg,final int returnCode)
	{
		Log.d("Howard", "ShowMsgDialog..........");
	MyAlertDialog = new AlertDialog.Builder(this);
	MyAlertDialog.setTitle(string.error);
	if(returnCode==-2)
		MyAlertDialog.setMessage(string.siinotherdevice);
	else if(returnCode==-3)
		MyAlertDialog.setMessage(string.accountlocked);
		else if(returnCode==-4)
			MyAlertDialog.setMessage(string.accountinsufficent);
		else if(returnCode==0)
			MyAlertDialog.setMessage(string.networkstatus);
	DialogInterface.OnClickListener OkClick = new DialogInterface.OnClickListener()
	{
	public void onClick(DialogInterface dialog, int which) {
	//如果不做任何事情 就會直接關閉 對話方塊
//		Intent mediaIntent = new Intent(SVideoPlayer.this,WebViewActivity.class);
//    	//修改的地方 start
//    	Bundle bundle = new Bundle();
//    	bundle.putInt("bk", returnCode);
//    	//修改的地方 end
//    	mediaIntent.putExtras(bundle); //將參數放入
//    	startActivity(mediaIntent);
	}
	};;
	MyAlertDialog.setNeutralButton(string.ok,OkClick );
	MyAlertDialog.show();
	
	}
    
    class ListDialog extends Dialog implements OnClickListener {
		Button reloadButton;
		Button autoButton;
		Button ratioButton;
		Button setButton;

		public ListDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.list);

			reloadButton = (Button) findViewById(R.id.reload);
			autoButton = (Button) findViewById(R.id.auto);
			ratioButton = (Button) findViewById(R.id.ratio);
			setButton = (Button) findViewById(R.id.set);

			reloadButton.setOnClickListener(this);
			autoButton.setOnClickListener(this);
			ratioButton.setOnClickListener(this);
			setButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Button button = (Button) view;
			button.setTextColor(Color.LTGRAY);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

			if (view == reloadButton) {
				loadActivity(movieurl);
			} else if (view == autoButton) {
				int vOrientation = getRequestedOrientation();
				if (vOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				} else if (vOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			} else if (view == ratioButton) {
				ratioButtonClick();
			} else {
				quiteyButtonClick();
			}

			dismiss();
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);
			int ex = (int) ev.getX();
			int ey = (int) ev.getY();
			if (!dialogBounds.contains(ex, ey)) {
				dismiss();
			}
			return super.dispatchTouchEvent(ev);
		}
	}

	class topDialog extends Dialog implements OnClickListener {
		Button backButton;
		Button listButton;

		public topDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.topbar2);

			backButton = (Button) findViewById(R.id.backw2);
			listButton = (Button) findViewById(R.id.listw2);

			backButton.setOnClickListener(this);
			listButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Button button = (Button) view;
			button.setTextColor(Color.LTGRAY);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			if (view == backButton) {
				onBackPressed();
				SVideoPlayer.this.finish();
			} else if (view == listButton) {
				sortButtonClick();
			} else {
				getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				dismiss();
			}

		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);
			int ex = (int) ev.getX();
			int ey = (int) ev.getY();
			if (!dialogBounds.contains(ex, ey)) {
				getWindow().getDecorView().setSystemUiVisibility(
						View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
				dismiss();
			}
			return super.dispatchTouchEvent(ev);
		}
	}

	
	
	class proportionDialog extends Dialog implements OnClickListener {
		Button fullButton;
		Button nineButton;
		Button tenButton;
		Button threeButton;

		public proportionDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.proportionmenu);

			fullButton = (Button) findViewById(R.id.full);
			nineButton = (Button) findViewById(R.id.nine);
			tenButton = (Button) findViewById(R.id.ten);
			threeButton = (Button) findViewById(R.id.three);

			fullButton.setOnClickListener(this);
			nineButton.setOnClickListener(this);
			tenButton.setOnClickListener(this);
			threeButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Button button = (Button) view;
			button.setTextColor(Color.LTGRAY);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			dm = getResources().getDisplayMetrics();
			// 取得螢幕顯示的資料
			ScreenWidth = dm.widthPixels;
			ScreenHeight = dm.heightPixels;
//			if (view == fullButton) {
//				video.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);
//				lay=VideoView.VIDEO_LAYOUT_ProportionNine;
//			} else if (view == nineButton) {
//				
//					video.setVideoLayout(VideoView.VIDEO_LAYOUT_ProportionNine, 0);
//					lay=VideoView.VIDEO_LAYOUT_ProportionNine;
//			} else if (view == tenButton) {
//				
//					video.setVideoLayout(VideoView.VIDEO_LAYOUT_Proportionten, 0);
//					lay=VideoView.VIDEO_LAYOUT_Proportionten;
//			} else if (view == threeButton) {
//				
//					video.setVideoLayout(VideoView.VIDEO_LAYOUT_ProportionThree, 0);
//					lay=VideoView.VIDEO_LAYOUT_ProportionThree;
//			}

			dismiss();
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);
			int ex = (int) ev.getX();
			int ey = (int) ev.getY();
			if (!dialogBounds.contains(ex, ey)) {
				dismiss();
			}
			return super.dispatchTouchEvent(ev);
		}
	}

	class SettingDialog extends Dialog implements OnClickListener {
		Button normalButton;
		Button fineButton;
		Button goodButton;

		public SettingDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.setting);

			normalButton = (Button) findViewById(R.id.Normal);
			fineButton = (Button) findViewById(R.id.Fine);
			goodButton = (Button) findViewById(R.id.Good);

			normalButton.setOnClickListener(this);
			fineButton.setOnClickListener(this);
			goodButton.setOnClickListener(this);

		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Button button = (Button) view;
			button.setTextColor(Color.LTGRAY);
			button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

			if (view == normalButton) {
				quality = "normal";
			} else if (view == fineButton) {
				quality = "fine";
			} else if (view == goodButton) {
				quality = "good";
			}

			dismiss();
			loadActivity(movieurl);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);
			int ex = (int) ev.getX();
			int ey = (int) ev.getY();
			if (!dialogBounds.contains(ex, ey)) {
				dismiss();
			}
			return super.dispatchTouchEvent(ev);
		}
	}
	class loadingDialog extends Dialog implements OnClickListener {
		

		public loadingDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.loading);

			

		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			

		}

		
	}
	
	class ControllerDialog extends Dialog implements OnClickListener , OnSeekBarChangeListener{
		ImageView playButton;
		SeekBar seekBar;
		TextView position;
		
		public ControllerDialog(Context context) {
			super(context);

			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.mp4controller);

			playButton = (ImageView) findViewById(R.id.mp4playbtn);
			seekBar = (SeekBar) findViewById(R.id.mp4seekBar);
			position = (TextView) findViewById(R.id.mp4position);

			playButton.setOnClickListener(this);
			seekBar.setOnSeekBarChangeListener(null);
		}

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

			if (view == playButton) {
				if(video.isPlaying()){
					playButton.setBackgroundResource(android.R.drawable.ic_media_play);
					video.pause();
					}
				else{
					playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
					video.start();
				}
			} 

			dismiss();
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			Rect dialogBounds = new Rect();
			getWindow().getDecorView().getHitRect(dialogBounds);
			int ex = (int) ev.getX();
			int ey = (int) ev.getY();
			if (!dialogBounds.contains(ex, ey)) {
				dismiss();
			}
			return super.dispatchTouchEvent(ev);
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
	}
	
} 

