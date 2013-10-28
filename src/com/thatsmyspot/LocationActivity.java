package com.thatsmyspot;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
//--------------------------------------------------------------------------------------------
		//change the AssetManager to the Scanner line
		try {
			AssetManager as = this.getAssets();
			InputStream is = as.open("buildings.txt");
			Scanner scan = new Scanner(is);
			
			RelativeLayout rl = (RelativeLayout) findViewById(R.id.locationlayout);
			RelativeLayout.LayoutParams rlpT = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlpT.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			
			TextView tv = new TextView(this);
			tv.setText("Please choose a location below:");
			if(getIntent().hasExtra("location")) {
				tv.setText("Previous location was: " + getIntent().getStringExtra("location"));
			}
			tv.setId(2000);
			tv.setLayoutParams(rlpT);
			rl.addView(tv);
			
			int i=0;
			//Change this scan.hasNext() while loop, you only need to modify the 
			//button.setText() method. This needs to be done for all locations individually
		    while(scan.hasNext()) {
		        Button button = new Button(this);
		        String[] str = scan.nextLine().split("\t");
		        final String name = str[0];
		        String avail = str[1];
		        button.setText(name);
		        button.setId(1000 + i);
		        button.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						//calling time choosing activity and passing location
						Intent intent;
						//Check to see if coming from an already created meeting group
						if(getIntent().hasExtra("changeRequest")) {
							intent = new Intent(LocationActivity.this, ChangeActivity.class);
							if(name.equals(getIntent().getStringExtra("location"))) {
								intent.putExtra("location", name); //Adds location chosen only if different
								intent.putExtra("changedEvent", true); //Adds a boolean value of event changed
							}
							intent.putExtra("groupName", getIntent().getStringExtra("groupName")); //Adds the groupname
						}
						else {
							intent = new Intent(LocationActivity.this, TimeActivity.class);
							intent.putExtra("location", name); //adds the location name
							if(getIntent().hasExtra("groupName"))
								intent.putExtras(getIntent()); //adds the groupname
							intent.putExtra("newEvent", true);
						}
						startActivity(intent);
					}
				});
		        
		        //creating progress bar
		        ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		        pb.setMax(Integer.parseInt(avail));
		        Random r = new Random();
		        pb.setProgress(r.nextInt(pb.getMax()));
		        pb.getProgressDrawable().setBounds(new Rect(0, 0, 100, 80));

		        if (i == 0) {
		            RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
		                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		            //setting first button below text view
		            rlpB.addRule(RelativeLayout.BELOW, tv.getId());
		            button.setLayoutParams(rlpB);
		            //adding progress bar to button
		            button.setCompoundDrawables(null, null, pb.getProgressDrawable(), null);
		            rl.addView(button);
		        } else {
		            RelativeLayout.LayoutParams rlpB = new RelativeLayout.LayoutParams(
		                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		            //setting buttons to come one after another
		            rlpB.addRule(RelativeLayout.BELOW, button.getId() - 1);
		            button.setLayoutParams(rlpB);
		            //adding progress bar to button
		            button.setCompoundDrawables(null, null, pb.getProgressDrawable(), null);
		            rl.addView(button);
		        }
		        i++;
		    }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

}
