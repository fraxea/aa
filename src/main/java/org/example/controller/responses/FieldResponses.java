package org.example.controller.responses;

public interface FieldResponses {
    String EMPTY_FIELD = "This field must be nonempty!";
    String SHORT_FIELD = "This field must contain at least 4 characters!";
    String LONG_FIELD = "This field must contain at most 14 characters!";
}
