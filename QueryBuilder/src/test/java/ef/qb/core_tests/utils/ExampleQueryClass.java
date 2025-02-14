package ef.qb.core_tests.utils;

import java.util.Date;
import ef.qb.core.annotation.Contains;
import ef.qb.core.annotation.Lesser;

public class ExampleQueryClass {

    private String name;

    @Lesser
    private Integer age;

    private String lastName;

    private Date birthDateGreater;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Contains
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthDateGreater() {
        return birthDateGreater;
    }

    public void setBirthDateGreater(Date birthDateGreater) {
        this.birthDateGreater = birthDateGreater;
    }

}
