package org.jboss.resteasy.test.interceptor.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class ClientResource
{
   @GET
   @Path("testIt")
   public String get() {
      return "OK";
   }

}
