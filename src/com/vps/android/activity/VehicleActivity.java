package com.vps.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class VehicleActivity extends Activity implements OnClickListener{

ImageView twoWheeler=null;
ImageView fourWheeler=null;

ImageView imgLogout=null;
ImageView imgHome=null;
int type=0;
String vehicle="two";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		type=getIntent().getExtras().getInt("type");
		setContentView(R.layout.vehicle);
		TextView txtUserName= (TextView) findViewById(R.id.txtWelcomeUserName);
		
		imgLogout=(ImageView)findViewById(R.id.imgLogout);
		imgHome=(ImageView)findViewById(R.id.imgHome);
		
		txtUserName.setText("Welcome, "+getSharedPreferences("com.vps.android.activity", Context.MODE_PRIVATE).getString("UserName","null")+" ");
		intialize();
		setListiners();
		Log.i("VPS","eType "+type);
		
		imgHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(VehicleActivity.this,EntryExitActivity.class);
				(VehicleActivity.this).startActivity(intent);
				
			}
		});
		
		
		imgLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
				Intent intent=new Intent(VehicleActivity.this,LoginActivity.class);
				(VehicleActivity.this).startActivity(intent);
				
			}
		});
	}
	private void setListiners() {
		// TODO Auto-generated method stub
		twoWheeler.setOnClickListener(this);
		fourWheeler.setOnClickListener(this);
		
	}
	private void intialize() {
	
		twoWheeler=(ImageView)findViewById(R.id.two);
		fourWheeler=(ImageView)findViewById(R.id.four);
	}
	@Override
	public void onClick(View arg0) {
		
		// TODO Auto-generated method stub
		if(arg0.getId()==R.id.four)
		{
			vehicle="four";	
		}
		finish();
		Intent intent=new Intent(VehicleActivity.this,ParkingDetailsActivity.class);
		intent.putExtra("v", vehicle);
		intent.putExtra("type", type);
		(VehicleActivity.this).startActivity(intent);
		
	}
	@Override
	public void onBackPressed() {
	    
	}


}
