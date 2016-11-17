package pl.rauch.jakub.samples.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.cdi.Uri;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests conversion made by XmlTransformRoute
 */
@RunWith(CamelCdiRunner.class)
public class XmlTransformRouteTest {

    @Inject
    @Uri("direct:start")
    private ProducerTemplate template;

    @Inject
    private ModelCamelContext context;

    @Before
    public void enableTracing() {
        context.setTracing(true);
    }

    @Test
    public void transform_whenXmlWithEmptyNodes_thenXmlWithFilledNodes() {
        // given
        Object request = "" +
                "<root>\n" +
                "   <header/>\n" +
                "   <body>\n" +
                "       <pre>0</pre>\n" +
                "       <list>" +
                "           <sublist><value/><value/></sublist>" +
                "           <sublist><value/><value/></sublist>" +
                "       </list>\n" +
                "       <post>3</post>\n" +
                "   </body>\n" +
                "</root>";


        // when
        String result = template.requestBody(request, String.class);

        // then
        assertThat(result).isXmlEqualTo("" +
                "<root>\n" +
                "   <header/>\n" +
                "   <body>\n" +
                "       <pre>0</pre>\n" +
                "       <list>\n" +
                "           <sublist><value>1</value><value>2</value></sublist>\n" +
                "           <sublist><value>3</value><value>4</value></sublist>\n" +
                "       </list>\n" +
                "       <post>3</post>\n" +
                "   </body>\n" +
                "</root>");
    }
}
