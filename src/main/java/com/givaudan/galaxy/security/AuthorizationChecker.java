package com.givaudan.galaxy.security;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.basic.BasicModel;
import static org.picketlink.idm.model.basic.BasicModel.getGroup;
import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static org.picketlink.idm.model.basic.BasicModel.hasRole;
import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;

@Model
public class AuthorizationChecker implements Serializable {
    
    @Inject
    private Identity identity;

    @Inject
    private IdentityManager identityManager;

    @Inject
    private RelationshipManager relationshipManager;

    @Produces
    @RequestScoped
    public User getCurrentUser() {
        return (User) identity.getAccount();
    }

    public boolean hasApplicationRole(String roleName) {
        Role role = getRole(this.identityManager, roleName);
        Boolean result = hasRole(this.relationshipManager, this.identity.getAccount(), role);
        return result;
    }

    public boolean isMember(String groupName) {
        Group group = getGroup(this.identityManager, groupName);
        return BasicModel.isMember(this.relationshipManager, this.identity.getAccount(), group);
    }

    public boolean hasGroupRole(String roleName, String groupName) {
        Group group = getGroup(this.identityManager, groupName);
        Role role = getRole(this.identityManager, roleName);
        return BasicModel.hasGroupRole(this.relationshipManager, this.identity.getAccount(), role, group);
    }

    public boolean hasManagerRole() throws Exception {
        if(!identity.isLoggedIn()) return false;
        return hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "manager"));
    }
    
    public boolean hasEditorRole() throws Exception {
        if(!identity.isLoggedIn()) return false;
        return hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "manager")) ||
               hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "moduleEditor"));
    }
    
    public boolean hasModuleRole() throws Exception {
        if(!identity.isLoggedIn()) return false;
        return hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "manager")) ||
               hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "moduleEditor")) ||
               hasRole(relationshipManager, identity.getAccount(), getRole(identityManager, "moduleViewer"));
    }
}
