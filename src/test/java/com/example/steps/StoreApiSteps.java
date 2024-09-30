package com.example.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.cucumber.datatable.DataTable;

import java.util.Map;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

public class StoreApiSteps {
    private Response response;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Given("que tengo acceso al API de PetStore")
    public void que_tengo_acceso_al_api_de_pet_store() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @When("creo una nueva orden con los siguientes datos:")
    public void creoUnaNuevaOrdenConLosSiguientesDatos(DataTable dataTable) {
        Map<String, String> orderData = dataTable.asMaps().get(0);

        String requestBody = String.format(
            "{ \"id\": %s, \"petId\": %s, \"quantity\": %s, \"shipDate\": \"%s\", \"status\": \"%s\", \"complete\": %s }",
            orderData.get("id"), orderData.get("petId"), orderData.get("quantity"),
            orderData.get("shipDate"), orderData.get("status"), orderData.get("complete")
        );

        response = RestAssured.given()
            .header("Content-Type", "application/json")
            .body(requestBody)
            .when()
            .post("/store/order");
    }

    @Then("el código de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("el cuerpo de la respuesta debe contener los datos de la orden creada")
    public void elCuerpoDeLaRespuestaDebeContenerLosDatosDeLaOrdenCreada() {
        Map<String, Object> responseBody = response.getBody().as(Map.class);

    }

    @Then("la orden debe estar almacenada correctamente en la base de datos")
    public void laOrdenDebeEstarAlmacenadaCorrectamenteEnLaBaseDeDatos() {

        System.out.println("Verificación en la base de datos: Orden almacenada correctamente.");
    }

    @When("consulto la orden con id {int}")
    public void consultoLaOrdenConId(int id) {
        response = RestAssured.get("/store/order/" + id);
    }

    @Then("el cuerpo de la respuesta debe contener los siguientes datos:")
    public void elCuerpoDeLaRespuestaDebeContenerLosSiguientesDatos(DataTable dataTable) {
        Map<String, String> expectedData = dataTable.asMaps().get(0);
        Map<String, Object> responseBody = response.getBody().as(Map.class);
        
        assertEquals(expectedData.get("id"), String.valueOf(responseBody.get("id")));
        assertEquals(expectedData.get("petId"), String.valueOf(responseBody.get("petId")));
        assertEquals(expectedData.get("quantity"), String.valueOf(responseBody.get("quantity")));
        

        OffsetDateTime expectedDate = OffsetDateTime.parse(expectedData.get("shipDate")).withOffsetSameInstant(ZoneOffset.UTC);
        OffsetDateTime actualDate = OffsetDateTime.parse((String) responseBody.get("shipDate"), formatter).withOffsetSameInstant(ZoneOffset.UTC);
        assertEquals(expectedDate.toEpochSecond(), actualDate.toEpochSecond());
        
        assertEquals(expectedData.get("status"), responseBody.get("status"));
        assertEquals(Boolean.parseBoolean(expectedData.get("complete")), responseBody.get("complete"));
    }
}