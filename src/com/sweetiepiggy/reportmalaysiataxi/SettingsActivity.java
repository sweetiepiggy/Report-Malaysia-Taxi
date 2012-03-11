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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingsActivity extends ListActivity {

	static final int ACTIVITY_SET_LANGUAGE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case ACTIVITY_SET_LANGUAGE:
			if (resultCode != RESULT_CANCELED) {
				setResult(resultCode, getIntent());
				finish();
			}
			break;
		}
	}

	private void init() {
		String[] settings = new String[] {
			getResources().getString(R.string.language),
			getResources().getString(R.string.about)
		};
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence item = ((TextView) view).getText();
				if (item.equals(getResources().getString(R.string.language))) {
					Intent language_intent = new Intent(getApplicationContext(), LanguageActivity.class);
					startActivityForResult(language_intent, ACTIVITY_SET_LANGUAGE);
				} else if (item.equals(getResources().getString(R.string.about))) {
					Intent about_intent = new Intent(getApplicationContext(), AboutActivity.class);
					startActivity(about_intent);
				}
			}
		});
	}
}

