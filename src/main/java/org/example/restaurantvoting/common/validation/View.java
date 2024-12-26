package org.example.restaurantvoting.common.validation;

import jakarta.validation.groups.Default;

public class View {

    // Validate only form UI/REST
    public interface Web extends Default {}

    // Validate only when DB save/update
    public interface Persist extends Default {}
}
