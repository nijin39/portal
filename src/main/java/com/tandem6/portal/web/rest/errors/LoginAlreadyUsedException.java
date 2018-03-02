package com.tandem6.portal.web.rest.errors;

public class LoginAlreadyUsedException extends BadRequestAlertException {

	private static final long serialVersionUID = 8470304385981537832L;

	public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login already in use", "userManagement", "userexists");
    }
}
