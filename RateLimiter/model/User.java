package model;

import enums.UserType;
import java.util.Objects;

public class User {
    private String name;
    private UserType userType;

    public User(String name, UserType userType) {
        this.name = name;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public UserType getUserType() {
        return userType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && userType == user.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userType);
    }
}