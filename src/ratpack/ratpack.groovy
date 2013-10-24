import postalcode.service.GeonamesPostalCodeService
import postalcode.service.PostalCodeService

import java.util.concurrent.TimeUnit

import static org.ratpackframework.groovy.RatpackScript.ratpack
import static org.ratpackframework.groovy.Template.groovyTemplate
import org.ratpackframework.jackson.JacksonModule
import static org.ratpackframework.jackson.Json.json


PostalCodeService service = new GeonamesPostalCodeService()

ratpack {

    modules {
        register new JacksonModule()
    }

    handlers {
        get {
            render groovyTemplate("index.html", title: "US Zipcode Tools")
        }

        get("api/postalcode/count") {
            def future = service.getSortedPostalCodeCounts()
            def result = future.get(30, TimeUnit.SECONDS)
            render json(result)
        }

        get("api/postalcode/clear") {
            service.clear()
            render json(new HashMap())
        }
        
        assets "public"
    }
}
