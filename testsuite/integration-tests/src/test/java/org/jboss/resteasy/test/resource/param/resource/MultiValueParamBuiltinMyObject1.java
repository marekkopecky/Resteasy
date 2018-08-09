package org.jboss.resteasy.test.resource.param.resource;

/**
 * @author Marek Kopecky mkopecky@redhat.com
 */
public class MultiValueParamBuiltinMyObject1 {

    String a;


    public static MultiValueParamBuiltinMyObject1 valueOf(String s) {
        MultiValueParamBuiltinMyObject1 myObject = new MultiValueParamBuiltinMyObject1();
        myObject.setA(s);
        return myObject;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return getA();
    }
}
