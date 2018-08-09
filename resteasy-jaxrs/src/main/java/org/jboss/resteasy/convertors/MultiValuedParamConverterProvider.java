package org.jboss.resteasy.convertors;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
// import java.util.Set;

/**
 * @author Marek Kopecky mkopecky@redhat.com
 */
public class MultiValuedParamConverterProvider implements ParamConverterProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        Type className = null;
        try {
            className = ((java.lang.reflect.ParameterizedType) genericType).getActualTypeArguments()[0];
        } catch (Throwable t) {
            return null;
        }

        if (List.class.isAssignableFrom(rawType)) {
            return (ParamConverter<T>) new MultiValuedParamListConverter((Class<?>) className);
        }

//        if (Set.class.isAssignableFrom(rawType)) {
//            return (ParamConverter<T>) new MultiValuedParamSetConverter(className.getClass());
//        }

        return null;
    }
}
