package ru.polescanner.roomexample.domain;

public class Image implements Description{
    public String value;

    public Image(String value) {
        this.value = value;
    }

    @Override
    public String toString64() {
        return value;
    }
}
