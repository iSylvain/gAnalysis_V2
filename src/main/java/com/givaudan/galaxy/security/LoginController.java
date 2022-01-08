package com.givaudan.galaxy.security;

import com.givaudan.galaxy.util.MessageProvider;
import com.givaudan.galaxy.util.MessageUtil;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.picketlink.Identity.AuthenticationResult;

@Stateless
@Named
public class LoginController {

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Inject
    private Identity identity;

    public void login() {
        logger.info("login called");

        AuthenticationResult result = identity.login();
        if (AuthenticationResult.FAILED.equals(result)) {
            MessageUtil.addErrorMessage(new MessageProvider().getValue("msg", "loginError"), "");
        } else {
            try {
                MessageUtil.addSuccessMessage(new MessageProvider().getValue("msg", "login"), "");
                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
