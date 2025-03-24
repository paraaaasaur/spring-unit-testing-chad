package com.herbivore.junitbasic;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

class ConditionalTest {

	@Test
	@Disabled("Don't run this test until JIRA #123 is resolved")
	void basicTest() {
		assert true;
	}

	@Test
	@EnabledOnOs({OS.WINDOWS, OS.SOLARIS})
	void testForWindowsAndSolarisOnly() {
		assert true;
	}

	@Test
	@EnabledOnOs(OS.MAC)
	void testForMacOnly() {
		assert true;
	}

	@Test
	@EnabledOnJre(JRE.JAVA_17)
	void testForOnlyJava17() {
		assert true;
	}

	@Test
	@EnabledForJreRange(min = JRE.JAVA_13, max = JRE.JAVA_19)
	void testForJre13To19() {
		System.out.println(Runtime.version().version());
		assert true;
	}

	@Test
	@EnabledIfEnvironmentVariable(named = "ENV", matches = "(?:DEV|UAT)")
	void testForDevOrUatEnvironment() {
		assert true;
	}

	@Test
	@EnabledIfSystemProperty(named = "PROP", matches = "CI_CD_DEPLOY")
	void testForSystemProperty() {
		assert false;
	}

}