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

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class AllFareCalcActivity extends Activity {

    private int m_starting_fare = 3;

    private double calc_fare_by_dist(final int starting_fare, double km,
            final int passengers, final boolean midnight, final boolean exec,
            final boolean telbooking) {
        double fare = starting_fare;
        km = km > 1 ? km - 1 : 0;
        fare += ((int) (km / 0.115)) * 0.1;
        if (midnight) {
            fare *= 1.5;
        }
        if (exec) {
            fare *= 2;
        }
        if (telbooking) {
            fare += 2;
        }
        if (passengers > 2) {
            fare += 1;
        }
        return fare;
    }

    private double calc_fare_by_time(final int starting_fare, int min,
            final int passengers, final boolean midnight, final boolean exec,
            final boolean telbooking) {
        double fare = starting_fare;
        min = min > 3 ? min - 3 : 0;
        fare += ((int) ((min * 60) / 21.)) * 0.1;
        if (midnight) {
            fare *= 1.5;
        }
        if (exec) {
            fare *= 2;
        }
        if (telbooking) {
            fare += 2;
        }
        if (passengers > 2) {
            fare += 1;
        }
        return fare;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fare_calc);

        final Bundle b = this.getIntent().getExtras();
        if ((b != null) && b.containsKey("starting_fare")) {
            this.m_starting_fare = b.getInt("starting_fare");
        }

        final EditText distance_entry = (EditText) this
                .findViewById(R.id.distance_entry);
        distance_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                AllFareCalcActivity.this.update_fare();
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start,
                    final int before, final int count) {
            }
        });

        final EditText time_entry = (EditText) this
                .findViewById(R.id.time_entry);
        time_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                AllFareCalcActivity.this.update_fare();
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start,
                    final int before, final int count) {
            }
        });

        final EditText passengers_entry = (EditText) this
                .findViewById(R.id.passengers_entry);
        passengers_entry.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                AllFareCalcActivity.this.update_fare();
            }

            @Override
            public void beforeTextChanged(final CharSequence s,
                    final int start, final int count, final int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, final int start,
                    final int before, final int count) {
            }
        });

        final CheckBox midnight_checkbox = (CheckBox) this
                .findViewById(R.id.midnight_checkbox);
        midnight_checkbox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            final CompoundButton buttonView,
                            final boolean isChecked) {
                        AllFareCalcActivity.this.update_fare();
                    }
                });

        final CheckBox executive_checkbox = (CheckBox) this
                .findViewById(R.id.executive_checkbox);
        executive_checkbox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            final CompoundButton buttonView,
                            final boolean isChecked) {
                        AllFareCalcActivity.this.update_fare();
                    }
                });

        final CheckBox telbooking_checkbox = (CheckBox) this
                .findViewById(R.id.telbooking_checkbox);
        telbooking_checkbox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(
                            final CompoundButton buttonView,
                            final boolean isChecked) {
                        AllFareCalcActivity.this.update_fare();
                    }
                });

        this.update_fare();
    }

    private void update_fare() {
        String distance_str = ((EditText) this
                .findViewById(R.id.distance_entry)).getText().toString();
        if (distance_str.length() == 0) {
            distance_str = "0";
        }
        final double km = Double.valueOf(distance_str);

        String time_str = ((EditText) this.findViewById(R.id.time_entry))
                .getText().toString();
        if (time_str.length() == 0) {
            time_str = "0";
        }
        final int min = Integer.parseInt(time_str);

        String passengers_str = ((EditText) this
                .findViewById(R.id.passengers_entry)).getText().toString();
        if (passengers_str.length() == 0) {
            passengers_str = "1";
        }
        final int passengers = Integer.parseInt(passengers_str);

        final boolean midnight = ((CheckBox) this
                .findViewById(R.id.midnight_checkbox)).isChecked();
        final boolean exec = ((CheckBox) this
                .findViewById(R.id.executive_checkbox)).isChecked();
        final boolean telbooking = ((CheckBox) this
                .findViewById(R.id.telbooking_checkbox)).isChecked();

        final double fare_by_dist = this.calc_fare_by_dist(
                this.m_starting_fare, km, passengers, midnight, exec,
                telbooking);
        final double fare_by_time = this.calc_fare_by_time(
                this.m_starting_fare, min, passengers, midnight, exec,
                telbooking);

        final double fare = fare_by_dist > fare_by_time ? fare_by_dist
                : fare_by_time;

        final EditText calc_fare_entry = (EditText) this
                .findViewById(R.id.calc_fare_entry);
        calc_fare_entry.setText(String.format("%.2f", fare));
    }

}
