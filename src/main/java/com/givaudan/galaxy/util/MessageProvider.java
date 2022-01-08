package com.givaudan.galaxy.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

public class MessageProvider {
 
    private ResourceBundle bundle;
 
    public String getLocale() {
        try {
            String locale = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
            return locale;
        } catch (Exception e) {
            return "en";
        }
    }
    
    private ResourceBundle getBundle(String resource) {
        if (bundle == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            bundle = context.getApplication().getResourceBundle(context, resource);
        }
        return bundle;
    }
 
    public String getValue(String resource, String key) {
 
        String result = null;
        try {
            result = getBundle(resource).getString(key);
        } catch (MissingResourceException e) {
            result = "???" + key + "??? not found";
        }
        return result;
    }
 
}
