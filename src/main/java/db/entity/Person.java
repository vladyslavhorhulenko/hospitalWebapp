package db.entity;

public abstract class Person {
    private int id;
    private String name;
    private String surname;

    public Person(int id, String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.id = id;
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Person() {}

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName() {
        return getSurname() + " " + getName();
    }

    @Override
    public String toString()  {
        return getFullName();
    }

    public int getId() {
        return id;
    }

}
