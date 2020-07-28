package com.tests.epds;

import org.testng.annotations.AfterSuite;

import com.scripted.configurations.SkriptmateConfigurations;

public class SkriptmateAfterSuite {

	@AfterSuite
	public void aftertest() {
		SkriptmateConfigurations.afterSuite();
	}
}
