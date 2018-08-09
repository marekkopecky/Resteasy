package org.jboss.resteasy.convertors;


import javax.ws.rs.ext.ParamConverter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marek Kopecky mkopecky@redhat.com
 */
public class MultiValuedParamListConverter<T> implements ParamConverter<List<T>> {
    private final Class<T> type;

    public MultiValuedParamListConverter(Class<T> type) {
        this.type = type;
    }

    public MultiValuedParamListConverter() {
        this.type = null;
    }


    @Override
    public List<T> fromString(String param) {
        if (param == null || param.trim().isEmpty()) {
            return null;
        }
        return parse(param.split(","));
    }

    private List<T> parse(String[] params) {
        List<T> list = new ArrayList<T>();
        try {
            for (String param : params) {
                T object = (T) type.getMethod("valueOf", String.class).invoke(null, param);
                list.add(object);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }




    @Override
    public String toString(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return stringify(list);
    }
    private <T> String stringify(List<T> list) {
        StringBuffer sb = new StringBuffer();
        for (T s : list) {
            sb.append(s).append(',');
        }
        return sb.toString();
    }
}
