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
			getResources().getString(R.string.jpj),
			getResources().getString(R.string.kpdnkk),
			getResources().getString(R.string.motour),
			getResources().getString(R.string.pcb),
			getResources().getString(R.string.pemudah),
			getResources().getString(R.string.ttpm),
		};

		gov_descs = new String[] {
			getResources().getString(R.string.spad_desc),
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
				CharSequence item = gov_depts[(int) id];
				/* TODO: refactor duplicate Intent and Bundle code */
				if (item.equals(getResources().getString(R.string.spad))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.spad));
					b.putString("desc", getResources().getString(R.string.spad_desc));
					b.putString("email", Constants.SPAD_EMAIL);
					b.putString("website", getResources().getString(R.string.spad_website));
					b.putString("form", getResources().getString(R.string.spad_form));
					b.putString("twitter", Constants.SPAD_TWITTER);
					b.putString("sms", Constants.SPAD_SMS);
					b.putString("phone", Constants.SPAD_PHONE);

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.jpj))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.jpj));
					b.putString("desc", getResources().getString(R.string.jpj_desc));
					b.putString("email", Constants.JPJ_EMAIL);
					b.putString("website", Constants.JPJ_WEBSITE);
					b.putString("phone", Constants.JPJ_PHONE);
					b.putString("twitter", Constants.JPJ_TWITTER);

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.kpdnkk))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.kpdnkk));
					b.putString("desc", getResources().getString(R.string.kpdnkk_desc));
					b.putString("email", Constants.KPDNKK_EMAIL);
					b.putString("website", Constants.KPDNKK_WEBSITE);

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.motour))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.motour));
					b.putString("email", Constants.MOTOUR_EMAIL);
					b.putString("website", Constants.MOTOUR_WEBSITE);
					b.putString("form", getResources().getString(R.string.motour_form));
					b.putString("phone", Constants.MOTOUR_PHONE);

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.pcb))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.pcb));
					b.putString("phone", Constants.PCB_PHONE);
					b.putString("email", Constants.PCB_EMAIL);
					b.putString("website", Constants.PCB_WEBSITE);
					b.putString("form", getResources().getString(R.string.pcb_form));

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.pemudah))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.pemudah));
					b.putString("desc", getResources().getString(R.string.pemudah_desc));
					b.putString("phone", Constants.PEMUDAH_PHONE);
					b.putString("email", Constants.PEMUDAH_EMAIL);
					b.putString("website", Constants.PEMUDAH_WEBSITE);
					b.putString("form", getResources().getString(R.string.pemudah_form));

					intent.putExtras(b);
					startActivity(intent);

				} else if (item.equals(getResources().getString(R.string.ttpm))) {
					Intent intent = new Intent(getApplicationContext(), ContactViewActivity.class);
					Bundle b = new Bundle();

					b.putString("name", getResources().getString(R.string.ttpm));
					b.putString("phone", Constants.TTPM_PHONE);
					b.putString("website", Constants.TTPM_WEBSITE);

					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});

	}
}


