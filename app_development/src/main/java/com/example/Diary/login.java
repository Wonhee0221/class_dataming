package com.example.Diary;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.Diary.R;

/**
 * 로그인 Activity
 *
 * @author 이민형
 */
public class login extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// layout 설정
		setContentView(R.layout.login);
		// 버튼 객체화 및 초기화
		Button b1 = (Button) findViewById(R.id.loginbtn);
		b1.setOnClickListener(this);
		Button b2 = (Button) findViewById(R.id.joincus);
		b2.setOnClickListener(this);

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	Intent it = new Intent();

	// 클릭시 작동 이벤트
	public void onClick(View arg0) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		// 아이디와 비밀번호 입력폼에서 값을 추출하여 변수에 저장 및 한글처리
		EditText et_name = (EditText) findViewById(R.id.login_id);
		String ustr_id = et_name.getText().toString();
		String str_id = "";
		try {
			str_id = URLEncoder.encode(ustr_id, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		EditText et_name01 = (EditText) findViewById(R.id.login_pw);
		String str_pass = et_name01.getText().toString();

		switch (arg0.getId()) {

			case R.id.loginbtn:

				//아이디와 비밀번호가 맞는지 입력받은 값을 파라메터로하여 php를 통해 고객테이블에서 해당 고객이있는지 조회
				try {

					URL url = new URL("http://172.16.161.168/check_id.php?u_id="
							+ str_id + "&u_pass=" + str_pass);
					URLConnection con = url.openConnection();

					BufferedReader br = new BufferedReader(new InputStreamReader(
							con.getInputStream()));
					String temp = "";
					//조회된 값이 없다면 고객이 없거나, 아이디나 비밀번호가 틀린상황으로 앙내창 출력
					if ((temp = br.readLine()) == null) {
						AlertDialog.Builder alert = new AlertDialog.Builder(this);
						alert.setTitle("확인창");
						alert.setMessage("아이디와 비밀번호를 확인하세요.");
						alert.setPositiveButton("확인", null);
						alert.show();
						it = new Intent(this, login.class);
						startActivity(it);
						finish();
						//브레이크문을 통해 아래의 코드가 수행되지 않도록 막음
						break;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//여기 까지 수행된다는것은 아이디와 비밀번호가 정상처리된 것으로 고객메뉴Activity로 이동하며 아이디와 비밀번호값을 같이 넘긴다.
				it = new Intent(this, cusmenuActivity.class);
				it.putExtra("it_num", str_id);
				it.putExtra("it_pass", str_pass);
				startActivity(it);
				finish();
				break;

			case R.id.joincus:
				//회원가입버튼이 클릭되면 회원가입Activity로 이동
				it = new Intent(this, MainActivity.class);
				startActivity(it);
				finish();
				break;
		}

	}

}
