package org.jboss.resteasy.test.interceptor;


import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.test.client.ClientTestBase;
import org.jboss.resteasy.test.interceptor.resource.ClientRequestFilterImpl;
import org.jboss.resteasy.test.interceptor.resource.ClientResource;
import org.jboss.resteasy.test.interceptor.resource.CustomTestApp;
import org.jboss.resteasy.utils.TestUtil;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @tpSubChapter Interceptor
 * @tpChapter Integration tests
 * @tpTestCaseDetails Tests @PreMatching annotation on ClientRequestFilter (RESTEASY-1696)
 * @tpSince RESTEasy 4.0.0
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ClientRequestFilterRegistrationTest extends ClientTestBase {
   
   private static Client client;

   @Deployment
   public static Archive<?> deploy() {
      WebArchive war = ShrinkWrap.create(WebArchive.class, ClientRequestFilterRegistrationTest.class.getSimpleName() + ".war");
      war.addClasses(CustomTestApp.class, ClientRequestFilterImpl.class, ClientResource.class);
      war.addAsManifestResource(new StringAsset("org.jboss.resteasy.test.interceptor.resource.ClientRequestFilterImpl"),
            "services/javax.ws.rs.ext.Providers");
      System.out.println(war.toString(true));
      return war;
   }

   @Before
   public void before() {
      client = ClientBuilder.newClient();
   }

   @After
   public void close() {
      client.close();
   }
   
   @Test
   public void preMatchingTest() throws Exception {
      WebTarget base = client.target(generateURL("/") + "testIt");
      Response response = base.request().get();
      Assert.assertEquals(404, response.getStatus());
   }

}
