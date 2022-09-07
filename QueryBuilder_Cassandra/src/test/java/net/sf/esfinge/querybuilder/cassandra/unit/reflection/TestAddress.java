package net.sf.esfinge.querybuilder.cassandra.unit.reflection;

import java.util.Objects;

public class TestAddress {

    private String city;
    private String province;
    private int code;

    public TestAddress(String city, String province, int code) {
        this.city = city;
        this.province = province;
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TestAddress{" +
                "city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", code=" + code +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestAddress that = (TestAddress) o;
        return code == that.code && Objects.equals(city, that.city) && Objects.equals(province, that.province);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, province, code);
    }
}
