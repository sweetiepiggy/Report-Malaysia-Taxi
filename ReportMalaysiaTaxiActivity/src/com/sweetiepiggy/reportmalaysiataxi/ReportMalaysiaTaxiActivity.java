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

public class ReportMalaysiaTaxiActivity extends Activity {
    public class OffenceOnItemSelectedListener implements
            OnItemSelectedListener {
        @Override
        public void onItemSelected(final AdapterView<?> parent,
                final View view, final int pos, final long id) {
            ReportMalaysiaTaxiActivity.this.mData.offence = ReportMalaysiaTaxiActivity.this
                    .getResources().getStringArray(R.array.offence_array)[pos];
            ReportMalaysiaTaxiActivity.this.mData.email_offence = ReportMalaysiaTaxiActivity.this
                    .getResources().getStringArray(R.array.email_offence_array)[pos];
        }

        @Override
        public void onNothingSelected(final AdapterView<?> parent) {
            /* do nothing */
        }
    }

    private static final int ACTIVITY_GET_ATTACH = 5;
    private static final int ACTIVITY_RECORD_SOUND = 1;

    private static final int ACTIVITY_SUBMIT = 4;
    private static final int ACTIVITY_TAKE_PHOTO = 0;
    private static final int ACTIVITY_TAKE_VIDEO = 3;
    private static final int DATE_DIALOG_ID = 0;
    private static final int MAX_TWEET_LENGTH = 140;

    private static final String SOURCE_URL = "https://github.com/sweetiepiggy/Report-Malaysia-Taxi";

    private static final int TIME_DIALOG_ID = 1;

    private DataWrapper mData;

