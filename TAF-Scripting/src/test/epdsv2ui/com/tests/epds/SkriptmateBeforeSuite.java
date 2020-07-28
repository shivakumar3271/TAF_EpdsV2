package com.tests.epds;

import org.testng.annotations.BeforeSuite;

import com.scripted.configurations.SkriptmateConfigurations;

public class SkriptmateBeforeSuite {

	@BeforeSuite
	public void beforetest() {
		SkriptmateConfigurations.beforeSuite();
	}
}
