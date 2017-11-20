package org.code4j.searchbox.core;

import org.code4j.searchbox.annotation.Id;
import org.code4j.searchbox.annotation.Routing;
import org.code4j.searchbox.annotation.Type;
import org.code4j.searchbox.annotation.Version;
import org.code4j.searchbox.core.index.IndexEntity;
import org.code4j.searchbox.kits.JsonUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xingtianyu(code4j) Created on 2017-11-19.
 */
public class IndexProcessor<T> {

    /**
     * 解析自定义类，转换成索引要用的实体
     * @param object
     * @return
     * @throws Exception
     */
    public IndexEntity handle(T object) throws Exception {
        IndexEntity entity = new IndexEntity();
        String typeName = "";
        Type type = object.getClass().getAnnotation(Type.class);
        if (type != null){
            typeName = type.value();
        }else{
            typeName = object.getClass().getSimpleName().toLowerCase();
        }
        entity.setType(typeName);
        Field[] fields = object.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                setId(field, object, entity);
                setRout(field, object, entity);
                setVersion(field, object, entity);
            }
        }
        entity.setSource(JsonUtil.toString(object));
        return entity;
    }

    public List<IndexEntity> handleBulk(List<T> objs) throws Exception {
        if (objs == null || objs.size() == 0){
            return null;
        }
        List<IndexEntity> entities = new ArrayList<>();
        for (T obj:objs){
            entities.add(handle(obj));
        }
        return entities;
    }

    private static void setId(Field field, Object delegate, IndexEntity entity) throws IllegalAccessException {
        Id id = field.getAnnotation(Id.class);
        String idValue;
        if (id != null) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object value = field.get(delegate);
            if (value == null) {
                return;
            }
            if (String.class == value.getClass()) {
                idValue = (String) value;
            } else {
                idValue = String.valueOf(value);
            }
            field.setAccessible(accessible);
            entity.setId(idValue);
        }
    }

    private static void setRout(Field field, Object delegate, IndexEntity entity) throws IllegalAccessException {
        Routing rout = field.getAnnotation(Routing.class);
        String routingValue;
        if (rout != null) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object value = field.get(delegate);
            if (value == null) {
                return;
            }
            if (String.class == value.getClass()) {
                routingValue = (String) value;
            } else {
                routingValue = String.valueOf(value);
            }
            field.setAccessible(accessible);
            entity.setRouting(routingValue);
        }
    }

    private static void setVersion(Field field, Object delegate, IndexEntity entity) throws IllegalAccessException {
        Version version = field.getAnnotation(Version.class);
        Long versionValue;
        if (version != null) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            Object value = field.get(delegate);
            if (value == null) {
                return;
            }
            if (Long.class == value.getClass()) {
                versionValue = (Long) value;
            } else {
                versionValue = Long.valueOf(value.toString());
            }
            field.setAccessible(accessible);
            entity.setVersion(versionValue);
        }
    }
}
