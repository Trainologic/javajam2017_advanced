package com.tl.language.wars.kotlin;

public class JavaPerson {
    private final String name;
    private final Integer age;

    public JavaPerson(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JavaPerson)) return false;

        JavaPerson that = (JavaPerson) o;

        if (!getName().equals(that.getName())) return false;
        return getAge().equals(that.getAge());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getAge().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "JavaPerson{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public static void main(String[] args) {
        Person p = new Person("test",123);
        p.getAge();
    }
}
