import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

Message processData(Message message) {

    // Read JSON
    def body = message.getBody(String)
    def json = new JsonSlurper().parseText(body)

    // Prepare XML builder
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)

    builder.ValueMappingList {

        // Loop through each result
        json.d.results.each { item ->
            ValueMappingInfo {
                ArtifactId(item.Id)
                ArtifactVersion(item.Version)
            }
        }
    }

    // Set XML as output
    message.setBody(writer.toString())

    return message
}
