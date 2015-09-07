package uk.co.exware.model;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Instrument {

    private final String code;

    public Instrument(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Instrument that = (Instrument) o;

        return this.hashCode() == that.hashCode();

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .toHashCode();
    }
}
