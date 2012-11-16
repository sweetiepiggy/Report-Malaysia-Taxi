/*
    Copyright (C) 2012 Sweetie Piggy Apps <sweetiepiggyapps@gmail.com>

    This file is part of Report Malaysia Taxi.

    Report Malaysia Taxi is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    Report Malaysia Taxi is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Report Malaysia Taxi; if not, see <http://www.gnu.org/licenses/>.
*/

package com.sweetiepiggy.reportmalaysiataxi;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AllFareCalcActivity extends Activity {

	/* TODO: define starting fare in a Constants class */
	private int m_starting_fare = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fare_calc);

		Bundle b = getIntent().getExtras();
		if (b != null && b.containsKey("starting_fare")) {
			m_starting_fare = b.getInt("starting_fare");
		}

		EditText distance_entry = (EditText)findViewById(R.id.distance_entry);
		distance_entry.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				update_fare();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		EditText time_entry = (EditText)findViewById(R.id.time_entry);
		time_entry.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				update_fare();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		EditText passengers_entry = (EditText)findViewById(R.id.passengers_entry);
		passengers_entry.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				update_fare();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});

		CheckBox midnight_checkbox = (CheckBox)findViewById(R.id.midnight_checkbox);
		midnight_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				update_fare();
			}
		});

		CheckBox executive_checkbox = (CheckBox)findViewById(R.id.executive_checkbox);
		executive_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				update_fare();
			}
		});

		CheckBox telbooking_checkbox = (CheckBox)findViewById(R.id.telbooking_checkbox);
		telbooking_checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				update_fare();
			}
		});

		update_fare();
	}

	private void update_fare()
	{
		String distance_str = ((EditText) findViewById(R.id.distance_entry)).getText().toString();
		if (distance_str.length() == 0) {
			distance_str = "0";
		}
		double km = Double.valueOf(distance_str);

		String time_str = ((EditText) findViewById(R.id.time_entry)).getText().toString();
		if (time_str.length() == 0) {
			time_str = "0";
		}
		int min = Integer.parseInt(time_str);

		String passengers_str = ((EditText) findViewById(R.id.passengers_entry)).getText().toString();
		if (passengers_str.length() == 0) {
			passengers_str = "1";
		}
		int passengers = Integer.parseInt(passengers_str);

		boolean midnight = ((CheckBox) findViewById(R.id.midnight_checkbox)).isChecked();
		boolean exec = ((CheckBox) findViewById(R.id.executive_checkbox)).isChecked();
		boolean telbooking = ((CheckBox) findViewById(R.id.telbooking_checkbox)).isChecked();

		double fare_by_dist = calc_fare_by_dist(m_starting_fare, km,
				passengers, midnight, exec, telbooking);
		double fare_by_time = calc_fare_by_time(m_starting_fare, min,
				passengers, midnight, exec, telbooking);

		double fare = fare_by_dist > fare_by_time ? fare_by_dist : fare_by_time;

		EditText calc_fare_entry = (EditText)findViewById(R.id.calc_fare_entry);
		calc_fare_entry.setText(String.format("%.2f", fare));
	}

	private double calc_fare_by_dist(int starting_fare, double km,
			int passengers, boolean midnight,
			boolean exec, boolean telbooking)
	{
		double fare = starting_fare;
		km = km > 1 ? km - 1 : 0;
		fare += ((int)(km / 0.115)) * 0.1;
		if (midnight) {
			fare *= 1.5;
		}
		if (exec) {
			fare *= 2;
		}
		if (telbooking) {
			fare += 2;
		}
		if (passengers > 2) {
			fare += 1;
		}
		return fare;
	}

	private double calc_fare_by_time(int starting_fare, int min,
			int passengers, boolean midnight,
			boolean exec, boolean telbooking)
	{
		double fare = starting_fare;
		min = min > 3 ? min - 3 : 0;
		fare += ((int)(min * 60 / 21. )) * 0.1;
		if (midnight) {
			fare *= 1.5;
		}
		if (exec) {
			fare *= 2;
		}
		if (telbooking) {
			fare += 2;
		}
		if (passengers > 2) {
			fare += 1;
		}
		return fare;
	}

}

