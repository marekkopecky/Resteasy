package org.jboss.resteasy.test.cdi.validation.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Response;

@RequestScoped
public class SubResourceImpl implements SubResource
{
   static boolean methodEntered;
   private static final Logger LOG = LogManager.getLogger(SubResourceImpl.class);

   @Override
   public Response getAll(QueryBeanParamImpl beanParam)
   {
      LOG.info("beanParam#getParam valid? " + beanParam.getParam());
      methodEntered = true;
      return Response.ok().build();
   }
}
