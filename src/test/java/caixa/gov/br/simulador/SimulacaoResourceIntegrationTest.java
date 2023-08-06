package caixa.gov.br.simulador;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@QuarkusTest
public class SimulacaoResourceIntegrationTest {

    @Test // Teste de Integração RestAssured
    public void testSimular() {
        given()
                .contentType("application/json")
                .body("{\"valorDesejado\": 1000, \"prazo\": 12}")
                .when()
                .post("/v1/simulacao")
                .then()
                .statusCode(200)
                .body("codigoProduto", notNullValue())
                .body("descricaoProduto", notNullValue())
                .body("taxaJuros", notNullValue());
    }


}
