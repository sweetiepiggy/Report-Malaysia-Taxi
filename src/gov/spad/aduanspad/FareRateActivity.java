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

package gov.spad.aduanspad;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FareRateActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] resources = new String[] {
			getResources().getString(R.string.lk_jb_kt_m),
			getResources().getString(R.string.penang),
			getResources().getString(R.string.airports),
		};
		setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					resources));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				CharSequence item = ((TextView) view).getText();
				/* TODO: refactor duplicate code */
				if (item.equals(getResources().getString(R.string.penang))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.penang_fare_rate));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.lk_jb_kt_m))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.kl_fare_rate));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.airports))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.airports_fare_rate));
					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}

