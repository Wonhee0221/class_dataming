package com.example.Diary;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



/**
 * 고객메뉴 Activity
 */
public class diartActivity extends Activity implements OnClickListener {
	String str_num="";
	String str_pass="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//layout 설정
		setContentView(R.layout.cusmenu);
		//각 버튼 초기화
		Button b1=(Button)findViewById(R.id.button1);
		b1.setOnClickListener(this);
		Button b2=(Button)findViewById(R.id.Button01);
		b2.setOnClickListener(this);
		Button b3=(Button)findViewById(R.id.Button02);
		b3.setOnClickListener(this);
		Button b4=(Button)findViewById(R.id.Button06);
		b4.setOnClickListener(this);

		Button b6=(Button)findViewById(R.id.acc);
		b6.setOnClickListener(this);
		//넘거받은 아이디와 비밀번호 값을 변수에 저장
		Intent it = getIntent();
		str_num = it.getStringExtra("it_num");
		str_pass = it.getStringExtra("it_pass");

	}
	//뒤로가기버튼 클릭시
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ( event.getAction() == KeyEvent.ACTION_DOWN )
		{
			if ( keyCode == KeyEvent.KEYCODE_BACK )
			{
				Intent intent = new Intent(this,login.class);
				startActivity(intent);
				finish();
			}

		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}
	//버튼클릭시 작동 이벤트
	Intent intent;
	public void onClick(View arg0) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
		//intent에 값 추출하여 변수에 저장
		Intent it = getIntent();
		str_num = it.getStringExtra("it_num");
		str_pass = it.getStringExtra("it_pass");
		switch(arg0.getId())
		{
			//각 버튼 클릭시 해당 Activity로 이동
    	/*
    	case R.id.button1:
    		intent=new Intent(this,cproductListActivity.class);
    		intent.putExtra("it_num",str_num);
    		intent.putExtra("it_pass",str_pass);
    		startActivity(intent);
    		finish();
    		break;
    		//각 버튼 클릭시 해당 Activity로 이동
    	case R.id.Button02:
    		intent=new Intent(this,TabViewActivity1.class);

    		intent.putExtra("it_num",str_num);
    		intent.putExtra("it_pass",str_pass);
    		startActivity(intent);
    		finish();
    		break;
    		//각 버튼 클릭시 해당 Activity로 이동
    	case R.id.Button06:
    		intent=new Intent(this,CookListActivity.class);
    		intent.putExtra("it_num",str_num);
    		intent.putExtra("it_pass",str_pass);
    		startActivity(intent);
    		finish();
    		break;
    		//각 버튼 클릭시 해당 Activity로 이동
    	case R.id.Button01:
    		intent=new Intent(this,conorder.class);
    		intent.putExtra("it_num",str_num);
    		//intent.putExtra("it_pass",str_pass);
    		startActivity(intent);
    		finish();
    		break;

    		//각 버튼 클릭시 해당 Activity로 이동
    	case R.id.acc:
    		intent=new Intent(this,accountActivity.class);
    		intent.putExtra("it_num",str_num);
    		intent.putExtra("it_pass",str_pass);
    		startActivity(intent);
    		finish();
    		break; */

		}



	}

}

