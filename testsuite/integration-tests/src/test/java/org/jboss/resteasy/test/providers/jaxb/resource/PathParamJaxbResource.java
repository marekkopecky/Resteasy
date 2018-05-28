package org.jboss.resteasy.test.providers.jaxb.resource;

import org.jboss.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/parameter")
public class PathParamJaxbResource {

    private final Logger logger = Logger.getLogger(PathParamJaxbResource.class.getName());

    @PUT
    @Path("{parameter}")
    @Consumes(MediaType.APPLICATION_XML)
    public void put(@PathParam("parameter") String parameter, PathParamJaxbModel model) throws Exception {
        logger.info("put(): http model from body : " + model.toString());
        logger.info("put(): pathParam: + " + parameter);
        if (!parameter.equals("put") || model.getA() != 1 || model.getB() != 2) {
            throw new Exception();
        }
    }

    @POST
    @Path("{parameter}")
    @Consumes(MediaType.APPLICATION_XML)
    public void post(@PathParam("parameter") String parameter, PathParamJaxbModel model) throws Exception {
        logger.info("post(): http model from body : " + model.toString());
        logger.info("post(): pathParam: + " + parameter);
        if (!parameter.equals("post") || model.getA() != 1 || model.getB() != 2) {
            throw new Exception();
        }
    }

}
