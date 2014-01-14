package com.example.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button b = (Button)findViewById(R.id.hello);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				helloClick();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void helloClick() {
		try {
			ServerSocket svr = new ServerSocket(6666);
			Socket sock = svr.accept();
			
			PrintWriter pw = new PrintWriter(sock.getOutputStream());
			pw.println("Hello");
			pw.flush();
			
			sock.close();
			svr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
