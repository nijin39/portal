package com.tandem6.portal.web.rest.errors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

	private static final long serialVersionUID = -96889200296550392L;

	public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email address already in use", "userManagement", "emailexists");
    }
}
