package com.vps.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EntryExitActivity extends Activity implements OnClickListener{
	ImageView btnEntry=null;
	ImageView btnExit=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entryexit);
		 btnEntry=(ImageView)findViewById(R.id.btnEntry);
		 btnExit=(ImageView)findViewById(R.id.btnExit);
		 TextView txtUserName= (TextView) findViewById(R.id.txtWelcomeUserName);
		 String userName=getSharedPreferences("com.vps.android.activity", Context.MODE_PRIVATE).getString("UserName","null");
		 Log.i("VPS","Entry Exit "+userName);
		 txtUserName.setText("Welcome, "+userName);
		 setListiners();
	
	}
	private void setListiners() {
		// TODO Auto-generated method stub
		btnEntry.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub	btnEntry.setOnClickListener(new View.OnClickListener() {
		
	
			int entry=0;
			if(v.getId()==R.id.btnExit)
			{
				entry=1;	
			}
						
			
			Intent intent=new Intent(EntryExitActivity.this,VehicleActivity.class);
			intent.putExtra("type", entry);
			(EntryExitActivity.this).startActivity(intent);
					
	}
	@Override
	public void onBackPressed() {
	    // your code.
		Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
		finish();
	}
}
