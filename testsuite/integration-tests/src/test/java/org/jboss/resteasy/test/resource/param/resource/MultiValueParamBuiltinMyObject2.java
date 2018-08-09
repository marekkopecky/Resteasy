package org.jboss.resteasy.test.resource.param.resource;

/**
 * @author Marek Kopecky mkopecky@redhat.com
 */
public class MultiValueParamBuiltinMyObject2 {
    String a;
    String b;


    public static MultiValueParamBuiltinMyObject2 valueOf(String s) {
        String[] array = s.split("\\+");

        MultiValueParamBuiltinMyObject2 myObject = new MultiValueParamBuiltinMyObject2();
        myObject.setA(array[0]);
        myObject.setB(array[1]);
        return myObject;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return getA() + "+" + getB();
    }

    public String specialToString() {
        return getA() + "...." + getB();
    }
}
