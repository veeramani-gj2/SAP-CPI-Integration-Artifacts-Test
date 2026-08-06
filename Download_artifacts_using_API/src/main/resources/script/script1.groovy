import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder

def Message processData(Message message) {

    // 1. Get JSON body
    def body = message.getBody(String)

    // 2. Parse JSON
    def json = new JsonSlurper().parseText(body)

    // 3. Prepare Writer for XML
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)

    // 4. Build XML
    xml.Packages {
        json.d.results.each { pkg ->
            Package {
                Id(pkg.Id)
                Name(pkg.Name)
                Version(pkg.Version ?: "")
            }
        }
    }

    // 5. Set XML output to message body
    message.setBody(writer.toString())

    return message
}
