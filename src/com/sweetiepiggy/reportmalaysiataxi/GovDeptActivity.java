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
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class GovDeptActivity extends ListActivity {

	/** names of government departments */
	private String[] gov_depts;

	/** descriptions of government departments */
	private String[] gov_descs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gov_depts = new String[] {
			getResources().getString(R.string.spad),
			getResources().getString(R.string.lpkp),
			getResources().getString(R.string.jpj),
			getResources().getString(R.string.kpdnkk),
			getResources().getString(R.string.motour),
			getResources().getString(R.string.pcb),
			getResources().getString(R.string.pemudah),
			getResources().getString(R.string.ttpm),
		};

		gov_descs = new String[] {
			getResources().getString(R.string.spad_desc),
			getResources().getString(R.string.lpkp_desc),
			getResources().getString(R.string.jpj_desc),
			getResources().getString(R.string.kpdnkk_desc),
			getResources().getString(R.string.motour_desc),
			getResources().getString(R.string.pcb_desc),
			getResources().getString(R.string.pemudah_desc),
			getResources().getString(R.string.ttpm_desc),
		};

		MatrixCursor c = new MatrixCursor(new String[] {"_id", "name", "desc"});
		for (int i=0; i < gov_depts.length; ++i) {
			c.addRow(new Object[] {i, gov_depts[i], gov_descs[i]});
		}

		setListAdapter(new SimpleCursorAdapter(this,
					android.R.layout.two_line_list_item,
					c, new String[] {"name", "desc"},
					new int[] {android.R.id.text1, android.R.id.text2}));

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
//				CharSequence item = ((TextView) view).getText();
				CharSequence item = gov_depts[pos];
				/* TODO: refactor duplicate Intent and Bundle code */
				if (item.equals(getResources().getString(R.string.spad))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.spad));
					b.putString("desc", getResources().getString(R.string.spad_desc));
					b.putString("email", Constants.SPAD_EMAIL);
					b.putString("website", Constants.SPAD_WEBSITE);
					b.putString("twitter", Constants.SPAD_TWITTER);
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


