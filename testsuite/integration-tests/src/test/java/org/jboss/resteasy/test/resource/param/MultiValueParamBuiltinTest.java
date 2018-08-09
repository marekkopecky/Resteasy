package org.jboss.resteasy.test.resource.param;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.test.resource.param.resource.MultiValueParamBuiltinMyObject1;
import org.jboss.resteasy.test.resource.param.resource.MultiValueParamBuiltinMyObject2;
import org.jboss.resteasy.test.resource.param.resource.MultiValueParamBuiltinResource;
import org.jboss.resteasy.utils.PortProviderUtil;
import org.jboss.resteasy.utils.TestUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;


/**
 * @tpSubChapter Parameters
 * @tpChapter Integration tests
 * @tpTestCaseDetails Usage of builtin MultiValuedParamConverterProvider
 * @tpSince RESTEasy 3.6
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MultiValueParamBuiltinTest {

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(MultiValueParamBuiltinTest.class.getSimpleName());
        war.addClass(MultiValueParamBuiltinMyObject1.class);
        war.addClass(MultiValueParamBuiltinMyObject2.class);
        war.addClass(MultiValueParamBuiltinResource.class);
        war.addAsWebInfResource(MultiValueParamBuiltinTest.class.getPackage(), "multi-value-param-provided-web.xml", "web.xml");

        return war;
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, MultiValueParamBuiltinTest.class.getSimpleName());
    }


    static String value1 = "1+2";
    static String value2 = "3+4";
    static String value3 = "5+6";

    /**
     * @tpTestDetails Simple class test
     * @tpSince RESTEasy 3.6
     */
    @Test
    public void testQueryParamSimpleClass() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        try {
            Response queryParamResourceClient = client.target(generateURL("/queryParam/list1"))
                    .queryParam("q", value1 + "," + value2 + "," + value3).request().get();
            Assert.assertEquals("Wrong response", "1+2,3+4,5+6,", queryParamResourceClient.readEntity(String.class));
        } finally {
            client.close();
        }
    }

    /**
     * @tpTestDetails Complex class test
     * @tpSince RESTEasy 3.6
     */
    @Test
    public void testQueryParamComplexClass() throws Exception {
        ResteasyClient client = new ResteasyClientBuilder().build();
        try {
            Response queryParamResourceClient = client.target(generateURL("/queryParam/list2"))
                    .queryParam("q", value1 + "," + value2 + "," + value3).request().get();
            Assert.assertEquals("Wrong response", "1....2,3....4,5....6,", queryParamResourceClient.readEntity(String.class));
        } finally {
            client.close();
        }
    }


}
