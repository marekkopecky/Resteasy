package org.jboss.resteasy.test.resource.param.resource;

import org.jboss.resteasy.annotations.jaxrs.QueryParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Marek Kopecky mkopecky@redhat.com
 */
@Path("queryParam")
public class MultiValueParamBuiltinResource {

    @GET
    @Path("list1")
    public Response conversion1(@QueryParam List<MultiValueParamBuiltinMyObject1> q) {
        if (q == null) throw new RuntimeException("q is null");
        System.out.println(q.size());
        q.stream().map(myObject -> myObject == null ? "null" : myObject.getA()).forEach(s -> System.out.println(s));
        return Response.ok(stringify(q.stream().map(myObject -> myObject.toString()).collect(Collectors.toList()))).build();
    }

    @GET
    @Path("list2")
    public Response conversion2(@QueryParam List<MultiValueParamBuiltinMyObject2> q) {
        if (q == null) throw new RuntimeException("q is null");
        System.out.println(q.size());
        q.stream().map(myObject -> myObject == null ? "null" : myObject.specialToString()).forEach(s -> System.out.println(s));
        return Response.ok(stringify(q.stream().map(myObject -> myObject.specialToString()).collect(Collectors.toList()))).build();
    }



    private static String stringify(List<String> list) {
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s).append(',');
        }
        return sb.toString();
    }
}
