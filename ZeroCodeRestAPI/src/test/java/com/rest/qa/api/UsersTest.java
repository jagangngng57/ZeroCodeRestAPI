package com.rest.qa.api;

import org.jsmart.zerocode.core.domain.Scenario;
import org.jsmart.zerocode.core.domain.TargetEnv;
import org.jsmart.zerocode.core.runner.ZeroCodeUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@TargetEnv("reqres_application.properties")
@RunWith(ZeroCodeUnitRunner.class)
public class UsersTest {

    @Test
    @Scenario("reqres/tests/create-user-test.json")
    public void createUserTest() {
    }

    @Test
    @Scenario("reqres/tests/get-single-user-test.json")
    public void getSingleUserTest() {
    }

    @Test
    @Scenario("reqres/tests/get-list-user-test.json")
    public void getListUserTest() {
    }

    @Test
    @Scenario("reqres/tests/update-user-test.json")
    public void updateUserTest() {
    }

    @Test
    @Scenario("reqres/tests/delete-user-test.json")
    public void deleteUserTest() {
    }

    @Test
    @Scenario("reqres/tests/get-delayed-response-test.json")
    public void getDelayedResponse() {
    }
}