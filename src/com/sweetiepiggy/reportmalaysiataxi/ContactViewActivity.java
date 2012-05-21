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

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactViewActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_view_layout);

		Bundle b = getIntent().getExtras();
		if (b == null) {
			b = new Bundle();
		}

		String name = b.getString("name");
		String desc = b.getString("desc");
		String sms = b.getString("sms");
		String phone = b.getString("phone");
		String email = b.getString("email");
		String website = b.getString("website");
		String form = b.getString("form");
		String twitter = b.getString("twitter");

		TextView name_v = (TextView) findViewById(R.id.name);
		TextView desc_v = (TextView) findViewById(R.id.desc);
		TextView sms_v = (TextView) findViewById(R.id.sms);
		TextView phone_v = (TextView) findViewById(R.id.phone);
		TextView email_v = (TextView) findViewById(R.id.email);
		TextView website_v = (TextView) findViewById(R.id.website);
		TextView form_v = (TextView) findViewById(R.id.form);
		TextView twitter_v = (TextView) findViewById(R.id.twitter);

		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

		if (name == null) {
			layout.removeView(name_v);
		} else {
			name_v.setText(name);
		}

		if (desc == null) {
			layout.removeView(desc_v);
		} else {
			desc_v.setText(desc);
		}

		if (sms == null) {
			layout.removeView(sms_v);
		} else {
			sms_v.setText(sms);
		}

		if (phone == null) {
			layout.removeView(phone_v);
		} else {
			phone_v.setText(phone);
		}

		if (email == null) {
			layout.removeView(email_v);
		} else {
			email_v.setText(email);
		}

		if (website == null) {
			layout.removeView(website_v);
		} else {
			website_v.setText(website);
		}

		if (form == null) {
			layout.removeView(form_v);
		} else {
			form_v.setText(form);
		}

		if (twitter == null) {
			layout.removeView(twitter_v);
		} else {
			twitter_v.setText(twitter);
		}
	}

}

