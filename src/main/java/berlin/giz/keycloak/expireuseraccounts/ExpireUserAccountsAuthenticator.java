package berlin.giz.keycloak.expireuseraccounts;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.*;
import org.keycloak.services.messages.Messages;

import java.time.LocalDate;
import javax.ws.rs.core.Response;

/**
 * {@link Authenticator} that requires a users account to not be expired.
 */
public class ExpireUserAccountsAuthenticator implements Authenticator {

    private static final String DENIED_MESSAGE_ATTRIBUTE_KEY = "deniedMessage";
    private static final String EXPIRATION_ATTRIBUTE_NAME = "accountExpirationDate";
    private static final Logger LOGGER = Logger.getLogger(ExpireUserAccountsAuthenticator
.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        ClientModel client = context.getAuthenticationSession().getClient();
        UserModel user = context.getUser();
        
        String accountExpirationDateString = user.getFirstAttribute(EXPIRATION_ATTRIBUTE_NAME);
        if (accountExpirationDateString == null || accountExpirationDateString.equals("")) {
            LOGGER.debugf("User %s had no account expiration date set or it was empty", user.getUsername());
            context.success();
            return;
        }

        LocalDate accountExpirationDate = LocalDate.parse(accountExpirationDateString);
        if (LocalDate.now().isBefore(accountExpirationDate)) {
            LOGGER.debugf("User's %s expiration date %s has not come yet", user.getUsername(), accountExpirationDate);
            context.success();
            return;
        }

        String deniedMessage = ExpireUserAccountsAuthenticatorFactory.DEFAULT_DENIED_MESSAGE;

        AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();
        if (authenticatorConfig != null) {
            deniedMessage = authenticatorConfig.getConfig().get(ExpireUserAccountsAuthenticatorFactory.DENIED_MESSAGE_NAME);
        }

        LOGGER.infof("The user %s tried to log in, but their account expired on %s. Disabling account ...", user.getUsername(), accountExpirationDate);

        // Disable the account, so they cannot use their account anywhere in Keycloak anymore.
        user.setEnabled(false);

        respondWithError(context, deniedMessage);
    }

    public void respondWithError(AuthenticationFlowContext context, String deniedMessage) {
        LoginFormsProvider loginFormsProvider = context.form();

        Response errorForm = loginFormsProvider
                .setError(deniedMessage)
                .createErrorPage(Response.Status.FORBIDDEN);
        context.forceChallenge(errorForm);
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void action(AuthenticationFlowContext context) {

    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }
}
