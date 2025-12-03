package com.boisseau.offertechnicaltest.model;

public enum Gender {
    MALE,FEMALE,OTHER;

    public String toString(){
        return switch(this) {
            case MALE -> "Male";
            case FEMALE -> "Female";
            case OTHER -> "Other";
        };

    }
}
