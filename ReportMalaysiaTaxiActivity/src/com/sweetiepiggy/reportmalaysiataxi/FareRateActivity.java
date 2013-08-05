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

public class FareRateActivity extends ListActivity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] resources = new String[] {
                this.getResources().getString(R.string.lk_jb_kt_m),
                this.getResources().getString(R.string.penang),
                this.getResources().getString(R.string.airports), };
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, resources));

        final ListView lv = this.getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int pos, final long id) {
                final CharSequence item = ((TextView) view).getText();
                /* TODO: refactor duplicate code */
                if (item.equals(FareRateActivity.this.getResources().getString(
                        R.string.penang))) {
                    final Intent intent = new Intent(FareRateActivity.this
                            .getApplicationContext(), TextViewActivity.class);
                    final Bundle b = new Bundle();
                    b.putString("text", FareRateActivity.this.getResources()
                            .getString(R.string.penang_fare_rate));
                    intent.putExtras(b);
                    FareRateActivity.this.startActivity(intent);

                } else if (item.equals(FareRateActivity.this.getResources()
                        .getString(R.string.lk_jb_kt_m))) {
                    final Intent intent = new Intent(FareRateActivity.this
                            .getApplicationContext(), TextViewActivity.class);
                    final Bundle b = new Bundle();
                    b.putString("text", FareRateActivity.this.getResources()
                            .getString(R.string.kl_fare_rate));
                    intent.putExtras(b);
                    FareRateActivity.this.startActivity(intent);

                } else if (item.equals(FareRateActivity.this.getResources()
                        .getString(R.string.airports))) {
                    final Intent intent = new Intent(FareRateActivity.this
                            .getApplicationContext(), TextViewActivity.class);
                    final Bundle b = new Bundle();
                    b.putString("text", FareRateActivity.this.getResources()
                            .getString(R.string.airports_fare_rate));
                    intent.putExtras(b);
                    FareRateActivity.this.startActivity(intent);
                }
            }
        });

    }
}
