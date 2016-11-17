package pl.rauch.jakub.samples.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.Uri;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.inject.Inject;

/**
 * Configures all our Camel routes, components, endpoints and beans
 */
public class XmlTransformRoute extends RouteBuilder {

    private static int i = 1;

    @Inject
    @Uri("direct:start")
    private Endpoint inputEndpoint;

    @Override
    public void configure() {
        from(inputEndpoint).tracing("true")
                .convertBodyTo(Document.class)
                .split().xpath("/root/body/list/sublist/value")
                    .log(body().toString())
                    .process().body(Node.class, node -> node.setTextContent("" + i++))
                .end();
    }

}
