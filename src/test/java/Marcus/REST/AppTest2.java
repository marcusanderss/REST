package Marcus.REST;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.opentest4j.AssertionFailedError;

class AppTest2 {

	@Test
	void test() {

		Throwable x = Assertions.assertThrows(AssertionFailedError.class, () -> {
			Assertions.assertEquals(10, 20);
		});
		//System.out.println("hohohoho " + x.getMessage());
	}

	@Test
	public void test_NumberOfCircuitsFor2017Season_ShouldBe20() {

		given()
		.when()
		.get("http://ergast.com/api/f1/2017/circuits.json")
		.then()
		.assertThat()
		.body("MRData.CircuitTable.Circuits.circuitId", hasSize(20));
	}

	@Test
	public void test_Md5CheckSumForTest_ShouldBe098f6bcd4621d373cade4e832627b4f6() {

		String originalText = "test";
		String expectedMd5CheckSum = "098f6bcd4621d373cade4e832627b4f6";

		given()
		.param("text", originalText)
		.when()
		.get("http://md5.jsontest.com")
		.then()
		.assertThat()
		.body("md5",equalTo(expectedMd5CheckSum));
	}

	@Test
	public void test_NumberOfCircuits_ShouldBe20_Parameterized() {

		String season = "2017";
		int numberOfRaces = 20;

		given()
		.pathParam("raceSeason", season)
		.when()
		.get("http://ergast.com/api/f1/{raceSeason}/circuits.json")
		.then()
		.assertThat()
		.body("MRData.CircuitTable.Circuits.circuitId", hasSize(numberOfRaces));
	}

	private static Stream<Arguments> seasonsAndNumberOfRaces() {
		return Stream.of(Arguments.of("2017", 20), Arguments.of("2016", 21), Arguments.of("1966", 9));
	}

	@ParameterizedTest
	@MethodSource("seasonsAndNumberOfRaces")
	public void test_NumberOfCircuits_ShouldBe_DataDriven(String season, int numberOfRaces) {

		given()
		.pathParam("raceSeason", season)
		.when()
		.get("http://ergast.com/api/f1/{raceSeason}/circuits.json")
		.then()
		.assertThat()
		.body("MRData.CircuitTable.Circuits.circuitId", hasSize(numberOfRaces));
	}

	@Test
	public void test_ScenarioRetrieveFirstCircuitFor2017SeasonAndGetCountry_ShouldBeAustralia() {
	        
	    // First, retrieve the circuit ID for the first circuit of the 2017 season
	    String circuitId = given().
	    when().
	        get("http://ergast.com/api/f1/2017/circuits.json").
	    then().
	        extract().
	        path("MRData.CircuitTable.Circuits.circuitId[0]");
	        
	    // Then, retrieve the information known for that circuit and verify it is located in Australia
	    String aResponseString = given().
	        pathParam("circuitId",circuitId).
	    when().
	        get("http://ergast.com/api/f1/circuits/{circuitId}.json").
	    then().
	        assertThat().
	        body("MRData.CircuitTable.Circuits.Location[0].country",equalTo("Australia")).extract().asString();
	    
	 System.out.println(aResponseString);
}}
