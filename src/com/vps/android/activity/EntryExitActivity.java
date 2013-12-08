package com.vps.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class EntryExitActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entryexit);
		ImageView btnEntry=(ImageView)findViewById(R.id.btnEntry);
		btnEntry.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(EntryExitActivity.this,VehicleActivity.class);
				(EntryExitActivity.this).startActivity(intent);
				
			}
		});
	}
}
