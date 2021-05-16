package com.credo.database.web.rest.errors;

import java.net.URI;

public class SaveException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SaveException() {
        super(URI.create(""), "General error saving.", "person", "generalsaveerror");
    }
}
