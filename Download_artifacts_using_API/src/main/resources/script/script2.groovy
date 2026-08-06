import com.sap.gateway.ip.core.customdev.util.Message
import java.io.StringWriter

Message processData(Message message) {

    String body = message.getBody(String)

    def xml = new XmlSlurper(false, false).parseText(body)

    // Extract namespaces dynamically
    def nsAtom = xml.lookupNamespace(null)     // default ns (Atom)
    def nsm = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata"
    def nsd = "http://schemas.microsoft.com/ado/2007/08/dataservices"

    // Apply namespace prefixes to slurper
    xml.declareNamespace(a: nsAtom, m: nsm, d: nsd)

    def results = []

    // Read <entry> elements
    xml.'a:entry'.each { entry ->

        def id = entry.'m:properties'.'d:Id'.text()
        def version = entry.'m:properties'.'d:Version'.text()

        results << [Id: id, Version: version]
    }

    // Build output XML
    def sw = new StringWriter()
    sw.append("<Artifacts>")

    results.each { r ->
        sw.append("<Artifact>")
        sw.append("<Id>${r.Id}</Id>")
        sw.append("<Version>${r.Version}</Version>")
        sw.append("</Artifact>")
    }

    sw.append("</Artifacts>")

    message.setBody(sw.toString())
    return message
}
