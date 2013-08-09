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

public class ConsumerGroupActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] gov_depts = new String[] {
				getResources().getString(R.string.transit),
				getResources().getString(R.string.nccc_desc), };
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, gov_depts));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				CharSequence item = ((TextView) view).getText();
				if (item.equals(getResources().getString(R.string.transit))) {
					Intent intent = new Intent(getApplicationContext(),
							ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name",
							getResources().getString(R.string.transit));
					b.putString("desc",
							getResources().getString(R.string.transit_desc));
					b.putString("email", Constants.TRANSIT_EMAIL);
					b.putString("website", Constants.TRANSIT_WEBSITE);
					b.putString("twitter", Constants.TRANSIT_TWITTER);

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(
						R.string.nccc_desc))) {
					Intent intent = new Intent(getApplicationContext(),
							ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.nccc));
					b.putString("desc",
							getResources().getString(R.string.nccc_desc));
					b.putString("phone", Constants.NCCC_PHONE);
					b.putString("email", Constants.NCCC_EMAIL);
					b.putString("website", Constants.NCCC_WEBSITE);
					b.putString("form", Constants.NCCC_FORM);
					b.putString("twitter", Constants.NCCC_TWITTER);

					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}
