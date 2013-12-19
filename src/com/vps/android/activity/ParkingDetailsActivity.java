package com.vps.android.activity;
import java.util.Calendar;

import org.ksoap2.serialization.PropertyInfo;

import com.vps.android.model.ServiceModel;
import com.vps.android.worker.ServiceCallback;
import com.vps.android.worker.WebServiceEngine;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ParkingDetailsActivity extends Activity implements OnClickListener, ServiceCallback {
	EditText entryTime=null,amountRs=null;
	Button back=null,movie=null,pass=null;
	TextView amount=null,user=null,vType=null;
	String time=null;
	String vehType=null;
	int type=0;
	ServiceModel serviceModel=null;
	
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
		amount=(TextView)findViewById(R.id.textamount);
		amountRs=(EditText)findViewById(R.id.editamount);
		vType=(TextView)findViewById(R.id.vtype);
		user=(TextView)findViewById(R.id.user);
		
		if(type==0)
		{
		amount.setVisibility(View.GONE);
		amountRs.setVisibility(View.GONE);
		if(vehType.equalsIgnoreCase("two"))
				{
			vType.setText("TWO WHEELERS");
				}
		}
		else if(type==1)
		{
			vType.setVisibility(View.INVISIBLE);
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
			
		}
		else
		{
			servicecall("Pass");	
		}
		break;
		
		
	}
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
		propertyInfo.setValue("DateTime");
		propertyInfo.setType(PropertyInfo.STRING_CLASS);
		serviceModel.addProperty(propertyInfo);
		
		 propertyInfo=new PropertyInfo();
		propertyInfo.setName("VehicleType");
		propertyInfo.setValue(passValue);
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
	    // your code.
		Intent intent=new Intent(ParkingDetailsActivity.this,VehicleActivity.class);
		intent.putExtra("type", type);
		(ParkingDetailsActivity.this).startActivity(intent);
	}
	@Override
	public void onResult(ServiceModel model) {
		// TODO Auto-generated method stub
		Log.i("VPS","parkingToken :"+model.getServiceOperation());
		Log.i("VPS","parkingToken :"+serviceModel.getResult().toString());
	}

}
