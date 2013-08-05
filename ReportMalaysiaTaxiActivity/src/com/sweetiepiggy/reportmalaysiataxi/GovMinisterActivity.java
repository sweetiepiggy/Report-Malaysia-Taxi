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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] gov_ministers = new String[] {
                this.getResources().getString(R.string.spad_chairman),
                this.getResources().getString(R.string.mo_transport),
                this.getResources().getString(R.string.prime_minister), };
        this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, gov_ministers));

        final ListView lv = this.getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent,
                    final View view, final int pos, final long id) {
                final CharSequence item = ((TextView) view).getText();
                if (item.equals(GovMinisterActivity.this.getResources()
                        .getString(R.string.spad_chairman))) {
                    final Intent intent = new Intent(GovMinisterActivity.this
                            .getApplicationContext(), ContactViewActivity.class);
                    final Bundle b = new Bundle();

                    b.putString("name", Constants.SPAD_CHAIRMAN_NAME);
                    b.putString("desc", GovMinisterActivity.this.getResources()
                            .getString(R.string.spad_chairman));
                    b.putString("twitter", Constants.SPAD_CHAIRMAN_TWITTER);

                    intent.putExtras(b);
                    GovMinisterActivity.this.startActivity(intent);
                } else if (item.equals(GovMinisterActivity.this.getResources()
                        .getString(R.string.mo_transport))) {
                    final Intent intent = new Intent(GovMinisterActivity.this
                            .getApplicationContext(), ContactViewActivity.class);
                    final Bundle b = new Bundle();

                    b.putString("name", GovMinisterActivity.this.getResources()
                            .getString(R.string.mo_transport_name));
                    b.putString("desc", GovMinisterActivity.this.getResources()
                            .getString(R.string.mo_transport));
                    b.putString("email", Constants.MO_TRANSPORT_EMAIL);
                    b.putString("twitter", Constants.MO_TRANSPORT_TWITTER);

                    intent.putExtras(b);
                    GovMinisterActivity.this.startActivity(intent);

                } else if (item.equals(GovMinisterActivity.this.getResources()
                        .getString(R.string.prime_minister))) {
                    final Intent intent = new Intent(GovMinisterActivity.this
                            .getApplicationContext(), ContactViewActivity.class);
                    final Bundle b = new Bundle();

                    b.putString("name", Constants.PRIME_MINISTER_NAME);
                    b.putString("desc", GovMinisterActivity.this.getResources()
                            .getString(R.string.prime_minister));
                    b.putString("email", Constants.PRIME_MINISTER_EMAIL);
                    b.putString("twitter", Constants.PRIME_MINISTER_TWITTER);

                    intent.putExtras(b);
                    GovMinisterActivity.this.startActivity(intent);
                }
            }
        });

    }
}
