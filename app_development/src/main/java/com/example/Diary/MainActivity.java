package com.example.Diary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.Diary.R;
/**
 * 회원가입 Activity
 *
 * @author 이민형
 */
public class MainActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// layout설정
		setContentView(R.layout.activity_main);

		Button btn = (Button) findViewById(R.id.join_btn);
		btn.setOnClickListener(this);
	}

	// 뒤로가기 버튼 작동시
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Intent intent = new Intent(this, login.class);

				startActivity(intent);
				finish();
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public void onClick(View v) {
		//각 입력폼에서 값 추출하여 변수에 저장
		Intent it = new Intent();
		// 성명 추출
		EditText et_name = (EditText)findViewById(R.id.cus_id);
		String ucus_id = et_name.getText().toString();
		String cus_id="";
		//한글처리위한 UTF-8로 변환처리
		try {
			cus_id=URLEncoder.encode(ucus_id, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		// 비밀번호  추출
		EditText et_name01 = (EditText)findViewById(R.id.cus_pw);
		String cus_pw = et_name01.getText().toString();
		EditText et_name05 = (EditText)findViewById(R.id.cus_name);
		String utf_name = et_name05.getText().toString();
		String cus_name="";
		//한글처리위한 UTF-8로 변환처리
		try {
			cus_name=URLEncoder.encode(utf_name, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		EditText et_name02 = (EditText)findViewById(R.id.cus_addr);
		String ucus_addr = et_name02.getText().toString();

		String cus_addr="";
		//한글처리위한 UTF-8로 변환처리
		try {
			cus_addr=URLEncoder.encode(ucus_addr, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		EditText et_name52 = (EditText)findViewById(R.id.cus_tel);
		String str_tel = et_name52.getText().toString();
		EditText et_name53 = (EditText)findViewById(R.id.cus_email);
		String str_email="";
		str_email = et_name53.getText().toString();
		try {
			str_email=URLEncoder.encode(str_email, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//체크박스에서 checked된 값 추출
		CheckBox   chk_ag = (CheckBox)findViewById(R.id.checkBox1);
		String str_ag = "";
		if (chk_ag.isChecked()) {
			str_ag = "동의";
		}else{
			str_ag = "거부";
		}

		try {
			str_ag=URLEncoder.encode(str_ag, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		//유효성검사 입력안한 값이 있는지
		if(cus_id==""){
			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			alert.setTitle("확인창");
			alert.setMessage("아이디를 입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}else if(cus_pw==""){
			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			//중복된 아이디창을 띄운디 회원가입Activity를 다시 실행시킨다.
			alert.setTitle("확인창");
			alert.setMessage("비밀번호를 입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}else if(cus_name==""){
			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			//중복된 아이디창을 띄운디 회원가입Activity를 다시 실행시킨다.
			alert.setTitle("확인창");
			alert.setMessage("이름을  입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}else if(cus_addr==""){

			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			//중복된 아이디창을 띄운디 회원가입Activity를 다시 실행시킨다.
			alert.setTitle("확인창");
			alert.setMessage("주소를 입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}else if(str_tel==""){

			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			alert.setTitle("확인창");
			alert.setMessage("전화번호를 입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}else if(str_email.equals("")){
			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			alert.setTitle("확인창");
			alert.setMessage("이메일을 입력해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;

		}else if(str_ag==""){
			AlertDialog.Builder alert= new AlertDialog.Builder(this);
			alert.setTitle("확인창");
			alert.setMessage("수신여부를 확인해주세요.");
			alert.setPositiveButton("확인",null);
			alert.show();
			return;
		}
		switch(v.getId())
		{
			//가입버튼 클릭시


			case R.id.join_btn:
				//먼저 기존에 존재하는 아이디인지 확인하는 php 구문으로 입력한 ID값을 보낸다
				try {
					URL url=new URL("http://211.109.171.121/check_rid.php?u_id="+cus_id);
					URLConnection con=url.openConnection();

					BufferedReader br=
							new BufferedReader(new InputStreamReader(con.getInputStream()));
					String temp="";
					//입력한 아이디로 아이디가 있는지 조회한뒤 결과 값이 있다면 이미 존재하는 아이디로
					if( (temp = br.readLine()) != null){
						AlertDialog.Builder alert= new AlertDialog.Builder(this);
						//중복된 아이디창을 띄운디 회원가입Activity를 다시 실행시킨다.
						alert.setTitle("확인창");
						alert.setMessage("아이디가 중복됩니다. 다시입력하세요.");
						alert.setPositiveButton("확인",null);
						alert.show();
						it=new Intent(this,joinActivity.class);
						startActivity(it);
						finish();
						//break문을 통해 아래의 코드가 실행되지 않도록한다.
						break;
					}else{
						//중복된 아이디가 없을시에는 정상 가입가능
						AlertDialog.Builder alert= new AlertDialog.Builder(this);
						alert.setTitle("확인창");
						alert.setMessage("가입성공");
						alert.setPositiveButton("확인",null);
						alert.show();
						//추출한 변수들을 파라메터로하여 php문에 전달하여 insert문 처리하여 회원가입
						try {
							URL urla=new URL("http://211.109.171.121/insert_cusjoin.php?u_id="+cus_id+"&u_pw="+cus_pw+"&u_name="+cus_name+"&u_addr="+cus_addr+"&u_ctel="+str_tel+"&u_cemail="+str_email+"&u_cag="+str_ag);
							urla.openStream();
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				//로그인 페이지로 이동
				it=new Intent(this,login.class);
				startActivity(it);
				finish();
				break;


		}

	}}