package model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Transient;
import ef.qb.core.annotation.PersistenceType;
import ef.qb.core.annotation.PolyglotJoin;
import ef.qb.core.annotation.PolyglotOneToMany;
import static ef.qb.core.utils.PersistenceTypeConstants.CASSANDRA;
import static ef.qb.core.utils.PersistenceTypeConstants.MONGODB;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;

@Entity
@PersistenceType(value = MONGODB, secondary = CASSANDRA)
@Data
public class Rating implements Serializable {

    @Id
    private ObjectId id;
    private String product;
    private String comment;
    private int value;
    private String source;
    @Transient
    @PolyglotOneToMany(referencedEntity = Rating.class)
    @PolyglotJoin(name = "product", referencedAttributeName = "product")
    private List<Item> items;
}
