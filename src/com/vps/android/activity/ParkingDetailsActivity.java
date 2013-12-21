package com.vps.android.activity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.ksoap2.serialization.PropertyInfo;

import com.vps.android.model.ServiceModel;
import com.vps.android.worker.ServiceCallback;
import com.vps.android.worker.WebServiceEngine;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ParkingDetailsActivity extends Activity implements OnClickListener, ServiceCallback {
	EditText entryTime=null,token=null,ve=null;
	Button back=null,movie=null,pass=null;
	TextView amount=null,user=null,vType=null,timeSet=null,vt=null;
	String time=null;
	String vehType=null;
	int type=0;
	double vals;
	int paasType=0;
	String vehicType="FourWheeler";
	ServiceModel serviceModel=null;
	long tokenNo=32;
	String tost="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.details);
		vehType=getIntent().getExtras().getString("v");
		type=getIntent().getExtras().getInt("type");
		intialize();
		setListiners();
		Log.i("VPS","finalType "+type);
				
		}
	private void setListiners() {
		entryTime.setOnClickListener(this);
		back.setOnClickListener(this);
		movie.setOnClickListener(this);
		pass.setOnClickListener(this);
		
		
	}
	public void intialize()
	{
		serviceModel=new ServiceModel();
		entryTime=(EditText)findViewById(R.id.entryTime);
		back=(Button)findViewById(R.id.back);
		movie=(Button)findViewById(R.id.movie);
		pass=(Button)findViewById(R.id.pass);
		token=(EditText)findViewById(R.id.token);
		vType=(TextView)findViewById(R.id.vtype);
		user=(TextView)findViewById(R.id.user);
		amount=(TextView)findViewById(R.id.textView3);
		timeSet=(TextView)findViewById(R.id.textView5);
		vt=(TextView)findViewById(R.id.vt);
		ve=(EditText)findViewById(R.id.ve);
		if(type==0)
		{
		token.setVisibility(View.GONE);
		amount.setVisibility(View.GONE);
		if(vehType.equalsIgnoreCase("two"))
				{
			vehicType="TwoWheeler";
			vType.setText("TWO WHEELERS");
				}
		}
		else if(type==1)
		{
			vt.setVisibility(View.GONE);
			ve.setVisibility(View.GONE);
			timeSet.setText("Exit Time");
			vType.setVisibility(View.VISIBLE);
			movie.setVisibility(View.GONE);
			pass.setText("Print");
		}
		}
		
	
	public void onClick(View v) {
	switch (v.getId())
	{
	case R.id.entryTime:
		
		Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ParkingDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            	if(selectedHour<12)
            	{
            	time=selectedHour + ":" + selectedMinute+" AM";	
            	}
            	else
            	{
            	time=selectedHour-12 + ":" + selectedMinute+" PM";	
            	}
                entryTime.setText(time);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
		break;
		
		
	case R.id.back:
		Intent intent=new Intent(ParkingDetailsActivity.this,VehicleActivity.class);
		intent.putExtra("type", type);
		(ParkingDetailsActivity.this).startActivity(intent);
		break;
		
	case R.id.movie:
	servicecall("Movie");
		break;
		
	case R.id.pass:
		if(type==1)
		{
		exitServiceCall(tokenNo);	
		}
		else
		{
			servicecall("Pass");	
		}
		break;
		
		
	}
	}
	
	
	private void exitServiceCall(long i) {
		// TODO Auto-generated method stub
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("TokenNo");
		propertyInfo.setValue(i);
		propertyInfo.setType(PropertyInfo.LONG_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("ExitDateTime");
		propertyInfo.setValue("20-Dec-2013 11:30 PM");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceCode");
		propertyInfo.setValue(1);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("UserCode");
		propertyInfo.setValue(1);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);
		propertyInfo=new PropertyInfo();
		
		propertyInfo.setName("TokenType");
		propertyInfo.setValue("Movie");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleType");
		propertyInfo.setValue("TwoWheeler");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleNo");
		propertyInfo.setValue("12345");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		propertyInfo=new PropertyInfo();
		
		propertyInfo.setName("EntryDateTime");
		propertyInfo.setValue("");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
	
		serviceModel.addProperty(propertyInfo);
		
		
		serviceModel.setServiceOperation("SetExit");
		WebServiceEngine engine=new WebServiceEngine(ParkingDetailsActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();
	}
	private void servicecall(String passValue) {
		// TODO Auto-generated method stub
		PropertyInfo propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleNo");
		propertyInfo.setValue("12345");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		propertyInfo=new PropertyInfo();
		propertyInfo.setName("EntryDateTime");
		propertyInfo.setValue("20-Dec-2013 11:00 PM");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		 propertyInfo=new PropertyInfo();
			propertyInfo.setName("TokenType");
			propertyInfo.setValue(passValue);
			propertyInfo.setType(PropertyInfo.STRING_CLASS);
			serviceModel.addProperty(propertyInfo);
		
		 propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleType");
		propertyInfo.setValue(vehicType);
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		 propertyInfo=new PropertyInfo();
		propertyInfo.setName("DeviceCode");
		propertyInfo.setValue(1);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);

		propertyInfo=new PropertyInfo();
		propertyInfo.setName("UserCode");
		propertyInfo.setValue(1);
		propertyInfo.setType(PropertyInfo.INTEGER_CLASS);
		serviceModel.addProperty(propertyInfo);
		serviceModel.setServiceOperation("SetEntry");
		WebServiceEngine engine=new WebServiceEngine(ParkingDetailsActivity.this);
		engine.setServiceModel(serviceModel);
		engine.start();
	}
	@Override
	public void onBackPressed() {
	
		Intent intent=new Intent(ParkingDetailsActivity.this,VehicleActivity.class);
		intent.putExtra("type", type);
		(ParkingDetailsActivity.this).startActivity(intent);
	}
	@Override
	public void onResult(ServiceModel model) {
		// TODO Auto-generated method stub
		Log.i("VPS","parkingToken :"+model.getServiceOperation());
		Log.i("VPS","parkingToken :"+serviceModel.getResult().toString());
		if(model.getServiceOperation()=="SetEntry")
		{
		System.out.println(serviceModel.getResult().toString());
		tost="The Token Id is "+serviceModel.getResult().toString();
		}
		else if(model.getServiceOperation()=="SetExit")
		{
			System.out.println(serviceModel.getResult().toString());
			tost="The bill has generated with the values "+serviceModel.getResult().toString();
		}
		this.runOnUiThread(show_toast);
		Intent intent=new Intent(ParkingDetailsActivity.this,EntryExitActivity.class);
		intent.putExtra("type", type);
		(ParkingDetailsActivity.this).startActivity(intent);
	}
	
	
	private Runnable show_toast = new Runnable()
	{
	    public void run()
	    {
	        Toast.makeText(ParkingDetailsActivity.this,tost, Toast.LENGTH_SHORT)
	                    .show();
	    }
	};
	

}
