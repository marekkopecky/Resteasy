package org.jboss.resteasy.test.providers.sse.resource;

import org.jboss.logging.Logger;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;
import java.io.IOException;

@Path("/broadcast")
public class SseBroadcastResource {

   private static final Object sseBroadcasterLock = new Object();
   private static volatile SseBroadcaster sseBroadcaster;
   private static volatile boolean onErrorCalled = false;
   private static volatile boolean onCloseCalled = false;

   private static final Logger logger = Logger.getLogger(SseBroadcastResource.class);

   static SseEventSink subscribedSink = null;

   @GET
   @Path("/subscribe")
   @Produces(MediaType.SERVER_SENT_EVENTS)
   public void subscribe(@Context SseEventSink sink, @Context Sse sse) throws IOException {

      if (sink == null) {
         throw new IllegalStateException("No client connected.");
      }
      synchronized (this.sseBroadcasterLock) {
         //subscribe
         if (sseBroadcaster == null) {
            sseBroadcaster = sse.newBroadcaster();
            onCloseCalled = false;
            onErrorCalled = false;
         }
      }
      sseBroadcaster.register(sink);
      subscribedSink = sink;
      logger.info("Sink registered");
   }

   @POST
   @Path("/start")
   public void broadcast(String message, @Context Sse sse) throws IOException {
      if (this.sseBroadcaster == null) {
         throw new IllegalStateException("No Sse broadcaster created.");
      }
      this.sseBroadcaster.broadcast(sse.newEvent(message));
   }

   @GET
   @Path("/closeSink")
   public void closeSink() {
      if (this.sseBroadcaster == null) {
         throw new IllegalStateException("No Sse broadcaster created.");
      }

      logger.info("attempt to close sseEventSink");
      if (subscribedSink != null) {
         logger.info("closing sseEventSink");
         subscribedSink.close();
      }
   }

   @GET
   @Path("/listeners")
   public void registerListeners(@Context Sse sse) throws IOException {

      synchronized (this.sseBroadcasterLock) {
         if (sseBroadcaster == null) {
            sseBroadcaster = sse.newBroadcaster();
            onCloseCalled = false;
            onErrorCalled = false;
         }
         sseBroadcaster.onClose(sseEventSink -> {
            onCloseCalled = true;
            logger.info("onClose called");
         });
         sseBroadcaster.onError((sseEventSink, throwable) -> {
            onErrorCalled = true;
            logger.info("onError called");
         });
      }
   }

   @DELETE
   public void close() throws IOException
   {
      synchronized (this.sseBroadcasterLock)
      {
         if (sseBroadcaster != null)
         {
            sseBroadcaster.close();
            sseBroadcaster = null;
         }
      }
   }

   @GET
   @Path("/onCloseCalled")
   public boolean onCloseCalled() {
      synchronized (this.sseBroadcasterLock) {
         return onCloseCalled;
      }
   }

   @GET
   @Path("/onErrorCalled")
   public boolean onErrorCalled() {
      synchronized (this.sseBroadcasterLock) {
         return onErrorCalled;
      }
   }
}
