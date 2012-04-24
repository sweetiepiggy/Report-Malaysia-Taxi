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

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ContactViewActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_view_layout);

		Bundle b = getIntent().getExtras();

		String name = (b == null) ? "" : b.getString("name");
		String desc = (b == null) ? "" : b.getString("desc");
		String email = (b == null) ? "" : b.getString("email");
		String website = (b == null) ? "" : b.getString("website");
		String twitter = (b == null) ? "" : b.getString("twitter");

		((TextView) findViewById(R.id.name)).setText(name);
		((TextView) findViewById(R.id.desc)).setText(desc);
		((TextView) findViewById(R.id.email)).setText(email);
		((TextView) findViewById(R.id.website)).setText(website);
		((TextView) findViewById(R.id.twitter)).setText(twitter);

	}

}

