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

import java.util.ArrayList;

import android.net.Uri;
import android.app.Activity;

public class Constants
{
	static final int RESULT_SET_ENGLISH = Activity.RESULT_FIRST_USER;
	static final int RESULT_SET_CHINESE = Activity.RESULT_FIRST_USER + 1;
	static final int RESULT_SET_MALAY = Activity.RESULT_FIRST_USER + 2;

	public enum Lang_t {
		LANG_DEFAULT, LANG_ENGLISH, LANG_CHINESE, LANG_MALAY
	}

	static final String COMPLAINT_EMAIL_MALAY = "Aduan Teksi";
	static final String COMPLAINT_MALAY = "ADUAN LPKP";
	static final String COMPLAINT_HASHTAG = "#aduanteksi";

	static final String LOCATION_MALAY = "Lokasi";
	static final String REGISTRATION_MALAY = "Nombor Teksi Pendaftaran";
	static final String OFFENCE_MALAY = "Kesalahan";
	static final String EMAIL_INTRO_MALAY = "Pihak Berkuasa yang berkenan,\n\n" +
		"Tujuan saya menulis email ini adalah kerana sesuatu kejadian " +
		"berlaku yang tidak menyenang hati. Perkara tersebut adalah " +
		"dicatat seperti dibawah. Sila ambil tindakan yang sepatutnya " +
		"terhadap aduan saya.\n\n" +
		"Sekian.\n";

	static final String[] OFFENCE_MALAY_ARRAY = {
		"Enggan menggunakan meter",
		"Enggan mengambil penumpang",
		"Enggan memberi baki tumpang",
		"Memandu dengan bahaya",
		"Menawarkan perkhidmatan yang menyalahi undang-undang",
		"Gangguan",
		"Merokok dalam teksi",
		"Other"
	};

	static final String[] LANGUAGES = {
		"English",
		"中文",
		"Bahasa Melayu"
	};

	static final String[] LANGUAGE_CODES = {
		"en",
		"zh",
		"ms"
	};

	static final String[] EMAIL_ADDRESSES = {
		"aduan@lpkp.gov.my; aduan@spad.gov.my; aduantrafik@jpj.gov.my; e-aduan@kpdnkk.gov.my; ",
		"klangvalley.transit@gmail.com; nccc@nccc.org.my; ",
		"menteri@mot.gov.my; yenyenng@motour.gov.my; najib@1malaysia.com.my; ",
		"editor@thestar.com.my; metro@thestar.com.my; mmnews@mmail.com.my; syedn@nst.com.my; letters@nst.com.my; streets@nst.com.my; letters@thesundaily.com, editor@malaysiakini.com.my; editor@themalaysianinsider.com; ",
		"rmp@rmp.gov.my; "
	};

	static final String[] TEL_NUMBERS = {
		"1800889600",
		"0388884244",
		"0378779000"
	};

	static final String SMS_NUMBER = "15888";

	static final String TWITTER_ADDRESS1 = "@aduanSPAD";
	static final String TWITTER_ADDRESS2 = "@transitmy";
	static final String TWITTER_ADDRESS3 = "@myAduan";
}

