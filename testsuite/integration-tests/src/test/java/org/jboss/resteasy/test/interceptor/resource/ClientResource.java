package org.jboss.resteasy.test.interceptor.resource;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Path("/")
public class ClientResource
{
   @GET
   @Path("testIt")
   public String get() {
      return "OK";
   }

   @POST
   @Path("call/client")
   public Response ping(String url) {

      WebTarget target = ClientBuilder.newClient()
              .target(url);

      return Response.status(target.request().get().getStatus() == 404 ? 204 : 500).build();
   }

}
