package ru.polescanner.roomexample.domain;

import static org.apache.commons.lang3.Validate.inclusiveBetween;
import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.apache.commons.lang3.Validate.notBlank;

import java.util.Objects;

abstract class Name{
    private final String value;

    Name (final String name, int NAME_MIN_LENGTH, int NAME_MAX_LENGTH, String NAME_VALID_CHARS) {
        notBlank(name, "Name should be provided");
        final String trimmed = name.trim();
        inclusiveBetween(NAME_MIN_LENGTH,
                         NAME_MAX_LENGTH,
                         trimmed.length(),
                         "Length of the name should be within %s - %s",
                         NAME_MIN_LENGTH, NAME_MAX_LENGTH);
        matchesPattern (trimmed,
                        NAME_VALID_CHARS,
                        "Allowed characters are: %s", NAME_VALID_CHARS);
        this.value = trimmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return value.equals(name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString(){
        return this.value;
    }
}
