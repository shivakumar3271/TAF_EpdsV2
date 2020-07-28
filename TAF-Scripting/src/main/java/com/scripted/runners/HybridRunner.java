package com.scripted.runners;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.scripted.configurations.SkriptmateConfigurations;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

	@CucumberOptions(features = "Features/AllTech", plugin = { "json:target/cucumber.json"}, glue = {
					"com.scripted.stepdefs" }, monochrome = true, tags = { "@abc" })
	public class HybridRunner  extends AbstractTestNGCucumberTests {
		@BeforeSuite
		public void setup() {
			SkriptmateConfigurations.beforeSuite();
		}

		@AfterSuite
		public void teardown() {
			SkriptmateConfigurations.afterSuite();
		}
}
