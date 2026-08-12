import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {

    // Get JSON payload as String
    def body = message.getBody(String)

    // Parse JSON
    def json = new JsonSlurper().parseText(body)

    // Start building XML
    def xmlBuilder = new StringBuilder()
    xmlBuilder.append('<?xml version="1.0" encoding="UTF-8"?>\n')
    xmlBuilder.append('<Artifacts>\n')

    // Loop through each artifact
    json.d.results.each { artifact ->
        xmlBuilder.append('    <Artifact>\n')
        xmlBuilder.append("        <Id>${artifact.Id}</Id>\n")
        xmlBuilder.append("        <Version>${artifact.Version}</Version>\n")
        xmlBuilder.append('    </Artifact>\n')
    }

    xmlBuilder.append('</Artifacts>')

    // Set XML as message body
    message.setBody(xmlBuilder.toString())
    message.setHeader("Content-Type", "application/xml")

    return message
}
