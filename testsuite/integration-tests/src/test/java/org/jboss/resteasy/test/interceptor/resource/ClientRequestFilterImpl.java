package org.jboss.resteasy.test.interceptor.resource;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class ClientRequestFilterImpl implements ClientRequestFilter
{

   @Override
   public void filter(ClientRequestContext clientRequestContext) throws IOException
   {
      if (clientRequestContext.getUri().toString().contains("testIt")) {
         clientRequestContext.abortWith(Response.status(404).build());
      }
   }
}
