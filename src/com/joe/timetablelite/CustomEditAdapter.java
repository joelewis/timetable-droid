package com.joe.timetablelite;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

 
@SuppressLint("ShowToast")
public class CustomEditAdapter extends ArrayAdapter<String> {
	Context contextt;
	TextView tv1;
	int positionn;
	View convertVieww;
	ViewGroup parentt;
	List values;
	//List<String> values;
	
	public CustomEditAdapter(Context context, int resource,
			int textViewResourceId, List objects) {
		super(context, resource, textViewResourceId, objects);
		values = objects;
		contextt = context;
		// TODO Auto-generated constructor stub
		//values = objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		positionn = position;
		convertVieww = convertView;
		parentt= parent;
		View v = super.getView(position, convertView, parent);
		LinearLayout ll = (LinearLayout) v;
		TextView tv = (TextView) v.findViewById(R.id.periodNo);
		tv.setText((position+1)+"");
		TextView tv2 = (TextView) v.findViewById(R.id.subject);
		if(values.get(position) == "nil") {
			tv2.setText("+++");
		}
		
		//tv1=tv2;
		//tv2.setOnClickListener(this);
		return ll;
	}
	
	
	
	
}
