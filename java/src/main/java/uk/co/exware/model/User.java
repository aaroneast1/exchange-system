package uk.co.exware.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class User {

    private final String userId;

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return this.hashCode() == user.hashCode();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(userId).toHashCode();
    }
}
