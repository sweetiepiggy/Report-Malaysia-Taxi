/*
    Copyright (C) 2013 Sweetie Piggy Apps <sweetiepiggyapps@gmail.com>

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
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.gov_depts = new String[] {
				this.getResources().getString(R.string.spad),
				this.getResources().getString(R.string.jpj),
				this.getResources().getString(R.string.kpdnkk),
				this.getResources().getString(R.string.motour),
				this.getResources().getString(R.string.pcb),
				this.getResources().getString(R.string.pemudah),
				this.getResources().getString(R.string.ttpm), };

		this.gov_descs = new String[] {
				this.getResources().getString(R.string.spad_desc),
				this.getResources().getString(R.string.jpj_desc),
				this.getResources().getString(R.string.kpdnkk_desc),
				this.getResources().getString(R.string.motour_desc),
				this.getResources().getString(R.string.pcb_desc),
				this.getResources().getString(R.string.pemudah_desc),
				this.getResources().getString(R.string.ttpm_desc), };

		final MatrixCursor c = new MatrixCursor(new String[] { "_id", "name",
				"desc" });
		for (int i = 0; i < this.gov_depts.length; ++i) {
			c.addRow(new Object[] { i, this.gov_depts[i], this.gov_descs[i] });
		}

		this.setListAdapter(new SimpleCursorAdapter(this,
				android.R.layout.two_line_list_item, c, new String[] { "name",
						"desc" }, new int[] { android.R.id.text1,
						android.R.id.text2 }));

		final ListView lv = this.getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int pos, final long id) {
				final CharSequence item = GovDeptActivity.this.gov_depts[(int) id];
				/* TODO: refactor duplicate Intent and Bundle code */
				if (item.equals(GovDeptActivity.this.getResources().getString(
						R.string.spad))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.spad));
					b.putString("desc", GovDeptActivity.this.getResources()
							.getString(R.string.spad_desc));
					b.putString("email", Constants.SPAD_EMAIL);
					b.putString("website", GovDeptActivity.this.getResources()
							.getString(R.string.spad_website));
					b.putString("form", GovDeptActivity.this.getResources()
							.getString(R.string.spad_form));
					b.putString("twitter", Constants.SPAD_TWITTER);
					b.putString("sms", Constants.SPAD_SMS);
					b.putString("phone", Constants.SPAD_PHONE);

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.jpj))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.jpj));
					b.putString("desc", GovDeptActivity.this.getResources()
							.getString(R.string.jpj_desc));
					b.putString("email", Constants.JPJ_EMAIL);
					b.putString("website", Constants.JPJ_WEBSITE);
					b.putString("phone", Constants.JPJ_PHONE);
					b.putString("twitter", Constants.JPJ_TWITTER);

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.kpdnkk))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.kpdnkk));
					b.putString("desc", GovDeptActivity.this.getResources()
							.getString(R.string.kpdnkk_desc));
					b.putString("email", Constants.KPDNKK_EMAIL);
					b.putString("website", Constants.KPDNKK_WEBSITE);

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.motour))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.motour));
					b.putString("email", Constants.MOTOUR_EMAIL);
					b.putString("website", Constants.MOTOUR_WEBSITE);
					b.putString("form", GovDeptActivity.this.getResources()
							.getString(R.string.motour_form));
					b.putString("phone", Constants.MOTOUR_PHONE);

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.pcb))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.pcb));
					b.putString("phone", Constants.PCB_PHONE);
					b.putString("email", Constants.PCB_EMAIL);
					b.putString("website", Constants.PCB_WEBSITE);
					b.putString("form", GovDeptActivity.this.getResources()
							.getString(R.string.pcb_form));

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.pemudah))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.pemudah));
					b.putString("desc", GovDeptActivity.this.getResources()
							.getString(R.string.pemudah_desc));
					b.putString("phone", Constants.PEMUDAH_PHONE);
					b.putString("email", Constants.PEMUDAH_EMAIL);
					b.putString("website", Constants.PEMUDAH_WEBSITE);
					b.putString("form", GovDeptActivity.this.getResources()
							.getString(R.string.pemudah_form));

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);

				} else if (item.equals(GovDeptActivity.this.getResources()
						.getString(R.string.ttpm))) {
					final Intent intent = new Intent(GovDeptActivity.this
							.getApplicationContext(), ContactViewActivity.class);
					final Bundle b = new Bundle();

					b.putString("name", GovDeptActivity.this.getResources()
							.getString(R.string.ttpm));
					b.putString("phone", Constants.TTPM_PHONE);
					b.putString("website", Constants.TTPM_WEBSITE);

					intent.putExtras(b);
					GovDeptActivity.this.startActivity(intent);
				}
			}
		});

	}
}
