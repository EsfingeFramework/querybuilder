package model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.MONGODB;
import java.io.Serializable;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity
@PersistenceType(MONGODB)
@Data
public class Rating implements Serializable {

    @Id
    private ObjectId id;
    private String product;
    private String comment;
    private int value;
    private String source;
}
