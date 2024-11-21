package ef.qb.cassandra_tests.testresources;

import com.datastax.driver.mapping.annotations.UDT;
import ef.qb.cassandra.entity.CassandraEntity;
import java.util.Objects;
import static java.util.Objects.hash;

@UDT(keyspace = "test", name = "address")
public class Address implements CassandraEntity {
    private String city;
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(state, address.state);
    }

    @Override
    public int hashCode() {
        return hash(city, state);
    }
}