    private String build_tweet(final HashMap<String, Boolean> map,
            final String date, final String time, final String loc,
            final String reg, final String offence, final String details) {
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

    private String format_email(final String msg) {
        return this.getResources().getString(R.string.email_intro) + "\n" + msg;
    }

    private String format_msg(final String date, final String time,
            final String location, final String reg, final String offence,
            final String details) {
        String message = "";
        if (date.length() != 0) {
            message += '\n' + date;
            if (time.length() != 0) {
                message += ' ' + time;
            }
        }

        if (reg.length() != 0) {
            message += '\n' + this.getResources().getString(R.string.email_reg)
                    + ": " + reg;
        }
        if (location.length() != 0) {
            message += '\n' + this.getResources().getString(R.string.email_loc)
                    + ": " + location;
        }
        /* TODO: don't hardcode "Other" */
        if ((offence.length() != 0) && !offence.equals("Other")) {
            message += '\n'
                    + this.getResources().getString(R.string.email_offence)
                    + ": " + offence;
        }
        if (details.length() != 0) {
            if ((offence.length() != 0) && !offence.equals("Other")) {
                message += '\n' + details;
            } else {
                message += '\n'
                        + this.getResources().getString(R.string.email_offence)
                        + ": " + details;
            }
        }
        return message;
    }

    private String format_sms(final String msg) {
        return Constants.COMPLAINT_MALAY + msg;
    }

    private String format_time(int hour, final int minute) {
        final String am_pm = hour > 11 ? "PM" : "AM";
        if (hour > 12) {
            hour -= 12;
        }
        if (hour == 0) {
            hour = 12;
        }
        return String.format("%d:%02d%s", hour, minute, am_pm);
    }

    private String format_tweet(final String date, final String time,
            final String loc, final String reg, final String offence,
            final String details) {
        /**
         * map to keep track of which info should be printed and which should be
         * dropped to keep tweet under 140 characters
         */
        final HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("twitter_address1", true);
        map.put("twitter_address2", false);
        map.put("twitter_address3", false);
        map.put("date", false);
        map.put("time", false);
        map.put("reg", reg.length() != 0);
        map.put("loc", false);
        map.put("offence", false);

        int max_length = ReportMalaysiaTaxiActivity.MAX_TWEET_LENGTH;
        if (this.mData.photoUris.size() > 0) {
            max_length -= 21;
        }

        map.put("details", true);
        if ((details.length() == 0)
                || (this.build_tweet(map, date, time, loc, reg, offence,
                        details).length() > max_length)) {
            map.put("details", false);
        }

        map.put("offence", true);
        /* TODO: don't hard code Other */
        if ((offence.length() == 0)
                || offence.equals("Other")
                || (this.build_tweet(map, date, time, loc, reg, offence,
                        details).length() > max_length)) {
            map.put("offence", false);
        }

        map.put("loc", true);
        if ((loc.length() == 0)
                || (this.build_tweet(map, date, time, loc, reg, offence,
                        details).length() > max_length)) {
            map.put("loc", false);
        }

        map.put("twitter_address2", true);
        if (this.build_tweet(map, date, time, loc, reg, offence, details)
                .length() > max_length) {
            map.put("twitter_address2", false);
        }

        map.put("date", true);
        if ((date.length() == 0)
                || (this.build_tweet(map, date, time, loc, reg, offence,
                        details).length() > max_length)) {
            map.put("date", false);
        }

        map.put("time", true);
        if ((time.length() == 0)
                || (this.build_tweet(map, date, time, loc, reg, offence,
                        details).length() > max_length)) {
            map.put("time", false);
        }

        map.put("twitter_address3", true);
        if (this.build_tweet(map, date, time, loc, reg, offence, details)
                .length() > max_length) {
            map.put("twitter_address3", false);
        }

        /*
         * always include additional details, but wait until here to set true to
         * avoid cutting down other fields if user description won't fit anyway
         */
        if (details.length() != 0) {
            map.put("details", true);
        }

        return this.build_tweet(map, date, time, loc, reg, offence, details);
    }

    private void init() {
        this.init_date_time_buttons();
        this.init_offence_spinner();
        this.init_camera_recorder_buttons();
        this.init_submit_button();
        this.init_cancel_button();
        this.init_call_button();
        this.init_entries(this.mData);
    }

    private void init_call_button() {
        final Button call_button = (Button) this.findViewById(R.id.call_button);
        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String tel_number = Constants.SPAD_PHONE;
                tel_number += " ("
                        + ReportMalaysiaTaxiActivity.this.getResources()
                                .getString(R.string.spad) + ")";
                final Intent call_intent = new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:" + tel_number));
                ReportMalaysiaTaxiActivity.this.startActivity(call_intent);
            }
        });
    }

    private void init_camera_recorder_buttons() {
        final Button camera_button = (Button) this
                .findViewById(R.id.camera_button);
        final Button vidcam_button = (Button) this
                .findViewById(R.id.vidcam_button);
        final Button recorder_button = (Button) this
                .findViewById(R.id.recorder_button);
        final Button attachment_button = (Button) this
                .findViewById(R.id.attachment_button);

        // boolean has_camera =
        // getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        // boolean has_microphone =
        // getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
        final boolean has_camera = true;
        final boolean has_microphone = true;

        if (has_camera) {
            camera_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent photo_intent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    ReportMalaysiaTaxiActivity.this.startActivityForResult(
                            Intent.createChooser(
                                    photo_intent,
                                    ReportMalaysiaTaxiActivity.this
                                            .getResources().getString(
                                                    R.string.take_photo)),
                            ReportMalaysiaTaxiActivity.ACTIVITY_TAKE_PHOTO);
                }
            });

            vidcam_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent video_intent = new Intent(
                            MediaStore.ACTION_VIDEO_CAPTURE);
                    ReportMalaysiaTaxiActivity.this.startActivityForResult(
                            Intent.createChooser(
                                    video_intent,
                                    ReportMalaysiaTaxiActivity.this
                                            .getResources().getString(
                                                    R.string.record_video)),
                            ReportMalaysiaTaxiActivity.ACTIVITY_TAKE_VIDEO);
                }
            });
        } else {
            camera_button.setVisibility(View.GONE);
            vidcam_button.setVisibility(View.GONE);
        }

        if (has_microphone) {
            recorder_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final Intent recorder_intent = new Intent(
                            MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    ReportMalaysiaTaxiActivity.this.startActivityForResult(
                            Intent.createChooser(
                                    recorder_intent,
                                    ReportMalaysiaTaxiActivity.this
                                            .getResources().getString(
                                                    R.string.record_sound)),
                            ReportMalaysiaTaxiActivity.ACTIVITY_RECORD_SOUND);
                }
            });
        } else {
            recorder_button.setVisibility(View.GONE);
        }

        attachment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                ReportMalaysiaTaxiActivity.this.startActivityForResult(
                        Intent.createChooser(intent,
                                ReportMalaysiaTaxiActivity.this.getResources()
                                        .getString(R.string.attach_file)),
                        ReportMalaysiaTaxiActivity.ACTIVITY_GET_ATTACH);
            }
        });
    }

    private void init_cancel_button() {
        final Button cancel_button = (Button) this
                .findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ReportMalaysiaTaxiActivity.this.init_date_time_buttons();
                ReportMalaysiaTaxiActivity.this
                        .init_vars(ReportMalaysiaTaxiActivity.this.mData);
                ReportMalaysiaTaxiActivity.this.init_offence_spinner();
                ReportMalaysiaTaxiActivity.this
                        .init_entries(ReportMalaysiaTaxiActivity.this.mData);
            }
        });
    }

    private void init_date_time_buttons() {
        final Button date_button = (Button) this.findViewById(R.id.date_button);
        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ReportMalaysiaTaxiActivity.this
                        .showDialog(ReportMalaysiaTaxiActivity.DATE_DIALOG_ID);
            }
        });
        final Button time_button = (Button) this.findViewById(R.id.time_button);
        time_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ReportMalaysiaTaxiActivity.this
                        .showDialog(ReportMalaysiaTaxiActivity.TIME_DIALOG_ID);
            }
        });
    }

    /* TODO: init offence spinner choice? */
    private void init_entries(final DataWrapper data) {
        this.update_date_label(this.mData.year, this.mData.month,
                this.mData.day);
        this.update_time_label(this.mData.hour, this.mData.minute);

        final String photo_size = data.photoUris.size() > 0 ? Integer
                .toString(data.photoUris.size()) : "";
        final String video_size = data.videoUris.size() > 0 ? Integer
                .toString(data.videoUris.size()) : "";
        final String recording_size = data.recordingUris.size() > 0 ? Integer
                .toString(data.recordingUris.size()) : "";
        final String attachment_size = data.attachmentUris.size() > 0 ? Integer
                .toString(data.attachmentUris.size()) : "";

        ((TextView) this.findViewById(R.id.camera_label)).setText(photo_size);
        ((TextView) this.findViewById(R.id.recorder_label))
                .setText(recording_size);
        ((TextView) this.findViewById(R.id.video_label)).setText(video_size);
        ((TextView) this.findViewById(R.id.attachment_label))
                .setText(attachment_size);
    }

    private void init_offence_spinner() {
        final Spinner offence_spinner = (Spinner) this
                .findViewById(R.id.offence_spinner);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter
                .createFromResource(this, R.array.offence_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offence_spinner.setAdapter(adapter);
        offence_spinner
                .setOnItemSelectedListener(new OffenceOnItemSelectedListener());
    }

    private void init_selected(final DataWrapper data) {
        /* TODO: selected defaults should not be hard coded here */
        data.who_selected = new boolean[] { true, true, true, false, false,
                false };
        data.submit_selected = new boolean[] { false, true, false, false };
    }

    private void init_submit_button() {
        final Button submit_button = (Button) this
                .findViewById(R.id.submit_button);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean results_complete = true;
                String incomplete_msg = "";

                if (((EditText) ReportMalaysiaTaxiActivity.this
                        .findViewById(R.id.reg_entry)).getText().toString()
                        .length() == 0) {
                    results_complete = false;
                    incomplete_msg = ReportMalaysiaTaxiActivity.this
                            .getResources().getString(R.string.missing_reg);

                    /* TODO: don't hardcode "Other" */
                } else if ((ReportMalaysiaTaxiActivity.this.mData.email_offence
                        .equals("Other") || ReportMalaysiaTaxiActivity.this.mData.email_offence
                        .equals("Lain-lain"))
                        && (((EditText) ReportMalaysiaTaxiActivity.this
                                .findViewById(R.id.details_entry)).getText()
                                .toString().length() == 0)) {
                    results_complete = false;
                    incomplete_msg = ReportMalaysiaTaxiActivity.this
                            .getResources().getString(R.string.missing_details);
                }

                if (results_complete) {
                    ReportMalaysiaTaxiActivity.this.submit_menu();
                } else {
                    Toast.makeText(
                            ReportMalaysiaTaxiActivity.this
                                    .getApplicationContext(), incomplete_msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init_vars(final DataWrapper data) {
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

        ((EditText) this.findViewById(R.id.location_entry)).setText("");
        ((EditText) this.findViewById(R.id.reg_entry)).setText("");
        ((EditText) this.findViewById(R.id.details_entry)).setText("");

        data.sms_checked = true;
        data.email_checked = true;
        data.tweet_checked = true;
        data.youtube_checked = false;

    }

    @Override
    protected void onActivityResult(final int requestCode,
            final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case ACTIVITY_TAKE_PHOTO:
            if (resultCode == Activity.RESULT_OK) {
                this.mData.photoUris.add(data.getData());
                ((TextView) this.findViewById(R.id.camera_label))
                        .setText(Integer.toString(this.mData.photoUris.size()));
            }
            break;
        case ACTIVITY_TAKE_VIDEO:
            if (resultCode == Activity.RESULT_OK) {
                this.mData.videoUris.add(data.getData());
                ((TextView) this.findViewById(R.id.video_label))
                        .setText(Integer.toString(this.mData.videoUris.size()));
                this.mData.submit_selected[3] = true;
            }
            break;
        case ACTIVITY_RECORD_SOUND:
            if (resultCode == Activity.RESULT_OK) {
                this.mData.recordingUris.add(data.getData());
                ((TextView) this.findViewById(R.id.recorder_label))
                        .setText(Integer.toString(this.mData.recordingUris
                                .size()));
            }
            break;
        case ACTIVITY_GET_ATTACH:
            if (resultCode == Activity.RESULT_OK) {
                this.mData.attachmentUris.add(data.getData());
                ((TextView) this.findViewById(R.id.attachment_label))
                        .setText(Integer.toString(this.mData.attachmentUris
                                .size()));
            }
            break;
        case ACTIVITY_SUBMIT:
            /* repeatedly submit until all send_*() functions have been called */
            this.submit();
            break;
        }
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);

        if (savedInstanceState == null) {
            this.mData = (DataWrapper) this.getLastNonConfigurationInstance();
            if (this.mData == null) {
                this.mData = new DataWrapper();
                this.init_vars(this.mData);
                this.init_selected(this.mData);
            }
        } else {
            this.mData = new DataWrapper();
            this.restore_saved_state(savedInstanceState);
        }

        this.init();
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker view, final int year,
                    final int monthOfYear, final int dayOfMonth) {
                ReportMalaysiaTaxiActivity.this.mData.year = year;
                ReportMalaysiaTaxiActivity.this.mData.month = monthOfYear;
                ReportMalaysiaTaxiActivity.this.mData.day = dayOfMonth;
                ReportMalaysiaTaxiActivity.this.update_date_label(
                        ReportMalaysiaTaxiActivity.this.mData.year,
                        ReportMalaysiaTaxiActivity.this.mData.month,
                        ReportMalaysiaTaxiActivity.this.mData.day);
            }
        };

        final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker view, final int hourOfDay,
                    final int minute) {
                ReportMalaysiaTaxiActivity.this.mData.hour = hourOfDay;
                ReportMalaysiaTaxiActivity.this.mData.minute = minute;
                ReportMalaysiaTaxiActivity.this.update_time_label(
                        ReportMalaysiaTaxiActivity.this.mData.hour,
                        ReportMalaysiaTaxiActivity.this.mData.minute);
            }
        };

        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener,
                    this.mData.year, this.mData.month, this.mData.day);
        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, mTimeSetListener,
                    this.mData.hour, this.mData.minute, false);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
        case R.id.fare_rate:
            final Intent fare_rate_intent = new Intent(
                    this.getApplicationContext(), FareRateActivity.class);
            this.startActivity(fare_rate_intent);
            return true;
        case R.id.fare_calc:
            final Intent fare_calc_intent = new Intent(
                    this.getApplicationContext(), FareCalcActivity.class);
            this.startActivity(fare_calc_intent);
            return true;
        case R.id.contacts:
            final Intent contacts_intent = new Intent(
                    this.getApplicationContext(), ContactsActivity.class);
            this.startActivity(contacts_intent);
            return true;
        case R.id.about:
            final Intent about_intent = new Intent(
                    this.getApplicationContext(), TextViewActivity.class);
            final Bundle b = new Bundle();
            b.putString("text", this.getResources().getString(R.string.gpl));
            about_intent.putExtras(b);
            this.startActivity(about_intent);
            return true;
        case R.id.source:
            final Intent source_intent = new Intent(Intent.ACTION_VIEW);
            source_intent.setDataAndType(
                    Uri.parse(ReportMalaysiaTaxiActivity.SOURCE_URL),
                    "text/html");
            this.startActivity(Intent.createChooser(source_intent, null));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.restore_saved_state(savedInstanceState);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return this.mData;
    }

    @Override
    public void onSaveInstanceState(final Bundle savedInstanceState) {
        savedInstanceState.putInt("year", this.mData.year);
        savedInstanceState.putInt("month", this.mData.month);
        savedInstanceState.putInt("day", this.mData.day);
        savedInstanceState.putInt("hour", this.mData.hour);
        savedInstanceState.putInt("minute", this.mData.minute);

        savedInstanceState.putBooleanArray("who_selected",
                this.mData.who_selected);
        savedInstanceState.putBooleanArray("submit_selected",
                this.mData.submit_selected);

        savedInstanceState.putString("offence", this.mData.offence);
        savedInstanceState.putString("email_offence", this.mData.email_offence);

        savedInstanceState.putStringArrayList("photo_uris",
                this.uriarr2strarr(this.mData.photoUris));
        savedInstanceState.putStringArrayList("recording_uris",
                this.uriarr2strarr(this.mData.recordingUris));
        savedInstanceState.putStringArrayList("video_uris",
                this.uriarr2strarr(this.mData.videoUris));
        savedInstanceState.putStringArrayList("attachment_uris",
                this.uriarr2strarr(this.mData.attachmentUris));

        super.onSaveInstanceState(savedInstanceState);
    }

    private void restore_saved_state(final Bundle savedInstanceState) {
        this.mData.year = savedInstanceState.getInt("year");
        this.mData.month = savedInstanceState.getInt("month");
        this.mData.day = savedInstanceState.getInt("day");
        this.mData.hour = savedInstanceState.getInt("hour");
        this.mData.minute = savedInstanceState.getInt("minute");

        this.mData.who_selected = savedInstanceState
                .getBooleanArray("who_selected");
        this.mData.submit_selected = savedInstanceState
                .getBooleanArray("submit_selected");

        this.mData.offence = savedInstanceState.getString("offence");
        this.mData.email_offence = savedInstanceState
                .getString("email_offence");

        this.mData.sms_checked = savedInstanceState.getBoolean("sms_checked");
        this.mData.email_checked = savedInstanceState
                .getBoolean("email_checked");
        this.mData.tweet_checked = savedInstanceState
                .getBoolean("tweet_checked");
        this.mData.youtube_checked = savedInstanceState
                .getBoolean("youtube_checked");

        this.mData.photoUris = this.strarr2uriarr(savedInstanceState
                .getStringArrayList("photo_uris"));
        this.mData.recordingUris = this.strarr2uriarr(savedInstanceState
                .getStringArrayList("recording_uris"));
        this.mData.videoUris = this.strarr2uriarr(savedInstanceState
                .getStringArrayList("video_uris"));
        this.mData.attachmentUris = this.strarr2uriarr(savedInstanceState
                .getStringArrayList("attachment_uris"));
    }

    private void send_email(final String msg, final String reg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                ReportMalaysiaTaxiActivity.this);
        builder.setTitle(R.string.who_email);
        builder.setMultiChoiceItems(R.array.email_choices,
                this.mData.who_selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int which, final boolean is_checked) {
                        ReportMalaysiaTaxiActivity.this.mData.who_selected[which] = is_checked;
                    }
                });
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final String email_msg = ReportMalaysiaTaxiActivity.this
                        .format_email(msg);

                String email_addresses = "";

                /*
                 * TODO: who_selected and EMAIL_ADDRESSES need to be better
                 * linked, possible problem if their lengths are not equal
                 */
                for (int i = 0; i < ReportMalaysiaTaxiActivity.this.mData.who_selected.length; ++i) {
                    if (ReportMalaysiaTaxiActivity.this.mData.who_selected[i]) {
                        email_addresses += Constants.EMAIL_ADDRESSES[i];
                    }
                }

                final ArrayList<Uri> uris = new ArrayList<Uri>();
                uris.addAll(ReportMalaysiaTaxiActivity.this.mData.photoUris);
                uris.addAll(ReportMalaysiaTaxiActivity.this.mData.recordingUris);
                uris.addAll(ReportMalaysiaTaxiActivity.this.mData.videoUris);
                uris.addAll(ReportMalaysiaTaxiActivity.this.mData.attachmentUris);

                final Intent email_intent = new Intent(
                        Intent.ACTION_SEND_MULTIPLE);
                email_intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { email_addresses });
                email_intent.putExtra(Intent.EXTRA_SUBJECT,
                        Constants.COMPLAINT_EMAIL_MALAY + ' ' + reg);
                email_intent.putExtra(Intent.EXTRA_TEXT, email_msg);

                if (uris.size() > 0) {
                    email_intent.putExtra(Intent.EXTRA_STREAM, uris);
                }

                if (uris.size() == 0) {
                    email_intent.setType("text/plain");
                } else if (ReportMalaysiaTaxiActivity.this.mData.videoUris
                        .size() > 0) {
                    email_intent.setType("video/*");
                } else if (ReportMalaysiaTaxiActivity.this.mData.photoUris
                        .size() > 0) {
                    email_intent.setType("image/*");
                } else if (ReportMalaysiaTaxiActivity.this.mData.recordingUris
                        .size() > 0) {
                    email_intent.setType("audio/*");
                } else if (ReportMalaysiaTaxiActivity.this.mData.attachmentUris
                        .size() > 0) {
                    email_intent.setType("file/*");
                }

                ReportMalaysiaTaxiActivity.this.mData.email_sent = true;
                /* don't createChooser() because AlertDialog will not close */
                // startActivityForResult(email_intent, ACTIVITY_SUBMIT);
                /*
                 * do createChooser(), will not close AlertDialog but at least
                 * string.send_email can be used
                 */
                ReportMalaysiaTaxiActivity.this.startActivityForResult(Intent
                        .createChooser(email_intent,
                                ReportMalaysiaTaxiActivity.this.getResources()
                                        .getString(R.string.send_email)),
                        ReportMalaysiaTaxiActivity.ACTIVITY_SUBMIT);
            }
        });

        builder.setNeutralButton(R.string.who_details, new OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                final Intent intent = new Intent(
                        ReportMalaysiaTaxiActivity.this.getApplicationContext(),
                        TextViewActivity.class);
                final Bundle b = new Bundle();
                b.putString("text", ReportMalaysiaTaxiActivity.this
                        .getResources().getString(R.string.email_details));
                intent.putExtras(b);
                ReportMalaysiaTaxiActivity.this.startActivityForResult(intent,
                        ReportMalaysiaTaxiActivity.ACTIVITY_SUBMIT);
            }
        });

        builder.setNegativeButton(android.R.string.cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int which) {
                        ReportMalaysiaTaxiActivity.this.mData.email_sent = true;
                        ReportMalaysiaTaxiActivity.this.submit();
                    }
                });

        final AlertDialog alert = builder.create();
        final ListView list = alert.getListView();
        for (int i = 0; i < this.mData.who_selected.length; ++i) {
            list.setItemChecked(i, this.mData.who_selected[i]);
        }

        alert.show();
    }

    private void send_sms(final String msg) {
        final String sms_msg = this.format_sms(msg);
        final Intent sms_intent = new Intent(Intent.ACTION_VIEW);

        sms_intent.putExtra("address", Constants.SMS_NUMBER);
        sms_intent.putExtra("sms_body", sms_msg);
        /* TODO: attach files to mms */
        sms_intent.setType("vnd.android-dir/mms-sms");
        this.mData.sms_sent = true;
        this.startActivityForResult(sms_intent,
                ReportMalaysiaTaxiActivity.ACTIVITY_SUBMIT);
    }

    private void send_tweet(final String date, final String time,
            final String loc, final String reg, final String details) {
        final String tweet_msg = this.format_tweet(date, time, loc, reg,
                this.mData.offence, details);
        final Intent tweet_intent = new Intent(Intent.ACTION_SEND);
        tweet_intent.putExtra(Intent.EXTRA_TEXT, tweet_msg);
        if (!this.mData.photoUris.isEmpty()) {
            tweet_intent.putExtra(Intent.EXTRA_STREAM,
                    this.mData.photoUris.get(this.mData.photoUris.size() - 1));
            tweet_intent.setType("image/*");
        } else {
            tweet_intent.setType("text/plain");
        }

        this.mData.tweet_sent = true;
        this.startActivityForResult(Intent.createChooser(tweet_intent, this
                .getResources().getString(R.string.send_tweet)),
                ReportMalaysiaTaxiActivity.ACTIVITY_SUBMIT);
    }

    private void send_youtube(final String msg) {
        final String action = this.mData.videoUris.size() > 1 ? Intent.ACTION_SEND_MULTIPLE
                : Intent.ACTION_SEND;
        final Intent youtube_intent = new Intent(action);
        youtube_intent
                .putExtra(Intent.EXTRA_SUBJECT, Constants.COMPLAINT_MALAY);
        youtube_intent.putExtra(Intent.EXTRA_TEXT, msg);
        youtube_intent.setType("video/*");

        if (this.mData.videoUris.size() == 1) {
            youtube_intent.putExtra(Intent.EXTRA_STREAM,
                    this.mData.videoUris.get(this.mData.videoUris.size() - 1));
        } else if (this.mData.videoUris.size() > 1) {
            youtube_intent.putExtra(Intent.EXTRA_STREAM, this.mData.videoUris);
        }

        this.mData.youtube_sent = true;
        this.startActivityForResult(Intent.createChooser(youtube_intent, this
                .getResources().getString(R.string.send_youtube)),
                ReportMalaysiaTaxiActivity.ACTIVITY_SUBMIT);
    }

    private ArrayList<Uri> strarr2uriarr(final ArrayList<String> str_arr) {
        final ArrayList<Uri> ret = new ArrayList<Uri>();
        final Iterator<String> itr = str_arr.iterator();
        while (itr.hasNext()) {
            ret.add(Uri.parse(itr.next()));
        }
        return ret;
    }

    private void submit() {
        final String date = String.format("%02d/%02d/%04d", this.mData.day,
                this.mData.month + 1, this.mData.year);
        final String time = this
                .format_time(this.mData.hour, this.mData.minute);
        final String loc = ((EditText) this.findViewById(R.id.location_entry))
                .getText().toString();
        final String reg = ((EditText) this.findViewById(R.id.reg_entry))
                .getText().toString();
        final String details = ((EditText) this
                .findViewById(R.id.details_entry)).getText().toString();

        /* TODO: order of index shouldn't be hard coded like this */
        final boolean sms_checked = this.mData.submit_selected[0];
        final boolean email_checked = this.mData.submit_selected[1];
        final boolean tweet_checked = this.mData.submit_selected[2];
        final boolean youtube_checked = this.mData.submit_selected[3];

        final String msg = this.format_msg(date, time, loc, reg,
                this.mData.email_offence, details);

        /*
         * send one at a time, repeated call submit() until all checked are sent
         */
        if (sms_checked && !this.mData.sms_sent) {
            this.send_sms(msg);
        } else if (email_checked && !this.mData.email_sent) {
            this.send_email(msg, reg);
        } else if (tweet_checked && !this.mData.tweet_sent) {
            this.send_tweet(date, time, loc, reg, details);
        } else if (youtube_checked && !this.mData.youtube_sent) {
            this.send_youtube(msg);
        }
    }

    private void submit_menu() {
        final String[] submit_choices = new String[] {
                this.getResources().getString(R.string.sms),
                this.getResources().getString(R.string.email),
                this.getResources().getString(R.string.tweet),
                this.getResources().getString(R.string.youtube), };

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                ReportMalaysiaTaxiActivity.this);
        builder.setTitle(R.string.select_submit);
        builder.setMultiChoiceItems(submit_choices, this.mData.submit_selected,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int which, final boolean is_checked) {
                        ReportMalaysiaTaxiActivity.this.mData.submit_selected[which] = is_checked;
                    }
                });
        builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                ReportMalaysiaTaxiActivity.this.mData.youtube_sent = false;
                ReportMalaysiaTaxiActivity.this.mData.email_sent = false;
                ReportMalaysiaTaxiActivity.this.mData.tweet_sent = false;
                ReportMalaysiaTaxiActivity.this.mData.sms_sent = false;
                ReportMalaysiaTaxiActivity.this.submit();
            }
        });
        builder.setNegativeButton(android.R.string.cancel,
                new OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog,
                            final int which) {
                    }
                });

        final AlertDialog alert = builder.create();
        final ListView list = alert.getListView();
        for (int i = 0; i < this.mData.submit_selected.length; ++i) {
            list.setItemChecked(i, this.mData.submit_selected[i]);
        }

        alert.show();
    }

    private String translate_day_of_week(final String day) {
        String ret = day;
        if (day.equals("Monday")) {
            ret = this.getResources().getString(R.string.monday);
        } else if (day.equals("Tuesday")) {
            ret = this.getResources().getString(R.string.tuesday);
        } else if (day.equals("Wednesday")) {
            ret = this.getResources().getString(R.string.wednesday);
        } else if (day.equals("Thursday")) {
            ret = this.getResources().getString(R.string.thursday);
        } else if (day.equals("Friday")) {
            ret = this.getResources().getString(R.string.friday);
        } else if (day.equals("Saturday")) {
            ret = this.getResources().getString(R.string.saturday);
        } else if (day.equals("Sunday")) {
            ret = this.getResources().getString(R.string.sunday);
        }
        return ret;
    }

    private void update_date_label(final int year, final int month,
            final int day) {
        final Button date_button = (Button) this.findViewById(R.id.date_button);
        final Date d = new Date(year - 1900, month, day);

        final String date = this.translate_day_of_week(DateFormat.format(
                "EEEE", d).toString())
                + " "
                + DateFormat.getLongDateFormat(this.getApplicationContext())
                        .format(d);
        date_button.setText(date);
    }

    private void update_time_label(final int hour, final int minute) {
        final Button time_button = (Button) this.findViewById(R.id.time_button);
        final String time = DateFormat.getTimeFormat(
                this.getApplicationContext()).format(
                new Date(0, 0, 0, hour, minute, 0));
        time_button.setText(time);
    }

    private ArrayList<String> uriarr2strarr(final ArrayList<Uri> uri_arr) {
        final ArrayList<String> ret = new ArrayList<String>();
        final Iterator<Uri> itr = uri_arr.iterator();
        while (itr.hasNext()) {
            ret.add(itr.next().toString());
        }
        return ret;
    }
}
