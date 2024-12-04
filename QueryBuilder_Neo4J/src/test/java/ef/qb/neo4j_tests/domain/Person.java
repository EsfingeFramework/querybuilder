package ef.qb.neo4j_tests.domain;

import ef.qb.core.annotation.PersistenceType;
import static ef.qb.core.utils.PersistenceTypeConstants.NEO4J;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
@PersistenceType(NEO4J)
public class Person {

    @Id
    private Long graphId;

    private String name;
    private String lastName;
    private Integer age;

    @Relationship(type = "LIVES", direction = Relationship.Direction.OUTGOING)
    private Address address;

    public Long getGraphId() {
        return graphId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        if (address != null) {
            return name + " " + lastName + " " + age + " " + address.getCity() + " " + address.getState();
        } else {
            return name + " " + lastName + " " + age + " null";
        }
    }
}
