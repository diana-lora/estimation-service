package com.sellics.estimationservice.web;

import com.sellics.estimationservice.ApplicationTest;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class SearchVolumeControllerTest extends ApplicationTest {

    private static ValidatableResponse estimate(String keyword) {
        return RestAssured.given()
            .log().all()
            .contentType(MediaType.APPLICATION_JSON.toString())
            .queryParam("keyword", keyword)
            .when()
            .get("/estimate")

            .then()
            .log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.APPLICATION_JSON.toString())
            .body("keyword", Matchers.equalTo(keyword));
    }

    @Test
    void getKeywordEstimation_iphone() {
        estimate("iphone").body("score", Matchers.equalTo(97));
    }

    @Test
    void getKeywordEstimation_charger() {
        estimate("charger").body("score", Matchers.equalTo(23));
    }

    @Test
    void getKeywordEstimation_iphoneCharger() {
        estimate("iphone charger").body("score", Matchers.equalTo(60));
    }
}