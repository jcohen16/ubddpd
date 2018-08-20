package com.ubddpd;

public class Employee {

    public String id;
    public String name;
    public String title;

    @Override
    public boolean equals(Object compare){
        Employee emp = (Employee)compare;
        boolean result = emp.id.equals(this.id);
        result = result && emp.name.equals(this.name);
        result = result && emp.title.equals(this.title);
        return result;
    }
}
