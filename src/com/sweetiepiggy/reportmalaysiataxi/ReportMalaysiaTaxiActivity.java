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
import java.util.Date;
import java.util.Iterator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReportMalaysiaTaxiActivity extends Activity
{
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private String mOffence;
	private String mOffenceMalay;

	private boolean[] mSelected;

	private ArrayList<Uri> mPhotoUris = new ArrayList<Uri>();
	private ArrayList<Uri> mRecordingUris = new ArrayList<Uri>();

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	static final int ACTIVITY_TAKE_PHOTO = 0;
	static final int ACTIVITY_RECORD_SOUND = 1;
	static final int ACTIVITY_UPDATE_SETTINGS = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();
	}

	private void init()
	{
		init_date_time_buttons();
		init_reg_entry();
		init_offence_spinner();
		init_camera_recorder_buttons();
		init_submit_button();
		init_cancel_button();
		init_call_button();
		init_vars();
		init_entries();
		update_date_label(mYear, mMonth, mDay);
		update_time_label(mHour, mMinute);
	}

	private void init_date_time_buttons()
	{
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
	}

	private void init_reg_entry()
	{
		EditText reg_entry = (EditText)findViewById(R.id.reg_entry);

		/* prevent keyboard from popping up when app starts */
		reg_entry.setInputType(InputType.TYPE_NULL);

		/* enable keyboard when entry is touched */
		reg_entry.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				EditText reg_entry = (EditText)findViewById(R.id.reg_entry);
				reg_entry.setInputType(InputType.TYPE_CLASS_TEXT);
				reg_entry.onTouchEvent(event);
				return true;
			}
		});
	}

	private void init_offence_spinner()
	{
		Spinner offence_spinner = (Spinner) findViewById(R.id.offence_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			this, R.array.offence_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		offence_spinner.setAdapter(adapter);
		offence_spinner.setOnItemSelectedListener(new OffenceOnItemSelectedListener());
	}

	/* TODO: disable recorder buttons if not supported by device */
	private void init_camera_recorder_buttons()
	{
		Button camera_button = (Button) findViewById(R.id.camera_button);
		camera_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent photo_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(photo_intent, ACTIVITY_TAKE_PHOTO);
			}
		});

		Button recorder_button = (Button) findViewById(R.id.recorder_button);
		recorder_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent recorder_intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
				startActivityForResult(recorder_intent, ACTIVITY_RECORD_SOUND);
			}
		});
	}

	private void init_entries()
	{
		((CheckBox) findViewById(R.id.sms_checkbox)).setChecked(true);
		((CheckBox) findViewById(R.id.email_checkbox)).setChecked(true);
		((CheckBox) findViewById(R.id.tweet_checkbox)).setChecked(true);
		((CheckBox) findViewById(R.id.other_checkbox)).setChecked(false);

		((EditText)findViewById(R.id.location_entry)).setText("");
		((EditText)findViewById(R.id.reg_entry)).setText("");
		((EditText)findViewById(R.id.other_entry)).setText("");
		((TextView)findViewById(R.id.camera_label)).setText("");
		((TextView)findViewById(R.id.recorder_label)).setText("");
	}

	private void init_submit_button()
	{
		Button submit_button = (Button) findViewById(R.id.submit_button);
		submit_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean results_complete = true;
				String incomplete_msg = "";

				if (((EditText) findViewById(R.id.reg_entry)).getText().toString().length() == 0) {
					results_complete = false;
					incomplete_msg = getResources().getString(R.string.missing_reg);

				/* TODO: don't hardcode "Other" */
				} else if (mOffenceMalay.equals("Other") &&
						((EditText) findViewById(R.id.other_entry)).getText().toString().length() == 0) {
					results_complete = false;
					incomplete_msg = getResources().getString(R.string.missing_other);
				}

				if (results_complete) {
					submit();
				} else {
					Toast.makeText(getApplicationContext(), incomplete_msg,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void init_cancel_button()
	{
		Button cancel_button = (Button)findViewById(R.id.cancel_button);
		cancel_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				init_date_time_buttons();
				init_vars();
				init_entries();
				update_date_label(mYear, mMonth, mDay);
				update_time_label(mHour, mMinute);
			}
		});
	}

	private void init_call_button()
	{
		Button call_button = (Button)findViewById(R.id.call_button);
		call_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String[] tel_number_names = getResources().getStringArray(R.array.tel_number_names);

				AlertDialog.Builder builder = new AlertDialog.Builder(ReportMalaysiaTaxiActivity.this);
				builder.setTitle(getResources().getString(R.string.place_call));
				builder.setItems(tel_number_names, new DialogInterface.OnClickListener() {
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

	private void init_vars()
	{
		mPhotoUris.clear();
		mRecordingUris.clear();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		mOffence = "";
		mOffenceMalay = "";
		mSelected = new boolean[] {true, true, false, false, false};
	}

	private void submit()
	{
		String date = String.format("%02d", mDay) +
			'/' + String.format("%02d", mMonth+1) +
			'/' + mYear;
		String time = format_time(mHour, mMinute);
		String loc = ((EditText) findViewById(R.id.location_entry)).getText().toString();
		String reg = ((EditText) findViewById(R.id.reg_entry)).getText().toString();
		String other = ((EditText) findViewById(R.id.other_entry)).getText().toString();

		boolean sms_checked = ((CheckBox) findViewById(R.id.sms_checkbox)).isChecked();
		boolean email_checked = ((CheckBox) findViewById(R.id.email_checkbox)).isChecked();
		boolean tweet_checked = ((CheckBox) findViewById(R.id.tweet_checkbox)).isChecked();
		boolean other_checked = ((CheckBox) findViewById(R.id.other_checkbox)).isChecked();

		if (!sms_checked && !email_checked && !tweet_checked && !other_checked) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.missing_other),
					Toast.LENGTH_SHORT).show();
		}

		String msg = format_msg(date, time, loc, reg, mOffenceMalay, other);

		if (other_checked) {
			send_other(msg);
		}

		if (email_checked) {
			send_email(msg, reg);
		}

		if (tweet_checked) {
			send_tweet(date, time, loc, reg, other);
		}

		if (sms_checked) {
			send_sms(msg);
		}
	}

	private void send_other(String msg)
	{
		String action = (mPhotoUris.size() + mRecordingUris.size() > 1) ?
				Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;
		Intent other_intent = new Intent(action);
		other_intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.complaint_malay));
		other_intent.putExtra(Intent.EXTRA_TEXT, msg);

		ArrayList<Uri> uris = new ArrayList<Uri>();
		Iterator<Uri> itr = mPhotoUris.iterator();
		while (itr.hasNext()) {
			uris.add(itr.next());
		}
		itr = mRecordingUris.iterator();
		while (itr.hasNext()) {
			uris.add(itr.next());
		}
		if (uris.size() != 0) {
			other_intent.putExtra(Intent.EXTRA_STREAM, uris);
		}
		other_intent.setType("text/plain");
		startActivity(Intent.createChooser(other_intent, getResources().getString(R.string.send_other)));
	}

	private void send_email(String msg, String reg)
	{
		final String f_msg = msg;
		final String f_reg = reg;

		AlertDialog.Builder builder = new AlertDialog.Builder(ReportMalaysiaTaxiActivity.this);
		builder.setTitle(getResources().getString(R.string.who_email));
		builder.setMultiChoiceItems(getResources().getStringArray(R.array.email_choices),
				mSelected, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				mSelected[which] = isChecked;
			}
		});
		builder.setPositiveButton(getResources().getString(R.string.done), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String email_msg = format_email(f_msg);
				String action = Intent.ACTION_SEND_MULTIPLE;
				Intent email_intent = new Intent(action);

				String[] all_email_addresses = getResources().getStringArray(R.array.email_addresses);
				String email_addresses = "";

				for (int i=0; i < 5; ++i) {
					if (mSelected[i]) {
						email_addresses += all_email_addresses[i];
					}
				}

				email_intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
						email_addresses} );
				email_intent.putExtra(Intent.EXTRA_SUBJECT,
						getResources().getString(R.string.complaint_email_malay) + ' ' + f_reg);
				email_intent.putExtra(Intent.EXTRA_TEXT, email_msg);

				ArrayList<Uri> uris = new ArrayList<Uri>();
				Iterator<Uri> itr = mPhotoUris.iterator();
				while (itr.hasNext()) {
					uris.add(itr.next());
				}
				itr = mRecordingUris.iterator();
				while (itr.hasNext()) {
					uris.add(itr.next());
				}
				if (uris.size() != 0) {
					email_intent.putExtra(Intent.EXTRA_STREAM, uris);
				}
				email_intent.setType("text/plain");
				startActivity(Intent.createChooser(email_intent, getResources().getString(R.string.send_email)));
			}
		});

		/* TODO: pressing neutral button should not close dialog */
		builder.setNeutralButton(getResources().getString(R.string.details), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent email_details_intent = new Intent(getApplicationContext(), EmailDetailsActivity.class);
				startActivity(email_details_intent);
			}
		});
		AlertDialog alert = builder.create();
		ListView list = alert.getListView();
		for (int i=0; i < 5; ++i) {
			list.setItemChecked(i, mSelected[i]);
		}

		alert.show();
	}

	private void send_tweet(String date, String time, String loc,
			String reg, String other)
	{
		String tweet_msg = format_tweet(date, time, loc, reg, mOffence, other);
		Intent tweet_intent = new Intent(Intent.ACTION_SEND);
		tweet_intent.putExtra(Intent.EXTRA_TEXT, tweet_msg);
		if (!mPhotoUris.isEmpty()) {
			tweet_intent.putExtra(Intent.EXTRA_STREAM,
					mPhotoUris.get(mPhotoUris.size()-1));
		}
		tweet_intent.setType("text/plain");
		startActivity(Intent.createChooser(tweet_intent,
					getResources().getString(R.string.send_tweet)));
	}

	private void send_sms(String msg)
	{
		String sms_msg = format_sms(msg);
		Intent sms_intent = new Intent(Intent.ACTION_VIEW);

		sms_intent.putExtra("address",
				getResources().getString(R.string.sms_number));
		sms_intent.putExtra("sms_body", sms_msg);
		sms_intent.setType("vnd.android-dir/mms-sms");
		startActivity(sms_intent);
	}

	private String format_msg(String date, String time, String location,
			String reg, String offence, String other)
	{
		String message = "";
		if (date.length() != 0) {
			message += '\n' + date;
			if (time.length() != 0) {
				message += ' ' + time;
			}
		}

		if (reg.length() != 0) {
				message += '\n' + getResources().getString(R.string.registration_malay) + ": " + reg;
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

	private String format_email(String msg)
	{
		return getResources().getString(R.string.email_intro_malay) + "\n" + msg;
	}

	private String format_sms(String msg)
	{
		return getResources().getString(R.string.complaint_malay) + msg;
	}

	private String format_tweet(String date, String time, String location,
			String reg, String offence, String other)
	{
		String msg = getResources().getString(R.string.twitter_address) + " " +
				getResources().getString(R.string.complaint_hashtag);

		if (offence.equals("Other")) {
			offence = "";
		}

		/* don't cut down other fields if user description won't fit anyway */
		String orig_other = other;
		if (msg.length() + reg.length() + other.length() + 1 > 140) {
			other = "";
		}

		int extra_length = 1;
		if (location.length() != 0) {
			extra_length += 1;
		}
		if (offence.length() != 0) {
			extra_length += 2;
		}
		if (orig_other.length() != 0) {
			extra_length += 2;
		}
		if (reg.length() != 0) {
			extra_length += 1;
		}

		if (date.length() != 0 &&
				(msg.length() + reg.length() + date.length() + location.length() +
						offence.length() + other.length() + extra_length < 140)) {
			msg += ' ' + date;
			if (time.length() != 0 &&
					(msg.length() + reg.length() + time.length() + location.length() +
							offence.length() + other.length() + extra_length < 140)) {
				msg += ' ' + time;
			}
		}

		if (reg.length() != 0) {
			msg += ' ' + reg;
		}

		boolean need_comma = false;
		extra_length = 1;
		if (offence.length() != 0) {
			extra_length += 2;
		}
		if (orig_other.length() != 0) {
			extra_length += 2;
		}
		if (location.length() != 0 &&
				(msg.length() + location.length() +
						offence.length() + other.length() + 1 < 140)) {
				msg += " " + location;
				need_comma = true;
		}

		extra_length = need_comma ? 2 : 1;
		if (offence.length() != 0 &&
				(msg.length() + other.length() + extra_length < 140)) {
				if (need_comma) {
					msg += ',';
				}
				msg += " " + offence;
				need_comma = true;
		}

		if (orig_other.length() != 0) {
			if (need_comma) {
				msg += ',';
			}
			msg += ' ' + orig_other;
		}

		return msg;
	}

	private void update_date_label(int year, int month, int day)
	{
		TextView date_label = (TextView)findViewById(R.id.date_label);
		String date = DateFormat.getMediumDateFormat(getApplicationContext()).format(new Date(year - 1900, month, day));
		date_label.setText(date);
	}

	private void update_time_label(int hour, int minute)
	{
		TextView time_label = (TextView)findViewById(R.id.time_label);
		String time = DateFormat.getTimeFormat(getApplicationContext()).format(new Date(0, 0, 0, hour, minute, 0));
		time_label.setText(time);
	}

	private String format_time(int hour, int minute)
	{
		String am_pm = hour > 11 ? "PM" : "AM";
		if (hour > 12) {
			hour -= 12;
		}
		if (hour == 0) {
			hour = 12;
		}
		return String.format("%d", hour) + ':' + String.format("%02d", minute) + am_pm;
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ACTIVITY_TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				mPhotoUris.add(data.getData());
				((TextView)findViewById(R.id.camera_label)).setText(Integer.toString(mPhotoUris.size()));
			}
			break;
		case ACTIVITY_RECORD_SOUND:
			if (resultCode == RESULT_OK) {
				mRecordingUris.add(data.getData());
				((TextView)findViewById(R.id.recorder_label)).setText(Integer.toString(mRecordingUris.size()));
			}
			break;
		case ACTIVITY_UPDATE_SETTINGS:
			Intent refresh = new Intent(this, ReportMalaysiaTaxiActivity.class);
			startActivity(refresh);
			finish();
			break;
		}
	}

	public class OffenceOnItemSelectedListener implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			mOffence = getResources().getStringArray(R.array.offence_array)[pos];
			mOffenceMalay = getResources().getStringArray(R.array.offence_malay_array)[pos];
		}

		public void onNothingSelected(AdapterView<?> parent) {
		/* do nothing */
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
		case R.id.resources:
			Intent resources_intent = new Intent(getApplicationContext(), ResourcesActivity.class);
			startActivity(resources_intent);
			return true;
		case R.id.settings:
			Intent settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivityForResult(settings_intent, ACTIVITY_UPDATE_SETTINGS);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

