package ru.arkhipov.MySecondTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Systems {
    ERP("Enterprise Resource Planning"),
    CRM("Customer Relationship Management"),
    WMS("Warehouse Management System");
    private final String system;

    Systems(String system) {
        this.system = system;
    }

    @JsonValue
    public String getSystem() {
        return system;
    }

    @Override
    public String toString() {
        return system;
    }
}
