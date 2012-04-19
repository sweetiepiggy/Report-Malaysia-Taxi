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

public class GovMinisterActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] gov_ministers = new String[] {
			getResources().getString(R.string.spad_chairman),
			getResources().getString(R.string.mo_transport),
			getResources().getString(R.string.mo_tourism),
			getResources().getString(R.string.prime_minister),
		};
		setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					gov_ministers));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				CharSequence item = ((TextView) view).getText();
				if (item.equals(getResources().getString(R.string.spad_chairman))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.spad_chairman_contact));
					intent.putExtras(b);
					startActivity(intent);
				} else if (item.equals(getResources().getString(R.string.mo_transport))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.motransport_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.mo_tourism))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.motourism_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.prime_minister))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.prime_minister_contact));
					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}



