/*
    Copyright (C) 2012,2013
    Sweetie Piggy Apps <sweetiepiggyapps@gmail.com>
    CyanogenMod-X <rdxperiaz@gmail.com>

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
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

@SuppressLint({ "DefaultLocale", "NewApi" })
@SuppressWarnings("unused")
public class ReportMalaysiaTaxiActivity extends Activity implements android.view.View.OnClickListener
{
	private DataWrapper mData;

	private static final int DATE_DIALOG_ID = 0;
	private static final int TIME_DIALOG_ID = 1;

	private static final int ACTIVITY_TAKE_PHOTO = 0;
	private static final int ACTIVITY_RECORD_SOUND = 1;
	private static final int ACTIVITY_TAKE_VIDEO = 3;
	private static final int ACTIVITY_SUBMIT = 4;
	private static final int ACTIVITY_GET_ATTACH = 5;

	private static final int MAX_TWEET_LENGTH = 140;

	private static final String SOURCE_URL = "https://github.com/sweetiepiggy/Report-Malaysia-Taxi";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//attach location listener to button
		findViewById(R.id.location_button).setOnClickListener(this);

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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.exit_prompt)
        .setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'NO' Button
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle(R.string.exit_title);
        alert.show();
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
		savedInstanceState.putString("email_offence", mData.email_offence);

		savedInstanceState.putStringArrayList("photo_uris", uriarr2strarr(mData.photoUris));
		savedInstanceState.putStringArrayList("recording_uris", uriarr2strarr(mData.recordingUris));
		savedInstanceState.putStringArrayList("video_uris", uriarr2strarr(mData.videoUris));
		savedInstanceState.putStringArrayList("attachment_uris", uriarr2strarr(mData.attachmentUris));

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
		mData.email_offence = savedInstanceState.getString("email_offence");

		mData.sms_checked = savedInstanceState.getBoolean("sms_checked");
		mData.email_checked = savedInstanceState.getBoolean("email_checked");
		mData.tweet_checked = savedInstanceState.getBoolean("tweet_checked");
		mData.youtube_checked = savedInstanceState.getBoolean("youtube_checked");

		mData.photoUris = strarr2uriarr(savedInstanceState.getStringArrayList("photo_uris"));
		mData.recordingUris = strarr2uriarr(savedInstanceState.getStringArrayList("recording_uris"));
		mData.videoUris = strarr2uriarr(savedInstanceState.getStringArrayList("video_uris"));
		mData.attachmentUris = strarr2uriarr(savedInstanceState.getStringArrayList("attachment_uris"));
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

	private void init_camera_recorder_buttons()
	{
		Button camera_button = (Button) findViewById(R.id.camera_button);
		Button vidcam_button = (Button) findViewById(R.id.vidcam_button);
		Button recorder_button = (Button) findViewById(R.id.recorder_button);
		Button attachment_button = (Button) findViewById(R.id.attachment_button);

		//boolean has_camera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
		//boolean has_microphone = getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
		boolean has_camera = true;
		boolean has_microphone = true;

		if (has_camera) {
			camera_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent photo_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(Intent.createChooser(photo_intent,
							getResources().getString(R.string.take_photo)),
						ACTIVITY_TAKE_PHOTO);
				}
			});

			vidcam_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent video_intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
					startActivityForResult(Intent.createChooser(video_intent,
							getResources().getString(R.string.record_video)),
						ACTIVITY_TAKE_VIDEO);
				}
			});
		} else {
			camera_button.setVisibility(View.GONE);
			vidcam_button.setVisibility(View.GONE);
		}

		if (has_microphone) {
			recorder_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent recorder_intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
					startActivityForResult(Intent.createChooser(recorder_intent,
							getResources().getString(R.string.record_sound)),
						ACTIVITY_RECORD_SOUND);
				}
			});
		} else {
			recorder_button.setVisibility(View.GONE);
		}

		attachment_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("file/*");
				startActivityForResult(Intent.createChooser(intent,
						getResources().getString(R.string.attach_file)),
					ACTIVITY_GET_ATTACH);
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
		String attachment_size = data.attachmentUris.size() > 0 ? Integer.toString(data.attachmentUris.size()) : "";

		((TextView) findViewById(R.id.camera_label)).setText(photo_size);
		((TextView) findViewById(R.id.recorder_label)).setText(recording_size);
		((TextView) findViewById(R.id.video_label)).setText(video_size);
		((TextView) findViewById(R.id.attachment_label)).setText(attachment_size);
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
				} else if ((mData.email_offence.equals("Other") || mData.email_offence.equals("Lain-lain"))
					&& ((EditText) findViewById(R.id.details_entry)).getText().toString().length() == 0) {
					results_complete = false;
					incomplete_msg = getResources().getString(R.string.missing_details);
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
		data.submit_selected = new boolean[] {false, true, false, false};
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
		data.email_offence = "";

		data.photoUris = new ArrayList<Uri>();
		data.recordingUris = new ArrayList<Uri>();
		data.videoUris = new ArrayList<Uri>();
		data.attachmentUris = new ArrayList<Uri>();

		data.youtube_sent = false;
		data.email_sent = false;
		data.tweet_sent = false;
		data.sms_sent = false;

		((EditText) findViewById(R.id.location_entry)).setText("");
		((EditText) findViewById(R.id.reg_entry)).setText("");
		((EditText) findViewById(R.id.details_entry)).setText("");

		data.sms_checked = true;
		data.email_checked = true;
		data.tweet_checked = true;
		data.youtube_checked = false;

	}

	@SuppressLint("DefaultLocale")
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
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mData.youtube_sent = false;
				mData.email_sent = false;
				mData.tweet_sent = false;
				mData.sms_sent = false;
				submit();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
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
		String details = ((EditText) findViewById(R.id.details_entry)).getText().toString();

		/* TODO: order of index shouldn't be hard coded like this */
		boolean sms_checked = mData.submit_selected[0];
		boolean email_checked = mData.submit_selected[1];
		boolean tweet_checked = mData.submit_selected[2];
		boolean youtube_checked = mData.submit_selected[3];

		String msg = format_msg(date, time, loc, reg, mData.email_offence, details);

		/* send one at a time, repeated call submit()
			until all checked are sent */
		if (sms_checked && !mData.sms_sent) {
			send_sms(msg);
		} else if (email_checked && !mData.email_sent) {
			send_email(msg, reg);
		} else if (tweet_checked && !mData.tweet_sent) {
			send_tweet(date, time, loc, reg, details);
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
		builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
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
				uris.addAll(mData.attachmentUris);

				String action = uris.size() > 1 ?
					Intent.ACTION_SEND_MULTIPLE : Intent.ACTION_SEND;

				Intent email_intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
				email_intent.putExtra(Intent.EXTRA_EMAIL, new String[] {
						email_addresses} );
				email_intent.putExtra(Intent.EXTRA_SUBJECT,
						Constants.COMPLAINT_EMAIL_MALAY + ' ' + reg);
				email_intent.putExtra(Intent.EXTRA_TEXT, email_msg);

				if (uris.size() > 0) {
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
				} else if (mData.attachmentUris.size() > 0) {
					email_intent.setType("file/*");
				}

				mData.email_sent = true;
				/* don't createChooser() because AlertDialog will not close */
				//startActivityForResult(email_intent, ACTIVITY_SUBMIT);
				/* do createChooser(), will not close
					AlertDialog but at least string.send_email can be used */
				startActivityForResult(Intent.createChooser(email_intent,
							getResources().getString(R.string.send_email)),
						ACTIVITY_SUBMIT);
			}
		});

		builder.setNeutralButton(R.string.who_details, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
				Bundle b = new Bundle();
				b.putString("text", getResources().getString(R.string.email_details));
				intent.putExtras(b);
				startActivityForResult(intent, ACTIVITY_SUBMIT);
			}
		});

		builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
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
			String reg, String details)
	{
		String tweet_msg = format_tweet(date, time, loc, reg, mData.offence, details);
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
			String reg, String offence, String details)
	{
		String message = "";
		if (date.length() != 0) {
			message += '\n' + date;
			if (time.length() != 0) {
				message += ' ' + time;
			}
		}

		if (reg.length() != 0) {
				message += '\n' + getResources().getString(R.string.email_reg) + ": " + reg;
		}
		if (location.length() != 0) {
				message += '\n' + getResources().getString(R.string.email_loc) + ": " + location;
		}
		/* TODO: don't hardcode "Other" */
		if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + getResources().getString(R.string.email_offence) + ": " + offence;
		}
		if (details.length() != 0) {
			if (offence.length() != 0 && !offence.equals("Other")) {
				message += '\n' + details;
			} else {
				message += '\n' + getResources().getString(R.string.email_offence) + ": " + details;
			}
		}
		return message;
	}

	private String format_email(String msg)
	{
		return getResources().getString(R.string.email_intro) + "\n" + msg;
	}

	private String format_sms(String msg)
	{
		return Constants.COMPLAINT_MALAY + msg;
	}

	private String format_tweet(String date, String time, String loc,
			String reg, String offence, String details)
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

		int max_length = MAX_TWEET_LENGTH;
		if (mData.photoUris.size() > 0) {
			max_length -= 21;
		}

		map.put("details", true);
		if (details.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, details).length() >
				max_length) {
			map.put("details", false);
		}

		map.put("offence", true);
		/* TODO: don't hard code Other */
		if (offence.length() == 0 || offence.equals("Other") ||
				build_tweet(map, date, time, loc, reg,
					offence, details).length() >
				max_length) {
			map.put("offence", false);
		}

		map.put("loc", true);
		if (loc.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, details).length() >
				max_length) {
			map.put("loc", false);
		}

		map.put("twitter_address2", true);
		if (build_tweet(map, date, time, loc, reg,
					offence, details).length() >
				max_length) {
			map.put("twitter_address2", false);
		}

		map.put("date", true);
		if (date.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, details).length() >
				max_length) {
			map.put("date", false);
		}

		map.put("time", true);
		if (time.length() == 0 || build_tweet(map, date, time,
					loc, reg, offence, details).length() >
				max_length) {
			map.put("time", false);
		}

		map.put("twitter_address3", true);
		if (build_tweet(map, date, time, loc, reg,
					offence, details).length() >
				max_length) {
			map.put("twitter_address3", false);
		}

		/* always include additional details, but wait until here to
			set true to avoid cutting down other fields if user
			description won't fit anyway */
		if (details.length() != 0) {
			map.put("details", true);
		}

		return build_tweet(map, date, time, loc, reg, offence, details);
	}

	private String build_tweet(HashMap<String, Boolean> map, String date,
			String time, String loc, String reg, String offence, String details)
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

		if (map.get("details")) {
			if (map.get("loc") || map.get("offence")) {
				res += ',';
			}
			if (res.length() != 0) {
				res += ' ';
			}
			res += details;
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
		case ACTIVITY_GET_ATTACH:
			if (resultCode == RESULT_OK) {
				mData.attachmentUris.add(data.getData());
				((TextView) findViewById(R.id.attachment_label)).setText(Integer.toString(mData.attachmentUris.size()));
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
			mData.email_offence = getResources().getStringArray(R.array.email_offence_array)[pos];
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
		case R.id.fare_rate:
			Intent fare_rate_intent = new Intent(getApplicationContext(), FareRateActivity.class);
			startActivity(fare_rate_intent);
			return true;
		case R.id.fare_calc:
			Intent fare_calc_intent = new Intent(getApplicationContext(), FareCalcActivity.class);
			startActivity(fare_calc_intent);
			return true;
		case R.id.contacts:
			Intent contacts_intent = new Intent(getApplicationContext(), ContactsActivity.class);
			startActivity(contacts_intent);
			return true;
		case R.id.about:
			Intent about_intent = new Intent(getApplicationContext(), TextViewActivity.class);
			Bundle b = new Bundle();
			b.putString("text", getResources().getString(R.string.gpl));
			about_intent.putExtras(b);
			startActivity(about_intent);
			return true;
		case R.id.source:
			Intent source_intent = new Intent(Intent.ACTION_VIEW);
			source_intent.setDataAndType(Uri.parse(SOURCE_URL), "text/html");
			startActivity(Intent.createChooser(source_intent, null));
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
			final Uri next = itr.next();
			if (next != null) {
				ret.add(next.toString());
			}
		}
		return ret;
	}
	
	public void onClick(View arg0) {
		//click listener for location button
		
	    
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_HIGH);
		String provider = locationManager.getBestProvider(criteria, true);
		
		if (provider == null) {
			Toast.makeText(getApplicationContext(),
					"enable location services to use this feature",
					Toast.LENGTH_SHORT).show();
		} else {
			locationManager.requestSingleUpdate(criteria, new LocationListener(){
				@SuppressLint("NewApi")
				@Override
				public void onLocationChanged(Location location) {
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ECLAIR)
						createDialog();
					else if(Geocoder.isPresent()) 
				    (new ReverseGeocodingTask(getBaseContext())).execute(new Location[] {location});
						// Invoking reverse geocoding in an AsyncTask. 
				}
				@Override public void onProviderDisabled(String provider) { }
				@Override public void onProviderEnabled(String provider) { }
				@Override public void onStatusChanged(String provider, int status, Bundle extras) { }
				
			}, null);
		}
		
		
		
	}
	
	protected void createDialog() {
		// TODO Auto-generated method stub
		
	}

	// AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
	// we do not want to invoke it from the UI thread.
	private class ReverseGeocodingTask extends AsyncTask<Location, Void, List<Address> >{
	    Context mContext;

	    public ReverseGeocodingTask(Context context) {
	        super();
	        mContext = context;
	    }
	    
	    @Override
	    protected List<Address> doInBackground(Location... params) {
	        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

	        Location loc = params[0];
	        List<Address> addresses = null;
	        try {
	            // Call the synchronous getFromLocation() method by passing in the lat/long values.
	            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        if (addresses != null && addresses.size() > 0) {
	            //Address address = addresses.get(0);
	            // Format the first line of address (if available), city, and country name.
	            /*
	            String addressText = String.format("%s, %s, %s",
	                    address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                    address.getLocality(),
	                    address.getCountryName()); */
	        }
	        return addresses;
	    }
	    
	    protected void onPostExecute(List<Address> result){
	    	
	    	String address = "";
	    	if (result != null){
		    	for (int i = 0; i <= result.get(0).getMaxAddressLineIndex(); i++){
		    		address += " " + result.get(0).getAddressLine(i);
		    	}
		    	address.trim();
		    	
	    	} else {address = "failed";}// if address == null, output 'failed', since dialogs don't work from here
	    	
	    	((EditText)findViewById(R.id.location_entry)).setText(address);
	    }
	}
}

