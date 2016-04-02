package models;

/**
 * Created by qinxi on 2016/4/3.
 */
public class User {
    String name;
    String phone;

    public User() {

    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
