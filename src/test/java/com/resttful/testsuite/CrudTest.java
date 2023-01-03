package com.resttful.testsuite;

import com.resttful.model.BookingPojo;
import com.resttful.steps.BookingSteps;
import com.resttful.testbase.TestBase;
import com.resttful.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class CrudTest extends TestBase {

    static String firstname = "tushar" + TestUtils.getRandomValue();
    static String lastname = "vib" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static boolean depositpaid = true;
    static String additionalneeds = "Vegetarian";

    static String token;
    static int id;

    @Steps
   BookingSteps bookingSteps;

    @Title("This method will create a Token")
    @org.junit.Test
    public void test001() {
        ValidatableResponse response = bookingSteps.getToken().statusCode(200);
        token = response.extract().path("token");
    }

    @Title("This method will create a booking")
    @org.junit.Test
    public void test002() {
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds).statusCode(200);
        id = response.extract().path("bookingid");
    }

    @Title("This method will verify new Booking ID creation")
    @Test
    public void test003() {
        ValidatableResponse response = bookingSteps.getBookingInfoByID();
        ArrayList<?> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(id));
    }

    @Title("This method will get booking with Id")
    @org.junit.Test
    public void test004() {
        bookingSteps.getSingleBookingIDs(id).statusCode(200);
    }

    @Title("This method will updated a booking with ID")
    @Test
    public void test005() {
        additionalneeds = "no-onion/garlic";
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        bookingSteps.updateBookingWithID(id, token, firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        ValidatableResponse response = bookingSteps.getSingleBookingIDs(id);
        HashMap<String, ?> update = response.extract().path("");
        Assert.assertThat(update, hasValue("no-onion/garlic"));
    }

    @Title("This method will delete a booking with ID")
    @Test
    public void test006() {
        bookingSteps.deleteABookingID(id, token).statusCode(201);
        bookingSteps.getSingleBookingIDs(id).statusCode(404);
    }
}








