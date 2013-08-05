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

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactViewActivity extends Activity {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.contact_view_layout);
        
        Bundle b = this.getIntent().getExtras();
        if (b == null) {
            b = new Bundle();
        }
        
        final String name = b.getString("name");
        final String desc = b.getString("desc");
        final String sms = b.getString("sms");
        final String phone = b.getString("phone");
        final String email = b.getString("email");
        final String website = b.getString("website");
        final String form = b.getString("form");
        final String twitter = b.getString("twitter");
        
        final TextView name_v = (TextView) this.findViewById(R.id.name);
        final TextView desc_v = (TextView) this.findViewById(R.id.desc);
        final TextView sms_v = (TextView) this.findViewById(R.id.sms);
        final TextView phone_v = (TextView) this.findViewById(R.id.phone);
        final TextView email_v = (TextView) this.findViewById(R.id.email);
        final TextView website_v = (TextView) this.findViewById(R.id.website);
        final TextView form_v = (TextView) this.findViewById(R.id.form);
        final TextView twitter_v = (TextView) this.findViewById(R.id.twitter);
        
        final LinearLayout layout = (LinearLayout) this
                .findViewById(R.id.layout);
        
        if (name == null) {
            layout.removeView(name_v);
        } else {
            name_v.setText(name);
        }
        
        if (desc == null) {
            layout.removeView(desc_v);
        } else {
            desc_v.setText(desc);
        }
        
        if (sms == null) {
            layout.removeView(sms_v);
        } else {
            sms_v.setText(sms);
        }
        
        if (phone == null) {
            layout.removeView(phone_v);
        } else {
            phone_v.setText(phone);
        }
        
        if (email == null) {
            layout.removeView(email_v);
        } else {
            email_v.setText(email);
        }
        
        if (website == null) {
            layout.removeView(website_v);
        } else {
            website_v.setText(website);
        }
        
        if (form == null) {
            layout.removeView(form_v);
        } else {
            form_v.setText(form);
        }
        
        if (twitter == null) {
            layout.removeView(twitter_v);
        } else {
            twitter_v.setText(twitter);
        }
    }
    
}
