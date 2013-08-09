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

import java.util.ArrayList;

import android.net.Uri;

public class DataWrapper {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public String offence;
	public String email_offence;

	public boolean[] who_selected;
	public boolean[] submit_selected;

	public ArrayList<Uri> photoUris;
	public ArrayList<Uri> recordingUris;
	public ArrayList<Uri> videoUris;
	public ArrayList<Uri> attachmentUris;

	public boolean youtube_sent;
	public boolean email_sent;
	public boolean tweet_sent;
	public boolean sms_sent;

	public boolean sms_checked;
	public boolean email_checked;
	public boolean tweet_checked;
	public boolean youtube_checked;
}
