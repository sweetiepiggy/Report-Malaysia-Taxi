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

public class DataWrapper {
    public ArrayList<Uri> attachmentUris;
    public int day;
    public boolean email_checked;
    public String email_offence;
    public boolean email_sent;
    public int hour;
    public int minute;

    public int month;
    public String offence;

    public ArrayList<Uri> photoUris;
    public ArrayList<Uri> recordingUris;
    public boolean sms_checked;
    public boolean sms_sent;

    public boolean[] submit_selected;
    public boolean tweet_checked;
    public boolean tweet_sent;
    public ArrayList<Uri> videoUris;

    public boolean[] who_selected;
    public int year;
    public boolean youtube_checked;
    public boolean youtube_sent;
}
