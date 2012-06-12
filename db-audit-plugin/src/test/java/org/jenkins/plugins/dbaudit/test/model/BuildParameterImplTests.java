/**
 * 
 */
package org.jenkins.plugins.dbaudit.test.model;

import junit.framework.Assert;

import org.jenkins.plugins.dbaudit.internal.model.BuildParameterImpl;
import org.jenkins.plugins.dbaudit.model.BuildParameter;
import org.junit.Test;

/**
 * Unit tests for the {@link BuildParameterImpl} class.
 * 
 * @author Marco Scata
 *
 */
public class BuildParameterImplTests {
	private final BuildParameter expected = new BuildParameterImpl(
			Long.valueOf(123), "PARAM NAME", "PARAM VALUE", "BUILD ID");
	
	@Test
	public void differentAttributesShouldPreserveEquality(){
		final BuildParameter actual = new BuildParameterImpl(
				expected.getId() + 100, 
				expected.getName(),
				expected.getValue() + "DIFFERENT", 
				expected.getBuildId());
		Assert.assertEquals("Broken equality", expected, actual);
	}
	
	@Test
	public void differentNameShouldBreakEquality(){
		final BuildParameter actual = new BuildParameterImpl(
				expected.getId(), 
				expected.getName() + "DIFFERENT",
				expected.getValue(), 
				expected.getBuildId());
		Assert.assertFalse("Broken inequality logic", actual.equals(expected));
	}
	
	@Test
	public void differentBuildIdShouldBreakEquality(){
		final BuildParameter actual = new BuildParameterImpl(
				expected.getId(), 
				expected.getName(),
				expected.getValue(), 
				expected.getBuildId() + "DIFFERENT");
		Assert.assertFalse("Broken inequality logic", actual.equals(expected));
	}
	
	@Test
	public void equalsNullShouldBeFalse() {
		Assert.assertFalse("Broken inequality logic", expected.equals(null));
	}

	@Test
	public void equalsSomethingElseShouldBeFalse() {
		Assert.assertFalse("Broken inequality logic", expected.equals("SOMESTRING"));
	}
}
