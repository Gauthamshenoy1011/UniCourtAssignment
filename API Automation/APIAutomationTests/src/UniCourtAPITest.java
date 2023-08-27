import org.apache.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UniCourtAPITest {

    final static String url = "https://enterpriseapi.unicourt.com/caseSearch?q=caseName%3AGoogle&sort=filedDate&order=desc&pageNumber=1";
    final static String jwtToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl9pZCI6IlRLSURLZTY0OGIwM2JlQ0pzMiIsImNsaWVudF9pZCI6IllPSDFXSEc1NlZWVUI3R2wzYTdYcW04QjZXQ2h3d3pjIiwidWNhX2FjY291bnRfaWQiOiJwMTU1NjQyNzI3MyIsImlzX2ludGVybmFsIjp0cnVlLCJhdWQiOiJodHRwczovL2VudGVycHJpc2VhcGkudW5pY291cnQuY29tIiwiaXNzIjoiaHR0cHM6Ly91bmljb3VydC5jb20iLCJzdWIiOiJFTlRFUlBSSVNFX0FQSV9DUkVERU5USUFMUyIsImd0eSI6IkNsaWVudF9DcmVkZW50YWlscyIsImlhdCI6MTY4NjgzMjA2MiwiZXhwIjo0ODQyNTkyMDYyfQ.OIoL_mD7dbrElzsPPtg09N--gngxx1IIVEzM22NYI8wgOUBr1_255oJIEeZ_r63HcWrs-T6nVq_jaKBEyIlzSpUckApwo-oN0R1XSdCubFR6BuB-plA6A02KDKwkLO5IDJu6izmRPRriLU0z6uk-B0qGs-3zuhbFw-gSqeViigXCIAbKwnyX9WPLFtedRCND2JqKIVq8viLnq8Qo0eLHWqURsN6yJOHjlec2QbCFTXPS4E6xoEHB5jmycl-XegOuXUJDlGSrtiNyxH1h3ksxRWO5yx7ovDCJIyJddoXlTOeP-gw-9tHT-eoSAuv2DcdrLOuG2Ww0dpqmZzYG6qozUQ";
    final static String textToBeAsserted = "Google";
    Response response;

    @Test
    public void callTheAPI() {
        System.out.println("Calling the API");
        response = RestAssured.given().auth().oauth2(jwtToken).get(url).andReturn();
    }

    @Test(dependsOnMethods = "callTheAPI")
    public void validateTheStatusCode() {
        System.out.println("Validating API response code");
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        System.out.println("Response code is matching");
    }


    @Test(dependsOnMethods = "validateTheStatusCode")
    public void validateIfTheGoogleIsPresentInTheResponse() {
        System.out.println("Validating if google is available in the response");
        System.out.println(response.asString());
        JsonPath jsonAPIResponse = new JsonPath(response.asString());

        //Get Each JSON Array Value through array size
        int totalNoOfCaseResults = jsonAPIResponse.getInt("caseSearchResultArray.size()");

        for (int i = 0; i < totalNoOfCaseResults; i++) {
            String allCaseNameResults = jsonAPIResponse.getString("caseSearchResultArray[" + i + "].caseName");

            //Assert if Google is present in each JSON
            if (allCaseNameResults.toLowerCase().contains(textToBeAsserted.toLowerCase())) {
                System.out.println("Google text is present in API Result . Search Result: " + allCaseNameResults + " is shown in Page No 1 ");
            } else {
                Assert.fail("Google text is not present in API Result . Search Result: " + allCaseNameResults + " is shown in Page No 1 ");
            }
        }
        System.out.println("Google is available in the response");
    }
}


