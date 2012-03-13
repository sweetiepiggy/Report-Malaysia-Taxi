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
					getResources().getStringArray(R.array.email_choices)));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				String[] email_choices = getResources().getStringArray(R.array.email_choices);

				CharSequence item = ((TextView) view).getText();

				/* TODO: don't hard code email_choices order */
				/* TODO: combine all below activities into one activity and pass the appropriate info to it */
				if (item.equals(email_choices[0])) {
					Intent gov_dept_intent = new Intent(getApplicationContext(), GovDeptActivity.class);
					startActivity(gov_dept_intent);

				} else if (item.equals(email_choices[1])) {
					Intent consumer_grp_intent = new Intent(getApplicationContext(), ConsumerGroupActivity.class);
					startActivity(consumer_grp_intent);

				} else if (item.equals(email_choices[2])) {
					Intent gov_minister_intent = new Intent(getApplicationContext(), GovMinisterActivity.class);
					startActivity(gov_minister_intent);

//				} else if (item.equals(email_choices[3])) {
//					Intent news_media_intent = new Intent(getApplicationContext(), NewsMediaActivity.class);
//					startActivity(news_media_intent);
//
//				} else if (item.equals(email_choices[4])) {
//					Intent traffic_police_intent = new Intent(getApplicationContext(), TrafficPoliceActivity.class);
//					startActivity(traffic_police_intent);
				}
			}
		});

	}
}

