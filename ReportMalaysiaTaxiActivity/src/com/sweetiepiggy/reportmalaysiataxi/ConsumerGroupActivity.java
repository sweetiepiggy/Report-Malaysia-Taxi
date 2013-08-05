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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] gov_depts = new String[] {
                this.getResources().getString(R.string.transit),
                this.getResources().getString(R.string.nccc_desc), };
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gov_depts));

        final ListView lv = this.getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int pos, final long id) {
                final CharSequence item = ((TextView) view).getText();
                if (item.equals(ConsumerGroupActivity.this.getResources()
                        .getString(R.string.transit))) {
                    final Intent intent = new Intent(ConsumerGroupActivity.this
                            .getApplicationContext(), ContactViewActivity.class);
                    final Bundle b = new Bundle();

                    b.putString("name", ConsumerGroupActivity.this
                            .getResources().getString(R.string.transit));
                    b.putString("desc", ConsumerGroupActivity.this
                            .getResources().getString(R.string.transit_desc));
                    b.putString("email", Constants.TRANSIT_EMAIL);
                    b.putString("website", Constants.TRANSIT_WEBSITE);
                    b.putString("twitter", Constants.TRANSIT_TWITTER);

                    intent.putExtras(b);
                    ConsumerGroupActivity.this.startActivity(intent);

                } else if (item.equals(ConsumerGroupActivity.this
                        .getResources().getString(R.string.nccc_desc))) {
                    final Intent intent = new Intent(ConsumerGroupActivity.this
                            .getApplicationContext(), ContactViewActivity.class);
                    final Bundle b = new Bundle();

                    b.putString("name", ConsumerGroupActivity.this
                            .getResources().getString(R.string.nccc));
                    b.putString("desc", ConsumerGroupActivity.this
                            .getResources().getString(R.string.nccc_desc));
                    b.putString("phone", Constants.NCCC_PHONE);
                    b.putString("email", Constants.NCCC_EMAIL);
                    b.putString("website", Constants.NCCC_WEBSITE);
                    b.putString("form", Constants.NCCC_FORM);
                    b.putString("twitter", Constants.NCCC_TWITTER);

                    intent.putExtras(b);
                    ConsumerGroupActivity.this.startActivity(intent);
                }
            }
        });

    }
}
