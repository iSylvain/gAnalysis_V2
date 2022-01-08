package com.givaudan.galaxy.security;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
import static org.picketlink.idm.model.basic.BasicModel.getRole;
import static org.picketlink.idm.model.basic.BasicModel.grantRole;

@Singleton
@Startup
public class SecurityInitializer {

    private static final Logger logger = Logger.getLogger(SecurityInitializer.class.getName());

    @Inject
    private PartitionManager partitionManager;


    @PostConstruct
    public void init() {

        IdentityManager identityManager = partitionManager.createIdentityManager();
        RelationshipManager relationshipManager = partitionManager.createRelationshipManager();

        //***Actual V2.0
        //Chemist
        //Manager
        //Labo
        //Safety

        createRoles(identityManager);

        createUsers(identityManager,
                relationshipManager,
                "terciers",
                "Sylvain",
                "Tercier",
                "sylvain.tercier@givaudan.com",
                "1234",
                "manager");

        createUsers(identityManager,
                relationshipManager,
                "cretinm",
                "Marie",
                "Cretin",
                "marie.cretin@givaudan.com",
                "antoinette",
                "manager");

        createUsers(identityManager,
                relationshipManager,
                "bussardd",
                "Déborah",
                "Bussard",
                "deborah.bussard@givaudan.com",
                "poulette",
                "manager");

        createUsers(identityManager,
                relationshipManager,
                "ruchetd",
                "Daniel",
                "Ruchet",
                "daniel.ruchet@givaudan.com",
                "12345",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "prianf",
                "Fabrice",
                "Prian",
                "fabrice.prian@givaudan.com",
                "Cameleon19",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "rosierc",
                "Clélia",
                "Rosier",
                "clelia.rosier@givaudan.com",
                "01Lady",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "boutlaner",
                "Radouane",
                "Boutlane",
                "radouane.boutlane@givaudan.com",
                "rad010281",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "pererap",
                "Pauline",
                "Perera",
                "pauline.perera@givaudan.com",
                "paul01",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "bettonig",
                "Gaël",
                "Bettoni",
                "gael.bettoni@givaudan.com",
                "gael07",
                "editor");

        createUsers(identityManager,
                relationshipManager,
                "paubers",
                "Sophie",
                "Paubert",
                "sophie.paubert@givaudan.com",
                "soph12",
                "editor");

    }

    private void createUsers(IdentityManager identityManager,
                             RelationshipManager relationshipManager,
                             String username,
                             String firsname,
                             String lastname,
                             String email,
                             String password,
                             String role) {

        //user
        User user = new User(username);
        user.setFirstName(firsname);
        user.setLastName(lastname);
        user.setEmail(email);

        identityManager.add(user);
        identityManager.updateCredential(user, new Password(password));

        switch (role.toLowerCase()) {

            case "manager":
                //Role
                grantRole(relationshipManager, user, getRole(identityManager, "manager"));
                break;

            case "editor":
                //Role
                grantRole(relationshipManager, user, getRole(identityManager, "moduleEditor"));
                break;

            case "moduleViewer":
                //Role
                grantRole(relationshipManager, user, getRole(identityManager, "moduleViewer"));
                break;

            default:
                //Role
                grantRole(relationshipManager, user, getRole(identityManager, "viewer"));
                break;
        }

    }

    private void createRoles(IdentityManager identityManager) {

        // Create role "manager"
        Role manager = new Role("manager");
        identityManager.add(manager);

        // Create role "moduleEditor"
        Role moduleEditor = new Role("moduleEditor");
        identityManager.add(moduleEditor);

        // Create role "moduleViewer"
        Role moduleViewer = new Role("moduleViewer");
        identityManager.add(moduleViewer);

        // Create role "viewer"
        Role viewer = new Role("viewer");
        identityManager.add(viewer);

    }
}
