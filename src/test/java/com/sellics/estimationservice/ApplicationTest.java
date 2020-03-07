package com.sellics.estimationservice;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTest {

    @BeforeTestClass
    public void beforeClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterTestClass
    public void afterClass() {
        RestAssured.reset();
    }
}
