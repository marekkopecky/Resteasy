package org.jboss.resteasy.test.providers.jaxb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.test.providers.jaxb.resource.PathParamJaxbModel;
import org.jboss.resteasy.test.providers.jaxb.resource.PathParamJaxbResource;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jboss.resteasy.utils.PortProviderUtil;
import org.jboss.resteasy.utils.TestUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.extras.creaper.core.online.OnlineManagementClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;


@RunWith(Arquillian.class)
@RunAsClient
public class PathParamJaxbTest {

    static ResteasyClient client;

    private static PathParamJaxbModel model;

    @BeforeClass
    public static void prepareModel() {
        model = new PathParamJaxbModel();
        model.setA(1);
        model.setB(2);
    }

    @BeforeClass
    public static void clientInit() {
        client = new ResteasyClientBuilder().build();
    }

    @AfterClass
    public static void clientClose() {
        client.close();
    }

    @BeforeClass
    public static void initLogging() throws Exception {
        OnlineManagementClient client = TestUtil.clientInit();

        // enable RESTEasy debug logging
        TestUtil.runCmd(client, "/subsystem=logging/console-handler=CONSOLE:write-attribute(name=level,value=ALL)");
        TestUtil.runCmd(client, "/subsystem=logging/logger=org.jboss.resteasy:add(level=ALL)");
        TestUtil.runCmd(client, "/subsystem=logging/logger=javax.xml.bind:add(level=ALL)");
        TestUtil.runCmd(client, "/subsystem=logging/logger=com.fasterxml.jackson:add(level=ALL)");

        client.close();
    }

    @AfterClass
    public static void removeLogging() throws Exception {
        OnlineManagementClient client = TestUtil.clientInit();

        // disable RESTEasy debug logging
        TestUtil.runCmd(client, "/subsystem=logging/console-handler=CONSOLE:write-attribute(name=level,value=INFO)");
        TestUtil.runCmd(client, "/subsystem=logging/logger=org.jboss.resteasy:remove()");
        TestUtil.runCmd(client, "/subsystem=logging/logger=javax.xml.bind:remove()");
        TestUtil.runCmd(client, "/subsystem=logging/logger=com.fasterxml.jackson:remove()");

        client.close();
    }

    @Deployment
    public static Archive<?> deploy() {
        WebArchive war = TestUtil.prepareArchive(PathParamJaxbTest.class.getSimpleName());
        war.addClass(PathParamJaxbModel.class);
        return TestUtil.finishContainerPrepare(war, null, PathParamJaxbResource.class);
    }

    private String generateURL(String path) {
        return PortProviderUtil.generateURL(path, PathParamJaxbTest.class.getSimpleName());
    }


    @Test
    public void putTest() {
        Response response = client.target(generateURL("/parameter/put")).request().put(Entity.entity(model, MediaType.APPLICATION_XML));
        Assert.assertThat(response.getStatus(), is(HttpResponseCodes.SC_NO_CONTENT));
    }

    @Test
    public void postTest() {
        Response response = client.target(generateURL("/parameter/post")).request().post(Entity.entity(model, MediaType.APPLICATION_XML));
        Assert.assertThat(response.getStatus(), is(HttpResponseCodes.SC_NO_CONTENT));
    }
}
