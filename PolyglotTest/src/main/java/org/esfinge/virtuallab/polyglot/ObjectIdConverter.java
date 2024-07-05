package org.esfinge.virtuallab.polyglot;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.bson.types.ObjectId;

@Converter(autoApply = true)
public class ObjectIdConverter implements AttributeConverter<ObjectId, String> {

    @Override
    public String convertToDatabaseColumn(ObjectId objectId) {
        return objectId != null ? objectId.toHexString() : null;
    }

    @Override
    public ObjectId convertToEntityAttribute(String dbData) {
        return dbData != null ? new ObjectId(dbData) : null;
    }
}
