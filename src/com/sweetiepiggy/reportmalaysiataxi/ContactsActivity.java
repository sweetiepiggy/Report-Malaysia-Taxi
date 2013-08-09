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
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, this.getResources()
						.getStringArray(R.array.contact_choices)));

		final ListView lv = this.getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int pos, final long id) {
				final String[] contact_choices = ContactsActivity.this
						.getResources().getStringArray(R.array.contact_choices);

				final CharSequence item = ((TextView) view).getText();

				/* TODO: don't hard code contact_choices order */
				/*
				 * TODO: combine all below activities into one activity and pass
				 * the appropriate info to it
				 */
				if (item.equals(contact_choices[0])) {
					final Intent intent = new Intent(ContactsActivity.this
							.getApplicationContext(), GovDeptActivity.class);
					ContactsActivity.this.startActivity(intent);

				} else if (item.equals(contact_choices[1])) {
					final Intent intent = new Intent(ContactsActivity.this
							.getApplicationContext(),
							ConsumerGroupActivity.class);
					ContactsActivity.this.startActivity(intent);

				} else if (item.equals(contact_choices[2])) {
					final Intent intent = new Intent(ContactsActivity.this
							.getApplicationContext(), GovMinisterActivity.class);
					ContactsActivity.this.startActivity(intent);

				} else if (item.equals(contact_choices[3])) {
					final Intent intent = new Intent(ContactsActivity.this
							.getApplicationContext(), TextViewActivity.class);
					final Bundle b = new Bundle();
					b.putString("text", ContactsActivity.this.getResources()
							.getString(R.string.news_media_contact));
					intent.putExtras(b);
					ContactsActivity.this.startActivity(intent);

				} else if (item.equals(contact_choices[4])) {
					final Intent intent = new Intent(ContactsActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", ContactsActivity.this.getResources()
							.getString(R.string.traffic_police));
					b.putString("email", Constants.TRAFFIC_POLICE_EMAIL);
					b.putString("website", Constants.TRAFFIC_POLICE_WEBSITE);

					intent.putExtras(b);
					ContactsActivity.this.startActivity(intent);
				}
			}
		});

	}
}
