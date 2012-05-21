/*
    Copyright (C) 2012 Sweetie Piggy Apps <sweetiepiggyapps@gmail.com>

    This file is part of Aduan SPAD.

    Aduan SPAD is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    Aduan SPAD is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Aduan SPAD; if not, see <http://www.gnu.org/licenses/>.
*/

package com.sweetiepiggy.reportmalaysiataxi;

import java.util.ArrayList;

import android.net.Uri;

public class DataWrapper
{
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;

	public boolean[] submit_selected;

	public ArrayList<Uri> photoUris;
	public ArrayList<Uri> recordingUris;
	public ArrayList<Uri> videoUris;

	public boolean email_sent;
	public boolean tweet_sent;
	public boolean sms_sent;

	public String loc;
	public String reg;
	public String details;

	public boolean sms_checked;
	public boolean email_checked;
	public boolean tweet_checked;
}

