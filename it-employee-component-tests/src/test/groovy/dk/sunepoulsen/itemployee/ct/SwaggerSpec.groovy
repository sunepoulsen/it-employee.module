package dk.sunepoulsen.itemployee.ct

import dk.sunepoulsen.tes.http.HttpHelper
import dk.sunepoulsen.tes.http.HttpResponseVerificator
import spock.lang.Specification

import java.net.http.HttpRequest

class SwaggerSpec extends Specification {

    void "GET /swagger-ui.html returns OK"() {
        given: 'Holidays service is available'
            DeploymentSpockExtension.itEmployeeModuleContainer().isHostAccessible()
            String baseUrl = "http://${DeploymentSpockExtension.itEmployeeModuleContainer().host}:${DeploymentSpockExtension.itEmployeeModuleContainer().getMappedPort(8080)}"

        when: 'Call GET /swagger-ui.html'
            HttpHelper httpHelper = new HttpHelper()
            HttpRequest httpRequest = httpHelper.newRequestBuilder(DeploymentSpockExtension.itEmployeeModuleContainer(),"/swagger-ui.html")
                .GET()
                .build()

            HttpResponseVerificator verificator = httpHelper.sendRequest(httpRequest)

        then: 'Response Code is 200'
            verificator.responseCode(200)

        and: 'Content Type is text/html'
            verificator.contentType('text/html')
    }
}
