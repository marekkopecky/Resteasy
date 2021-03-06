package org.jboss.resteasy.test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Path("/jaxrs")
public class AsyncJaxrsResource
{
   protected boolean cancelled;

   @GET
   @Path("resume/object")
   @Produces("application/xml")
   public void resumeObject(@Suspended final AsyncResponse response) {
      response.resume(new XmlData("bill"));
   }

   @GET
   @Path("resume/object/thread")
   @Produces("application/xml")
   public void resumeObjectThread(@Suspended final AsyncResponse response) throws Exception
   {
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            response.resume(new XmlData("bill"));
         }
      };
      t.start();
   }

   @GET
   @Path("injection-failure/{param}")
   public void injectionFailure(@Suspended final AsyncResponse response, @PathParam("param") int id) {
      System.out.println("injectionFailure: " + id);
      throw new ForbiddenException("Should be unreachable");
   }

   @GET
   @Path("method-failure")
   public void injectionFailure(@Suspended final AsyncResponse response) {
      throw new ForbiddenException("Should be unreachable");
   }

   @GET
   @Path("cancelled")
   public Response getCancelled()
   {
      if (cancelled) return Response.noContent().build();
      else return Response.status(500).build();
   }

   @PUT
   @Path("cancelled")
   public void resetCancelled()
   {
      cancelled = false;
   }

   @GET
   @Produces("text/plain")
   public void get(@Suspended final AsyncResponse response) throws Exception
   {
      response.setTimeout(200000, TimeUnit.MILLISECONDS);
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Thread.sleep(100);
               Response jaxrs = Response.ok("hello").type(MediaType.TEXT_PLAIN).build();
               response.resume(jaxrs);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      t.start();
   }

   @GET
   @Path("empty")
   @Produces("text/plain")
   public void getEmpty(@Suspended final AsyncResponse response) throws Exception
   {
      response.setTimeout(200000, TimeUnit.MILLISECONDS);
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Thread.sleep(100);
               response.resume(Response.noContent().build());
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      t.start();
   }

   @GET
   @Path("timeout")
   @Produces("text/plain")
   public void timeout(@Suspended final AsyncResponse response)
   {
      response.setTimeout(10, TimeUnit.MILLISECONDS);
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               Thread.sleep(100000);
               Response jaxrs = Response.ok("goodbye").type(MediaType.TEXT_PLAIN).build();
               response.resume(jaxrs);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      t.start();
   }

   @GET
   @Path("cancel")
   @Produces("text/plain")
   public void cancel(@Suspended final AsyncResponse response) throws Exception
   {
      System.out.println("entering cancel()");
      response.setTimeout(10000, TimeUnit.MILLISECONDS);
      final CountDownLatch sync = new CountDownLatch(1);
      final CountDownLatch ready = new CountDownLatch(1);
      Thread t = new Thread()
      {
         @Override
         public void run()
         {
            try
            {
               System.out.println("cancel(): starting thread");
               sync.countDown();
               ready.await();
               Response jaxrs = Response.ok("hello").type(MediaType.TEXT_PLAIN).build();
               System.out.println("SETTING CANCELLED");
               cancelled = !response.resume(jaxrs);
               System.out.println("cancelled: " + cancelled);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      t.start();

      sync.await();
      System.out.println("cancel(): cancelling response");
      response.cancel();
      ready.countDown();
      Thread.sleep(1000);
   }

}