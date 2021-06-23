package com.rest.qa.api;

import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@TargetEnv("reqres_application.properties")
@RunWith(ZeroCodeUnitRunner.class)
public class LoginTest {

	
	@Test
    @Scenario("reqres/tests/user-login-test.json")
    public void registerUserTest() {
    }
}
