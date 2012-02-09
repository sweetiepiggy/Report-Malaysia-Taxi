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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReportMalaysiaTaxiActivity extends Activity {
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String mOffence;
	private ArrayList<String> mPhotoUris = new ArrayList<String>();
	private ArrayList<String> mRecordingUris = new ArrayList<String>();

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	static final int ACTIVITY_TAKE_PHOTO = 0;
	static final int ACTIVITY_RECORD_SOUND = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button date_button = (Button)findViewById(R.id.date_button);
		date_button.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			showDialog(DATE_DIALOG_ID);
		}
	});
	Button time_button = (Button)findViewById(R.id.time_button);
	time_button.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			showDialog(TIME_DIALOG_ID);
		}
	});

	final Calendar c = Calendar.getInstance();
	mYear = c.get(Calendar.YEAR);
	mMonth = c.get(Calendar.MONTH);
	mDay = c.get(Calendar.DAY_OF_MONTH);
	mHour = c.get(Calendar.HOUR_OF_DAY);
	mMinute = c.get(Calendar.MINUTE);
	update_date_label(mYear, mMonth, mDay);
	update_time_label(mHour, mMinute);

	Spinner offence_spinner = (Spinner) findViewById(R.id.offence_spinner);
	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.offence_array, android.R.layout.simple_spinner_item);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	offence_spinner.setAdapter(adapter);
	offence_spinner.setOnItemSelectedListener(new OffenceOnItemSelectedListener());

	Button camera_button = (Button)findViewById(R.id.camera_button);
	camera_button.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent photo_intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(photo_intent, ACTIVITY_TAKE_PHOTO);
		}
	});
	Button recorder_button = (Button)findViewById(R.id.recorder_button);
	recorder_button.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent recorder_intent = new Intent (MediaStore.Audio.Media.RECORD_SOUND_ACTION);
			startActivityForResult(recorder_intent, ACTIVITY_RECORD_SOUND);
		}
	});

	((CheckBox)findViewById(R.id.sms_checkbox)).setChecked(true);
	((CheckBox)findViewById(R.id.email_checkbox)).setChecked(true);
	((CheckBox)findViewById(R.id.tweet_checkbox)).setChecked(true);

	Button submit_button = (Button)findViewById(R.id.submit_button);

	submit_button.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			TextView date_label = (TextView)findViewById(R.id.date_label);
			TextView time_label = (TextView)findViewById(R.id.time_label);
			EditText location_entry = (EditText)findViewById(R.id.location_entry);
			EditText registration_entry = (EditText)findViewById(R.id.registration_entry);
			EditText other_entry = (EditText)findViewById(R.id.other_entry);

			boolean results_complete = true;
			String incomplete_msg = "";

			if (registration_entry.getText().toString().length() == 0) {
				results_complete = false;
				incomplete_msg = "Enter Registration Number";
			} else if (mOffence.equals("Other") && other_entry.getText().toString().length() == 0) {
				results_complete = false;
				incomplete_msg = "Enter Other Comments";
			}

			if (results_complete) {
				String date = date_label.getText().toString();
				String time = time_label.getText().toString();
				String loc = location_entry.getText().toString();
				String reg = registration_entry.getText().toString();
				String other = other_entry.getText().toString();

				String msg = format_msg(date, time, loc, reg, mOffence, other);

				CheckBox sms_checkbox = ((CheckBox)findViewById(R.id.sms_checkbox));
				CheckBox email_checkbox = ((CheckBox)findViewById(R.id.email_checkbox));
				CheckBox tweet_checkbox = ((CheckBox)findViewById(R.id.tweet_checkbox));
				CheckBox other_checkbox = ((CheckBox)findViewById(R.id.other_checkbox));

				if (!sms_checkbox.isChecked() && !email_checkbox.isChecked() &&
						!tweet_checkbox.isChecked() && !other_checkbox.isChecked()) {
					Toast.makeText(getApplicationContext(), "Select a Checkbox",
							Toast.LENGTH_SHORT).show();
				}

				if (other_checkbox.isChecked()) {
					String action = (mPhotoUris.size() + mRecordingUris.size() > 1) ?
							Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;
					Intent other_intent = new Intent(action);
					other_intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.complaint_malay));
					other_intent.putExtra(Intent.EXTRA_TEXT, msg);

					ArrayList<Uri> uris = new ArrayList<Uri>();
					Iterator<String> itr = mPhotoUris.iterator();
					while (itr.hasNext()) {
						uris.add(Uri.parse(itr.next()));
					}
					itr = mRecordingUris.iterator();
					while (itr.hasNext()) {
						uris.add(Uri.parse(itr.next()));
					}
					if (uris.size() != 0) {
						other_intent.putExtra(Intent.EXTRA_STREAM, uris);
					}
					other_intent.setType("text/plain");
					startActivity(Intent.createChooser(other_intent, "Send other"));
				}

				if (tweet_checkbox.isChecked()) {
					String tweet_msg = format_tweet_msg(msg);
					Intent tweet_intent = new Intent(Intent.ACTION_SEND);
					tweet_intent.putExtra(Intent.EXTRA_TEXT, tweet_msg);
					if (!mPhotoUris.isEmpty()) {
						tweet_intent.putExtra(Intent.EXTRA_STREAM,
								Uri.parse(mPhotoUris.get(mPhotoUris.size()-1)));
					}
					tweet_intent.setType("text/plain");
					startActivity(Intent.createChooser(tweet_intent, "Send tweet"));
				}
				if (email_checkbox.isChecked()) {
					String email_msg = format_email_msg(msg);
					String action = (mPhotoUris.size() + mRecordingUris.size() > 1) ?
							Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;
					Intent email_intent = new Intent(action);

					String email_addresses = getResources().getString(R.string.email_address);
					if (mOffence.equals("Gangguan") ||
						mOffence.equals("Menawarkan perkhidmatan yang menyalahi undang-undang")) {
						email_addresses += getResources().getString(R.string.traffic_police_email_address);
					}

					email_intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
							email_addresses} );
					email_intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.complaint_email_malay));
					email_intent.putExtra(Intent.EXTRA_TEXT, email_msg);

					ArrayList<Uri> uris = new ArrayList<Uri>();
					Iterator<String> itr = mPhotoUris.iterator();
					while (itr.hasNext()) {
						uris.add(Uri.parse(itr.next()));
					}
					itr = mRecordingUris.iterator();
					while (itr.hasNext()) {
						uris.add(Uri.parse(itr.next()));
					}
					if (uris.size() != 0) {
						email_intent.putExtra(Intent.EXTRA_STREAM, uris);
					}
					email_intent.setType("text/plain");
					startActivity(Intent.createChooser(email_intent, "Send email"));
				}
				if (sms_checkbox.isChecked()) {
					String sms_msg = format_sms_msg(msg);
					Intent sms_intent = new Intent(Intent.ACTION_VIEW);

					sms_intent.putExtra("address",
							getResources().getString(R.string.sms_number));
					sms_intent.putExtra("sms_body", sms_msg);
					sms_intent.setType("vnd.android-dir/mms-sms");
					startActivity(sms_intent);
				}

			} else {
				Toast.makeText(getApplicationContext(), incomplete_msg,
						Toast.LENGTH_SHORT).show();
			}
		}
	});

	Button cancel_button = (Button)findViewById(R.id.cancel_button);

	cancel_button.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);
			update_date_label(mYear, mMonth, mDay);
			update_time_label(mHour, mMinute);

			mPhotoUris.clear();
			mRecordingUris.clear();

			((EditText)findViewById(R.id.location_entry)).setText("");
			((EditText)findViewById(R.id.registration_entry)).setText("");
			((EditText)findViewById(R.id.other_entry)).setText("");
			((CheckBox)findViewById(R.id.sms_checkbox)).setChecked(true);
			((CheckBox)findViewById(R.id.email_checkbox)).setChecked(true);
			((CheckBox)findViewById(R.id.tweet_checkbox)).setChecked(true);
			((CheckBox)findViewById(R.id.other_checkbox)).setChecked(false);
			((TextView)findViewById(R.id.camera_label)).setText("");
			((TextView)findViewById(R.id.recorder_label)).setText("");
		}
	});


	Button call_button = (Button)findViewById(R.id.call_button);
	call_button.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			String[] items = getResources().getStringArray(R.array.tel_number_names);

			AlertDialog.Builder builder = new AlertDialog.Builder(ReportMalaysiaTaxiActivity.this);
			builder.setTitle("Place Call");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					String tel_number = getResources().getStringArray(R.array.tel_numbers)[item];
					Intent call_intent = new Intent(Intent.ACTION_DIAL);
					call_intent.setData(Uri.parse("tel:" + tel_number));
					startActivity(call_intent);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			}
		});

	}

	private String format_msg(String date, String time, String location,
			String registration, String offence, String other) {
		String message = "";
		if (date.length() != 0) {
			message += '\n' + date;
			if (time.length() != 0) {
				message += ' ' + time;
			}
		}

		if (registration.length() != 0) {
				message += '\n' + getResources().getString(R.string.registration_malay) + ": " + registration;
		}
		if (location.length() != 0) {
				message += '\n' + getResources().getString(R.string.location_malay) + ": " + location;
		}
		if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + getResources().getString(R.string.offence_malay) + ": " + offence;
		}
		if (other.length() != 0) {
			if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + other;
			} else {
				message += '\n' + getResources().getString(R.string.offence_malay) + ": " + other;
			}
		}
		return message;
	}

	private String format_email_msg(String msg) {
		return getResources().getString(R.string.email_intro_malay) + "\n" + msg;
	}

	private String format_sms_msg(String msg) {
		return getResources().getString(R.string.complaint_malay) + msg;
	}

	private String format_tweet_msg(String msg) {
		return getResources().getString(R.string.twitter_address) + " " +
			getResources().getString(R.string.complaint_hashtag) + msg;
	}

	private void update_date_label(int year, int month, int day) {
		TextView date_label = (TextView)findViewById(R.id.date_label);
		date_label.setText(new StringBuilder()
				.append(String.format("%02d", day))
				.append('/')
				.append(String.format("%02d", month+1))
				.append('/')
				.append(year)
		);
	}

	private void update_time_label(int hour, int minute) {
		TextView time_label = (TextView)findViewById(R.id.time_label);
		String am_pm = hour > 11 ? "PM" : "AM";
		if (hour > 12) {
			hour -= 12;
		}
		if (hour == 0) {
			hour = 12;
		}
		time_label.setText(new StringBuilder()
				.append(hour)
				.append(':')
				.append(String.format("%02d", minute))
				.append(am_pm)
		);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mYear = year;
					mMonth = monthOfYear;
					mDay = dayOfMonth;
					update_date_label(mYear, mMonth, mDay);
				}
		};

		TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker view,
						int hourOfDay, int minute) {
					mHour = hourOfDay;
					mMinute = minute;
					update_time_label(mHour, mMinute);
				}
		};

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear,
					mMonth, mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour,
					mMinute, false);
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != -1) {
			return;
		}

		switch (requestCode) {
		case ACTIVITY_TAKE_PHOTO:
			mPhotoUris.add(data.getDataString());
			((TextView)findViewById(R.id.camera_label)).setText(Integer.toString(mPhotoUris.size()));
			break;
		case ACTIVITY_RECORD_SOUND:
			mRecordingUris.add(data.getDataString());
			((TextView)findViewById(R.id.recorder_label)).setText(Integer.toString(mRecordingUris.size()));
			break;
		}
	}

	public class OffenceOnItemSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			mOffence = getResources().getStringArray(R.array.offence_malay_array)[pos];
		}

		public void onNothingSelected(AdapterView<?> parent) {
		/* do nothing */
		}
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings:
	//                newGame();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
