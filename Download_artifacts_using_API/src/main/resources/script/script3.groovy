import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

Message processData(Message message) {

    def body = message.getBody(String)

    // Parse XML (no import needed in CPI)
    def xml = new XmlSlurper(false, false).parseText(body)

    // Declare namespaces
    xml.declareNamespace(
        atom: "http://www.w3.org/2005/Atom",
        m: "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata",
        d: "http://schemas.microsoft.com/ado/2007/08/dataservices"
    )

    // Prepare JSON list
    def artifactsList = []

    xml.'atom:entry'.each { entry ->
        def artifact = [
            Id        : entry.'m:properties'.'d:Id'.text(),
            Version   : entry.'m:properties'.'d:Version'.text(),
            PackageId : entry.'m:properties'.'d:PackageId'.text(),
            Name      : entry.'m:properties'.'d:Name'.text(),
            CreatedBy : entry.'m:properties'.'d:CreatedBy'.text(),
            CreatedAt : entry.'m:properties'.'d:CreatedAt'.text()
        ]
        artifactsList << artifact
    }

    // Wrap JSON
    def output = [ArtifactsList: artifactsList]

    // Convert to JSON string
    message.setBody(JsonOutput.toJson(output))
    return message
}
