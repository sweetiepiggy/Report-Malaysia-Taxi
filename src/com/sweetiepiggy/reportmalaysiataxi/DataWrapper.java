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

import android.net.Uri;
import android.app.Activity;

public class DataWrapper
{
	/* TODO: RESULTs should be in a different class */
	static final int RESULT_SET_ENGLISH = Activity.RESULT_FIRST_USER;
	static final int RESULT_SET_CHINESE = Activity.RESULT_FIRST_USER + 1;
	static final int RESULT_SET_MALAY = Activity.RESULT_FIRST_USER + 2;

	/* TODO: Lang_t should be in a different class */
	public enum Lang_t {
		LANG_DEFAULT, LANG_ENGLISH, LANG_CHINESE, LANG_MALAY
	}

	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public String offence;
	public String offenceMalay;

	public boolean[] selected;

	public Lang_t lang = DataWrapper.Lang_t.LANG_DEFAULT;

	public ArrayList<Uri> photoUris;
	public ArrayList<Uri> recordingUris;
	public ArrayList<Uri> videoUris;

	public boolean youtube_sent;
	public boolean email_sent;
	public boolean tweet_sent;
	public boolean sms_sent;
}

