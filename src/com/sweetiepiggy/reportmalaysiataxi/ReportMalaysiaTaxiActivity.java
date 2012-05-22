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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReportMalaysiaTaxiActivity extends Activity
{
	private DataWrapper mData;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;

	static final int ACTIVITY_TAKE_PHOTO = 0;
	static final int ACTIVITY_RECORD_SOUND = 1;
	static final int ACTIVITY_TAKE_VIDEO = 3;
	static final int ACTIVITY_SUBMIT = 4;

	static final int MAX_TWEET_LENGTH = 140;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState == null) {
			mData = (DataWrapper) getLastNonConfigurationInstance();
			if (mData == null) {
				mData = new DataWrapper();
				init_vars(mData);
				init_selected(mData);
			}
		} else {
			mData = new DataWrapper();
			restore_saved_state(savedInstanceState);
		}

		init();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putInt("year", mData.year);
		savedInstanceState.putInt("month", mData.month);
		savedInstanceState.putInt("day", mData.day);
		savedInstanceState.putInt("hour", mData.hour);
		savedInstanceState.putInt("minute", mData.minute);

		savedInstanceState.putBooleanArray("who_selected", mData.who_selected);
		savedInstanceState.putBooleanArray("submit_selected", mData.submit_selected);

		savedInstanceState.putString("offence", mData.offence);
		savedInstanceState.putString("offence_malay", mData.offenceMalay);

		savedInstanceState.putStringArrayList("photo_uris", uriarr2strarr(mData.photoUris));
		savedInstanceState.putStringArrayList("recording_uris", uriarr2strarr(mData.recordingUris));
		savedInstanceState.putStringArrayList("video_uris", uriarr2strarr(mData.videoUris));

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		restore_saved_state(savedInstanceState);
	}

	private void restore_saved_state(Bundle savedInstanceState)
	{
		mData.year = savedInstanceState.getInt("year");
		mData.month = savedInstanceState.getInt("month");
		mData.day = savedInstanceState.getInt("day");
		mData.hour = savedInstanceState.getInt("hour");
		mData.minute = savedInstanceState.getInt("minute");

		mData.who_selected = savedInstanceState.getBooleanArray("who_selected");
		mData.submit_selected = savedInstanceState.getBooleanArray("submit_selected");

		mData.offence = savedInstanceState.getString("offence");
		mData.offenceMalay = savedInstanceState.getString("offence_malay");

		mData.sms_checked = savedInstanceState.getBoolean("sms_checked");
		mData.email_checked = savedInstanceState.getBoolean("email_checked");
		mData.tweet_checked = savedInstanceState.getBoolean("tweet_checked");
		mData.youtube_checked = savedInstanceState.getBoolean("youtube_checked");

		mData.photoUris = strarr2uriarr(savedInstanceState.getStringArrayList("photo_uris"));
		mData.recordingUris = strarr2uriarr(savedInstanceState.getStringArrayList("recording_uris"));
		mData.videoUris = strarr2uriarr(savedInstanceState.getStringArrayList("video_uris"));
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return mData;
	}

	private void init()
	{
		init_date_time_buttons();
		init_offence_spinner();
		init_camera_recorder_buttons();
		init_submit_button();
		init_cancel_button();
		init_call_button();
		init_entries(mData);
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
				Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(photo_intent, ACTIVITY_TAKE_PHOTO);
			}
		});

		Button vidcam_button = (Button) findViewById(R.id.vidcam_button);
		vidcam_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				startActivityForResult(video_intent, ACTIVITY_TAKE_VIDEO);
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

	/* TODO: init offence spinner choice? */
	private void init_entries(DataWrapper data)
	{
		update_date_label(mData.year, mData.month, mData.day);
		update_time_label(mData.hour, mData.minute);

		String photo_size = data.photoUris.size() > 0 ? Integer.toString(data.photoUris.size()) : "";
		String video_size = data.videoUris.size() > 0 ? Integer.toString(data.videoUris.size()) : "";
		String recording_size = data.recordingUris.size() > 0 ? Integer.toString(data.recordingUris.size()) : "";

		((TextView) findViewById(R.id.camera_label)).setText(photo_size);
		((TextView) findViewById(R.id.recorder_label)).setText(recording_size);
		((TextView) findViewById(R.id.video_label)).setText(video_size);
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
				} else if (mData.offenceMalay.equals("Other") &&
						((EditText) findViewById(R.id.other_entry)).getText().toString().length() == 0) {
					results_complete = false;
					incomplete_msg = getResources().getString(R.string.missing_other);
				}

				if (results_complete) {
					submit_menu();
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
				init_vars(mData);
				init_offence_spinner();
				init_entries(mData);
			}
		});
	}

	private void init_call_button()
	{
		Button call_button = (Button)findViewById(R.id.call_button);
		call_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String tel_number = Constants.SPAD_PHONE;
				tel_number += " (" + getResources().getString(R.string.spad) + ")";
				Intent call_intent = new Intent(Intent.ACTION_DIAL);
				call_intent.setData(Uri.parse("tel:" + tel_number));
				startActivity(call_intent);
			}
		});
	}

	private void init_selected(DataWrapper data)
	{
		/* TODO: selected defaults should not be hard coded here */
		data.who_selected = new boolean[] {true, true, true, false, false, false};
		data.submit_selected = new boolean[] {false, true, true, false};
	}

	private void init_vars(DataWrapper data)
	{
		final Calendar c = Calendar.getInstance();
		data.year = c.get(Calendar.YEAR);
		data.month = c.get(Calendar.MONTH);
		data.day = c.get(Calendar.DAY_OF_MONTH);
		data.hour = c.get(Calendar.HOUR_OF_DAY);
		data.minute = c.get(Calendar.MINUTE);

		data.offence = "";
		data.offenceMalay = "";

		data.photoUris = new ArrayList<Uri>();
		data.recordingUris = new ArrayList<Uri>();
		data.videoUris = new ArrayList<Uri>();

		data.youtube_sent = false;
		data.email_sent = false;
		data.tweet_sent = false;
		data.sms_sent = false;

		((EditText) findViewById(R.id.location_entry)).setText("");
		((EditText) findViewById(R.id.reg_entry)).setText("");
		((EditText) findViewById(R.id.other_entry)).setText("");

		data.sms_checked = true;
		data.email_checked = true;
		data.tweet_checked = true;
		data.youtube_checked = false;

	}

	private void submit_menu()
	{
		final String[] submit_choices = new String[] {
			getResources().getString(R.string.sms),
			getResources().getString(R.string.email),
			getResources().getString(R.string.tweet),
			getResources().getString(R.string.youtube),
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(ReportMalaysiaTaxiActivity.this);
		builder.setTitle(R.string.select_submit);
		builder.setMultiChoiceItems(submit_choices,
				mData.submit_selected, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean is_checked) {
				mData.submit_selected[which] = is_checked;
			}
		});
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mData.youtube_sent = false;
				mData.email_sent = false;
				mData.tweet_sent = false;
				mData.sms_sent = false;
				submit();
			}
		});
		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		AlertDialog alert = builder.create();
		ListView list = alert.getListView();
		for (int i=0; i < mData.submit_selected.length; ++i) {
			list.setItemChecked(i, mData.submit_selected[i]);
		}

		alert.show();
	}

	private void submit()
	{
		String date = String.format("%02d/%02d/%04d", mData.day, mData.month+1, mData.year);
		String time = format_time(mData.hour, mData.minute);
		String loc = ((EditText) findViewById(R.id.location_entry)).getText().toString();
		String reg = ((EditText) findViewById(R.id.reg_entry)).getText().toString();
		String other = ((EditText) findViewById(R.id.other_entry)).getText().toString();

		/* TODO: order of index shouldn't be hard coded like this */
		boolean sms_checked = mData.submit_selected[0];
		boolean email_checked = mData.submit_selected[1];
		boolean tweet_checked = mData.submit_selected[2];
		boolean youtube_checked = mData.submit_selected[3];

		String msg = format_msg(date, time, loc, reg, mData.offenceMalay, other);

		/* send one at a time, repeated call submit()
			until all checked are sent */
		if (sms_checked && !mData.sms_sent) {
			send_sms(msg);
		} else if (email_checked && !mData.email_sent) {
			send_email(msg, reg);
		} else if (tweet_checked && !mData.tweet_sent) {
			send_tweet(date, time, loc, reg, other);
		} else if (youtube_checked && !mData.youtube_sent) {
			send_youtube(msg);
		}
	}

	private void send_youtube(String msg)
	{
		String action = mData.videoUris.size() > 1 ?
			Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;
		Intent youtube_intent = new Intent(action);
		youtube_intent.putExtra(Intent.EXTRA_SUBJECT, Constants.COMPLAINT_MALAY);
		youtube_intent.putExtra(Intent.EXTRA_TEXT, msg);
		youtube_intent.setType("video/*");

		if (mData.videoUris.size() == 1) {
			youtube_intent.putExtra(Intent.EXTRA_STREAM,
					mData.videoUris.get(mData.videoUris.size()-1));
		} else if (mData.videoUris.size() > 1) {
			youtube_intent.putExtra(Intent.EXTRA_STREAM, mData.videoUris);
		}

		mData.youtube_sent = true;
		startActivityForResult(Intent.createChooser(youtube_intent,
					getResources().getString(R.string.send_youtube)),
				ACTIVITY_SUBMIT);
	}

	private void send_email(final String msg, final String reg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ReportMalaysiaTaxiActivity.this);
		builder.setTitle(R.string.who_email);
		builder.setMultiChoiceItems(R.array.email_choices,
				mData.who_selected, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean is_checked) {
				mData.who_selected[which] = is_checked;
			}
		});
		builder.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String email_msg = format_email(msg);

				String email_addresses = "";

				/* TODO: who_selected and EMAIL_ADDRESSES need to be better linked,
					possible problem if their lengths are not equal */
				for (int i=0; i < mData.who_selected.length; ++i) {
					if (mData.who_selected[i]) {
						email_addresses += Constants.EMAIL_ADDRESSES[i];
					}
				}

				ArrayList<Uri> uris = new ArrayList<Uri>();
				uris.addAll(mData.photoUris);
				uris.addAll(mData.recordingUris);
				uris.addAll(mData.videoUris);

				String action = uris.size() > 1 ?
					Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;

				Intent email_intent = new Intent(action);
				email_intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
						email_addresses} );
				email_intent.putExtra(Intent.EXTRA_SUBJECT,
						Constants.COMPLAINT_EMAIL_MALAY + ' ' + reg);
				email_intent.putExtra(Intent.EXTRA_TEXT, email_msg);

				if (uris.size() == 1) {
					email_intent.putExtra(Intent.EXTRA_STREAM,
							uris.get(uris.size()-1));
				} else if (uris.size() > 1) {
					email_intent.putExtra(Intent.EXTRA_STREAM, uris);
				}

				if (uris.size() == 0) {
					email_intent.setType("text/plain");
				} else if (mData.videoUris.size() > 0) {
					email_intent.setType("video/*");
				} else if (mData.photoUris.size() > 0) {
					email_intent.setType("image/*");
				} else if (mData.recordingUris.size() > 0) {
					email_intent.setType("audio/*");
				}

				mData.email_sent = true;
				/* don't createChooser() because AlertDialog will not close */
				startActivityForResult(email_intent, ACTIVITY_SUBMIT);
			}
		});

		builder.setNeutralButton(R.string.details, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
				Bundle b = new Bundle();
				b.putString("text", getResources().getString(R.string.email_details));
				intent.putExtras(b);
				startActivityForResult(intent, ACTIVITY_SUBMIT);
			}
		});

		builder.setNegativeButton(R.string.cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mData.email_sent = true;
				submit();
			}
		});

		AlertDialog alert = builder.create();
		ListView list = alert.getListView();
		for (int i=0; i < mData.who_selected.length; ++i) {
			list.setItemChecked(i, mData.who_selected[i]);
		}

		alert.show();
	}

	private void send_tweet(String date, String time, String loc,
			String reg, String other)
	{
		String tweet_msg = format_tweet(date, time, loc, reg, mData.offence, other);
		Intent tweet_intent = new Intent(Intent.ACTION_SEND);
		tweet_intent.putExtra(Intent.EXTRA_TEXT, tweet_msg);
		if (!mData.photoUris.isEmpty()) {
			tweet_intent.putExtra(Intent.EXTRA_STREAM,
					mData.photoUris.get(mData.photoUris.size()-1));
			tweet_intent.setType("image/*");
		} else {
			tweet_intent.setType("text/plain");
		}

		mData.tweet_sent = true;
		startActivityForResult(Intent.createChooser(tweet_intent,
					getResources().getString(R.string.send_tweet)),
				ACTIVITY_SUBMIT);
	}

	private void send_sms(String msg)
	{
		String sms_msg = format_sms(msg);
		Intent sms_intent = new Intent(Intent.ACTION_VIEW);

		sms_intent.putExtra("address",
				Constants.SMS_NUMBER);
		sms_intent.putExtra("sms_body", sms_msg);
		/* TODO: attach files to mms */
		sms_intent.setType("vnd.android-dir/mms-sms");
		mData.sms_sent = true;
		startActivityForResult(sms_intent, ACTIVITY_SUBMIT);
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
				message += '\n' + Constants.REGISTRATION_MALAY + ": " + reg;
		}
		if (location.length() != 0) {
				message += '\n' + Constants.LOCATION_MALAY + ": " + location;
		}
		if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + Constants.OFFENCE_MALAY + ": " + offence;
		}
		if (other.length() != 0) {
			if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + other;
			} else {
				message += '\n' + Constants.OFFENCE_MALAY + ": " + other;
			}
		}
		return message;
	}

	private String format_email(String msg)
	{
		return Constants.EMAIL_INTRO_MALAY + "\n" + msg;
	}

	private String format_sms(String msg)
	{
		return Constants.COMPLAINT_MALAY + msg;
	}

	private String format_tweet(String date, String time, String loc,
			String reg, String offence, String other)
	{
		/** map to keep track of which info should be printed and
			which should be dropped to keep tweet under 140 characters */
		HashMap<String, Boolean> map = new HashMap<String, Boolean>();
		map.put("twitter_address1", true);
		map.put("twitter_address2", false);
		map.put("twitter_address3", false);
		map.put("date", false);
		map.put("time", false);
		map.put("reg", reg.length() != 0);
		map.put("loc", false);
		map.put("offence", false);

		map.put("other", true);
		if (other.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("other", false);
		}

		map.put("offence", true);
		/* TODO: don't hard code Other */
		if (offence.length() == 0 || offence.equals("Other") ||
				build_tweet(map, date, time, loc, reg,
					offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("offence", false);
		}

		map.put("loc", true);
		if (loc.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("loc", false);
		}

		map.put("twitter_address2", true);
		if (build_tweet(map, date, time, loc, reg,
					offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("twitter_address2", false);
		}

		map.put("date", true);
		if (date.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("date", false);
		}

		map.put("time", true);
		if (time.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("time", false);
		}

		map.put("twitter_address3", true);
		if (build_tweet(map, date, time, loc, reg,
					offence, other).length() >
				MAX_TWEET_LENGTH) {
			map.put("twitter_address3", false);
		}

		/* always include additional details, but wait until here to
			set true to avoid cutting down other fields if user
			description won't fit anyway */
		if (other.length() != 0) {
			map.put("other", true);
		}

		return build_tweet(map, date, time, loc, reg, offence, other);
	}

	private String build_tweet(HashMap<String, Boolean> map, String date,
			String time, String loc, String reg, String offence, String other)
	{
		String res = "";

		if (map.get("twitter_address1")) {
			res += Constants.TWITTER_ADDRESS1;
		}

		if (map.get("date")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += date;
		}

		if (map.get("time")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += time;
		}

		if (map.get("reg")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += reg;
		}

		if (map.get("loc")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += loc;
		}

		if (map.get("offence")) {
			if (map.get("loc")) {
				res += ',';
			}
			if (res.length() != 0) {
				res += ' ';
			}
			res += offence.toLowerCase();
		}

		if (map.get("other")) {
			if (map.get("loc") || map.get("offence")) {
				res += ',';
			}
			if (res.length() != 0) {
				res += ' ';
			}
			res += other;
		}

		if (map.get("twitter_address2")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += Constants.TWITTER_ADDRESS2;
		}

		if (map.get("twitter_address3")) {
			if (res.length() != 0) {
				res += ' ';
			}
			res += Constants.TWITTER_ADDRESS3;
		}

		return res;
	}

	private void update_date_label(int year, int month, int day)
	{
		Button date_button = (Button)findViewById(R.id.date_button);
		Date d = new Date(year - 1900, month, day);

		String date = translate_day_of_week(DateFormat.format("EEEE", d).toString()) +
			" " + DateFormat.getLongDateFormat(getApplicationContext()).format(d);
		date_button.setText(date);
	}

	private String translate_day_of_week(String day)
	{
		String ret = day;
		if (day.equals("Monday")) {
			ret = getResources().getString(R.string.monday);
		} else if (day.equals("Tuesday")) {
			ret = getResources().getString(R.string.tuesday);
		} else if (day.equals("Wednesday")) {
			ret = getResources().getString(R.string.wednesday);
		} else if (day.equals("Thursday")) {
			ret = getResources().getString(R.string.thursday);
		} else if (day.equals("Friday")) {
			ret = getResources().getString(R.string.friday);
		} else if (day.equals("Saturday")) {
			ret = getResources().getString(R.string.saturday);
		} else if (day.equals("Sunday")) {
			ret = getResources().getString(R.string.sunday);
		}
		return ret;
	}

	private void update_time_label(int hour, int minute)
	{
		Button time_button = (Button)findViewById(R.id.time_button);
		String time = DateFormat.getTimeFormat(getApplicationContext()).format(new Date(0, 0, 0, hour, minute, 0));
		time_button.setText(time);
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
		return String.format("%d:%02d%s", hour, minute, am_pm);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mData.year = year;
					mData.month = monthOfYear;
					mData.day = dayOfMonth;
					update_date_label(mData.year, mData.month, mData.day);
				}
		};

		TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
				public void onTimeSet(TimePicker view,
						int hourOfDay, int minute) {
					mData.hour = hourOfDay;
					mData.minute = minute;
					update_time_label(mData.hour, mData.minute);
				}
		};

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mData.year,
					mData.month, mData.day);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mData.hour,
					mData.minute, false);
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
				mData.photoUris.add(data.getData());
				((TextView)findViewById(R.id.camera_label)).setText(Integer.toString(mData.photoUris.size()));
			}
			break;
		case ACTIVITY_TAKE_VIDEO:
			if (resultCode == RESULT_OK) {
				mData.videoUris.add(data.getData());
				((TextView)findViewById(R.id.video_label)).setText(Integer.toString(mData.videoUris.size()));
				mData.submit_selected[3] = true;
			}
			break;
		case ACTIVITY_RECORD_SOUND:
			if (resultCode == RESULT_OK) {
				mData.recordingUris.add(data.getData());
				((TextView)findViewById(R.id.recorder_label)).setText(Integer.toString(mData.recordingUris.size()));
			}
			break;
		case ACTIVITY_SUBMIT:
			/* repeatedly submit until all send_*() functions have been called */
			submit();
			break;
		}
	}

	public class OffenceOnItemSelectedListener implements OnItemSelectedListener
	{
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			mData.offence = getResources().getStringArray(R.array.offence_array)[pos];
			mData.offenceMalay = Constants.OFFENCE_MALAY_ARRAY[pos];
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private ArrayList<Uri> strarr2uriarr(ArrayList<String> str_arr)
	{
		ArrayList<Uri> ret = new ArrayList<Uri>();
		Iterator<String> itr = str_arr.iterator();
		while (itr.hasNext()) {
			ret.add(Uri.parse(itr.next()));
		}
		return ret;
	}

	private ArrayList<String> uriarr2strarr(ArrayList<Uri> uri_arr)
	{
		ArrayList<String> ret = new ArrayList<String>();
		Iterator<Uri> itr = uri_arr.iterator();
		while (itr.hasNext()) {
			ret.add(itr.next().toString());
		}
		return ret;
	}
}

