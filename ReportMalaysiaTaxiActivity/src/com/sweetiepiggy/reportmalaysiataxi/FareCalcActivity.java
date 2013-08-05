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

public class FareCalcActivity extends ListActivity {

    private static final int KL_STARTING_FARE = 3;
    private static final int PENANG_STARTING_FARE = 4;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] resources = new String[] {
                this.getResources().getString(R.string.lk_jb_kt_m),
                this.getResources().getString(R.string.penang), };
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resources));

        final ListView lv = this.getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int pos, final long id) {
                final CharSequence item = ((TextView) view).getText();
                if (item.equals(FareCalcActivity.this.getResources().getString(
                        R.string.penang))) {
                    final Intent intent = new Intent(FareCalcActivity.this
                            .getApplicationContext(), AllFareCalcActivity.class);
                    final Bundle b = new Bundle();
                    b.putInt("starting_fare",
                            FareCalcActivity.PENANG_STARTING_FARE);
                    intent.putExtras(b);
                    FareCalcActivity.this.startActivity(intent);

                } else if (item.equals(FareCalcActivity.this.getResources()
                        .getString(R.string.lk_jb_kt_m))) {
                    final Intent intent = new Intent(FareCalcActivity.this
                            .getApplicationContext(), AllFareCalcActivity.class);
                    final Bundle b = new Bundle();
                    b.putInt("starting_fare", FareCalcActivity.KL_STARTING_FARE);
                    intent.putExtras(b);
                    FareCalcActivity.this.startActivity(intent);
                }
            }
        });

    }
}
