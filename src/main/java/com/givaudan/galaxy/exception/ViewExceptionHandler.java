package com.givaudan.galaxy.exception;

import com.givaudan.galaxy.util.MessageUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.el.ELException;
import javax.enterprise.context.NonexistentConversationException;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class ViewExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger logger = Logger.getLogger(ViewExceptionHandler.class.getName());

    private final ExceptionHandler wrapped;

    public ViewExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {

//        logger.log(Level.INFO, "handle called.");

        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext();) {

            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable t = context.getException();

            while ((t instanceof FacesException || t instanceof EJBException || t instanceof ELException) && t.getCause() != null) {
                t = t.getCause();
                logger.log(Level.INFO, "Throwable message : {0}", t.getMessage());
                logger.log(Level.INFO, "Throwable class name : {0}", t.getClass().getName());
            }

            if (t instanceof ViewExpiredException) {

                logger.log(Level.WARNING, "ViewExpiredException");
                ViewExpiredException exception = (ViewExpiredException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {

                    String message;
                    message = exception.getMessage(); // beware, don't leak internal info!
                    logger.log(Level.WARNING, "message:{0}", message);

                    MessageUtil.addWarmMessage("Session expired !", "");
                    nav.handleNavigation(fc, null, "/home?faces-redirect=true");

                    fc.renderResponse();

                } finally {
                    i.remove();
                }
            } else if (t instanceof SecurityException) {

                logger.log(Level.WARNING, "SecurityException");
                SecurityException exception = (SecurityException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {

                    String message = exception.getMessage(); // beware, don't leak internal info!
                    if (message == null) {
                        message = "Entity not modified -> Insufficient privileges !";
                    }
                    logger.log(Level.WARNING, "message:{0}", message);
                    MessageUtil.addErrorMessage("SecurityException", message);
                    nav.handleNavigation(fc, null, "/error?faces-redirect=true");

                    fc.renderResponse();

                } finally {
                    i.remove();
                }
            } else if (t instanceof NonexistentConversationException) {

                logger.log(Level.WARNING, "");
                NonexistentConversationException exception = (NonexistentConversationException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {

                    String message = exception.getMessage(); // beware, don't leak internal info!
                    logger.log(Level.WARNING, "message:{0}", message);
                    MessageUtil.addErrorMessage("NonexistentConversationException", message);
                    nav.handleNavigation(fc, null, "/error?faces-redirect=true?nocid=true");

                    fc.renderResponse();

                } finally {
                    i.remove();
                }
            } else if (t instanceof NoEntityException) {

                logger.log(Level.WARNING, "NoEntityException");
                NoEntityException exception = (NoEntityException) t;
                FacesContext fc = FacesContext.getCurrentInstance();
                NavigationHandler nav = fc.getApplication().getNavigationHandler();

                try {

                    String message = exception.getMessage(); // beware, don't leak internal info!
                    logger.log(Level.WARNING, "message:{0}", message);

                    Flash flash = fc.getExternalContext().getFlash();
                    flash.setKeepMessages(true);
                    flash.setRedirect(true);
                    
                    fc.getExternalContext().getFlash().put("errorType","NoEntityException");
                    fc.getExternalContext().getFlash().put("errorMsg",message);
                    nav.handleNavigation(fc, null, "/error?faces-redirect=true");
                    
                    fc.renderResponse();

                } finally {
                    i.remove();
                }
            }
        }

        // At this point, the queue will not contain any ViewExpiredEvents.
        // Therefore, let the parent handle them.
        getWrapped().handle();
    }

}
