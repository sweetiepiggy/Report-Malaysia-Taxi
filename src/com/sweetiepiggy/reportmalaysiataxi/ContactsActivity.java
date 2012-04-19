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

public class ContactsActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(R.array.contact_choices)));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] contact_choices = getResources().getStringArray(R.array.contact_choices);

				CharSequence item = ((TextView) view).getText();

				/* TODO: don't hard code contact_choices order */
				/* TODO: combine all below activities into one activity and pass the appropriate info to it */
				if (item.equals(contact_choices[0])) {
					Intent intent = new Intent(getApplicationContext(), GovDeptActivity.class);
					startActivity(intent);

				} else if (item.equals(contact_choices[1])) {
					Intent intent = new Intent(getApplicationContext(), ConsumerGroupActivity.class);
					startActivity(intent);

				} else if (item.equals(contact_choices[2])) {
					Intent intent = new Intent(getApplicationContext(), GovMinisterActivity.class);
					startActivity(intent);

				} else if (item.equals(contact_choices[3])) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.news_media_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(contact_choices[4])) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.traffic_police_contact));
					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}

