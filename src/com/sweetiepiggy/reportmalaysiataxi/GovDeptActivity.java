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

public class GovDeptActivity extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] gov_depts = new String[] {
			getResources().getString(R.string.spad),
			getResources().getString(R.string.lpkp),
			getResources().getString(R.string.jpj),
			getResources().getString(R.string.kpdnkk),
			getResources().getString(R.string.motour),
			getResources().getString(R.string.pcb),
			getResources().getString(R.string.pemudah),
			getResources().getString(R.string.ttpm),
		};
		setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					gov_depts));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				CharSequence item = ((TextView) view).getText();
				/* TODO: refactor duplicate Intent and Bundle code */
				if (item.equals(getResources().getString(R.string.spad))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.spad_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.lpkp))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.lpkp_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.jpj))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.jpj_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.kpdnkk))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.kpdnkk_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.motour))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.motour_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.pcb))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.pcb_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.pemudah))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.pemudah_contact));
					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.ttpm))) {
					Intent intent = new Intent(getApplicationContext(), TextViewActivity.class);
					Bundle b = new Bundle();
					b.putString("text", getResources().getString(R.string.ttpm_contact));
					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}


