package net.sf.esfinge.querybuilder.controller;

import java.util.Locale;

public class Start {

	public static void main(String[] args) throws Exception {
		Locale site = new Locale("en", "US");
		Locale.setDefault(site);

		ApplicationController ac = new ApplicationController();
		ac.start();
	}

}
