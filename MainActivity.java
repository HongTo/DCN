package com.example.dproject1_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	String result = new String("");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {

		EditText editText = (EditText)findViewById(R.id.editText1);
		TextView textView = (TextView)findViewById(R.id.textView1);
		String clean = new String("clean");
		textView.setText(clean);		

		String inputAddress = editText.getText().toString();
		String preAddress = "http://";

		if(inputAddress.substring(0,6)!="http://")
			preAddress += inputAddress;
		else
			preAddress = inputAddress;

		final String address = preAddress;
		final Handler mHandler = new Handler();


		Thread httpThread = new Thread("HTTP_Thread") {
			@Override
			public void run() {
				try {
					URL url = new URL(address);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();

					if(conn != null) {
						conn.setConnectTimeout(10000);
						conn.setReadTimeout(0);
						conn.setUseCaches(false);
						conn.setRequestMethod("GET");

						int resCode = conn.getResponseCode();

						if(resCode == HttpURLConnection.HTTP_OK) {
							InputStreamReader isr = new InputStreamReader(conn.getInputStream());						
							BufferedReader br = new BufferedReader(isr);
							while(true){
								final String line = br.readLine();						
								if(line == null)
									break;

								mHandler.post(new Runnable(){
									public void run() {
										result += (line + "\n");		
									}
								});

							}
							isr.close();
							br.close();
						}
					}
					conn.disconnect();
				}
				catch(Exception e) {
					Log.i("Mymessage","this is my message");
					e.printStackTrace();
				}
			}
		};
		httpThread.start();

		textView.setText(result);
	}

}
