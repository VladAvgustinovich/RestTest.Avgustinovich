import com.sun.net.httpserver.Request;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class Specification {

    public static RequestSpecification requestSpecification(String url) {
        return new RequestSpecBuilder()
                .setBaseUri(url)
                .build();
    }
    public static void installSpecification(RequestSpecification requestSpecification) {
        RestAssured.requestSpecification = requestSpecification;
    }
}
