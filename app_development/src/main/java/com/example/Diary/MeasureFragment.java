package com.example.fooddiary;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MeasureFragment extends Fragment {
    private EditText edit;
    private TextView kcal;


    private XmlPullParser xpp;
    private static final String key = "a9411a84ed604f16b7ad"; //

    public MeasureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measure, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit = view.findViewById(R.id.food_id);
        kcal = view.findViewById(R.id.kcal);

        Button button = view.findViewById(R.id.food_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String data = getXmlData();
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    kcal.setText(data);
                                }
                            });
                        }
                    } }).start();
            }
        });
    }

    private String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String location = URLEncoder.encode(str);

        String queryUrl = "http://openapi.foodsafetykorea.go.kr/api/a9411a84ed604f16b7ad/I2790/xml/1/1"
                + "?DESC_KOR=" + location;

        try {
            URL url = new URL(queryUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is = connection.getInputStream();

            xpp = XmlPullParserFactory.newInstance().newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            xpp.next();
            int eventType = xpp.getEventType();
            boolean fSet = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();
                        if (tag != null && tag.equals("row id=\"0\"")) {
                            fSet = true;// 첫번째 검색결과
                        }
                    case XmlPullParser.TEXT:
                        tag = xpp.getName();
                        if (tag != null && tag.equals("NUTR_CONT1")) {
                            xpp.next();
                            String kcalValue = xpp.getText();
                            buffer.append("칼로리: ").append(kcalValue).append("kcal").append("\n");
                        } else if (tag != null && tag.equals("NUTR_CONT2")) {
                            xpp.next();
                            String carbohydrateValue = xpp.getText();
                            buffer.append("탄수화물: ").append(carbohydrateValue).append("g").append("\n");
                        } else if (tag != null &&tag.equals("NUTR_CONT3")) {
                            xpp.next();
                            String proteinValue = xpp.getText();
                            buffer.append("단백질: ").append(proteinValue).append("g").append("\n");
                        } else if (tag != null &&tag.equals("NUTR_CONT4")) {
                            xpp.next();
                            String fatValue = xpp.getText();
                            buffer.append("지방: ").append(fatValue).append("g").append("\n");
                        } else if (tag != null &&tag.equals("NUTR_CONT6")) {
                            xpp.next();
                            String sodiumValue = xpp.getText();
                            buffer.append("나트륨: ").append(sodiumValue).append("mg").append("\n");
                        }else if (tag != null && tag.equals("SERVING_SIZE")) {
                            xpp.next();
                            String sizeValue = xpp.getText();
                            buffer.append("1회량: ").append(sizeValue).append("g").append("\n");
                        }
                        fSet = false;
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("row"))
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //buffer.append("파싱 끝\n");
        return buffer.toString();
    }
}
