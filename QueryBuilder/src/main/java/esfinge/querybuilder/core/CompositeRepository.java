package esfinge.querybuilder.core;

import esfinge.querybuilder.core.annotation.PolyglotJoin;
import esfinge.querybuilder.core.annotation.PolyglotOneToMany;
import esfinge.querybuilder.core.annotation.PolyglotOneToOne;
import esfinge.querybuilder.core.executor.QueryExecutor;
import esfinge.querybuilder.core.methodparser.QueryInfo;
import esfinge.querybuilder.core.methodparser.QueryStyle;
import esfinge.querybuilder.core.methodparser.QueryType;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_LIST;
import static esfinge.querybuilder.core.methodparser.QueryType.RETRIEVE_SINGLE;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CompositeRepository<E> implements Repository<E> {

    private final Repository<E> primaryRepo;
    private final Repository secondaryRepo;
    private final QueryExecutor queryExecutor;
    private Class<E> configuredClass;

    @Override
    public void configureClass(Class<E> clazz) {
        configuredClass = clazz;
    }

    public CompositeRepository(Repository<E> primaryRepo, Repository secondaryRepo, QueryExecutor queryExecutor) {
        this.primaryRepo = primaryRepo;
        this.secondaryRepo = secondaryRepo;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public E save(E obj) {
        if (primaryRepo != null) {
            if (obj != null) {
                for (var field : obj.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(PolyglotOneToOne.class)) {
                        obj = saveForOneToOne(field, obj);
                    } else if (field.isAnnotationPresent(PolyglotOneToMany.class)) {
                        obj = saveForOneToMany(field, obj);
                    }
                }
                return obj;
            }
        }
        return null;
    }

    private E saveForOneToOne(Field field, E obj) {
        var fieldType = field.getType();
        var oneToOneAnn = field.getAnnotation(PolyglotOneToOne.class);
        var joinAnn = Objects.requireNonNull(field.getAnnotation(PolyglotJoin.class), "PolyglotJoin is required on field annotated with PolyglotOneToOne.");

        var referencedEntity = oneToOneAnn.referencedEntity();
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();
        try {
            if (fieldType.equals(referencedEntity)) {
                field.setAccessible(true);
                var fieldValue = field.get(obj);
                if (fieldValue != null) {
                    secondaryRepo.configureClass(fieldType);
                    var secSaved = secondaryRepo.save(fieldValue);
                    if (secSaved != null) {
                        field.set(obj, secSaved);
                        var castSec = fieldType.cast(secSaved);
                        var joinField = obj.getClass().getDeclaredField(joinName);
                        joinField.setAccessible(true);
                        Field secKey = castSec.getClass().getDeclaredField(referencedAttributeName);
                        secKey.setAccessible(true);
                        var secKeyValue = secKey.get(castSec);
                        joinField.set(obj, secKeyValue);
                        return primaryRepo.save(obj);
                    }
                }
            } else if (obj.getClass().equals(referencedEntity)) {
                obj = primaryRepo.save(obj);
                field.setAccessible(true);
                var fieldValue = field.get(obj);
                if (fieldValue != null) {
                    Field priKey = obj.getClass().getDeclaredField(referencedAttributeName);
                    priKey.setAccessible(true);
                    var priKeyValue = priKey.get(obj);
                    Field joinField = fieldValue.getClass().getDeclaredField(joinName);
                    joinField.setAccessible(true);
                    joinField.set(fieldValue, priKeyValue);
                    secondaryRepo.configureClass(fieldType);
                    var secSaved = secondaryRepo.save(fieldValue);
                    if (secSaved != null) {
                        field.set(obj, secSaved);
                        return obj;
                    }
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private E saveForOneToMany(Field field, E obj) {
        var oneToManyAnn = field.getAnnotation(PolyglotOneToMany.class);
        var joinAnn = Objects.requireNonNull(field.getAnnotation(PolyglotJoin.class), "PolyglotJoin is required on field annotated with PolyglotOneToMany.");

        var referencedEntity = oneToManyAnn.referencedEntity();
        var joinName = joinAnn.name();
        var referencedAttributeName = joinAnn.referencedAttributeName();
        try {
            if (obj.getClass().equals(referencedEntity)) {
                obj = primaryRepo.save(obj);
                field.setAccessible(true);
                var collection = (Collection<?>) field.get(obj);
                if (collection != null) {
                    Collection<Object> savedItems = collection.getClass().getDeclaredConstructor().newInstance();
                    for (var item : collection) {
                        if (item != null) {
                            secondaryRepo.configureClass(item.getClass());
                            Field joinField = item.getClass().getDeclaredField(joinName);
                            joinField.setAccessible(true);
                            Field priKey = obj.getClass().getDeclaredField(referencedAttributeName);
                            priKey.setAccessible(true);
                            var priKeyValue = priKey.get(obj);
                            joinField.set(item, priKeyValue);
                            savedItems.add(secondaryRepo.save(item));
                        }
                    }
                    if (!savedItems.isEmpty()) {
                        field.set(obj, savedItems);
                    }
                }
            }
            return obj;
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException | NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(E obj) {
        if (primaryRepo != null) {
            if (obj != null) {
                for (var field : obj.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(PolyglotOneToOne.class)) {
                        deleteForOneToOne(field, obj);
                    } else if (field.isAnnotationPresent(PolyglotOneToMany.class)) {
                        deleteForOneToMany(field, obj);
                    }
                }
                primaryRepo.delete(obj);
            }
        }
    }

    private void deleteForOneToOne(Field field, E obj) {
        try {
            field.setAccessible(true);
            var fieldValue = field.get(obj);
            if (fieldValue != null) {
                secondaryRepo.configureClass(field.getType());
                secondaryRepo.delete(fieldValue);
            }
        } catch (IllegalAccessException | SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteForOneToMany(Field field, E obj) {
        try {
            field.setAccessible(true);
            var collection = (Collection<?>) field.get(obj);
            if (collection != null) {
                for (var item : collection) {
                    if (item != null) {
                        secondaryRepo.configureClass(item.getClass());
                        secondaryRepo.delete(item);
                    }
                }
            }
        } catch (IllegalAccessException | SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<E> list() {
        return (List<E>) queryExecutor.executeQuery(createQueryInfo(RETRIEVE_LIST), null);
    }

    @Override
    public E getById(Object id) {
        return (E) queryExecutor.executeQuery(createQueryInfo(RETRIEVE_SINGLE), null);
    }

    @Override
    public List<E> queryByExample(E obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private QueryInfo createQueryInfo(QueryType queryType) {
        var qi = new QueryInfo();
        qi.setEntityType(configuredClass);
        qi.setEntityName(configuredClass.getSimpleName());
        qi.setQueryStyle(QueryStyle.METHOD_SIGNATURE);
        qi.setQueryType(queryType);
        return qi;
    }
}
