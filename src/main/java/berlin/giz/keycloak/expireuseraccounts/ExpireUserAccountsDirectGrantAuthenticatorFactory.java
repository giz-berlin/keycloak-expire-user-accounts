package berlin.giz.keycloak.expireuseraccounts;

import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;

public class ExpireUserAccountsDirectGrantAuthenticatorFactory extends ExpireUserAccountsAuthenticatorFactory {

    private static final String PROVIDER_ID = "expire-user-accounts-direct-grant";
    private static final ExpireUserAccountsDirectGrantAuthenticator EXPIRE_USER_ACCOUNTS_AUTHENTICATOR = new ExpireUserAccountsDirectGrantAuthenticator();

    @Override
    public String getDisplayType() {
        return "Expire User Accounts Authenticator for Direct Grant";
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[0];
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return EXPIRE_USER_ACCOUNTS_AUTHENTICATOR;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
