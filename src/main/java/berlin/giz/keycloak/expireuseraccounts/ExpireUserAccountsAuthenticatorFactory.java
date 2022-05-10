package berlin.giz.keycloak.expireuseraccounts;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.services.messages.Messages;

import java.util.Collections;
import java.util.List;

public class ExpireUserAccountsAuthenticatorFactory implements AuthenticatorFactory {

    private static final String PROVIDER_ID = "expire-user-accounts";
    public static final String DEFAULT_DENIED_MESSAGE = Messages.NO_ACCESS;
    public static final String DENIED_MESSAGE_NAME = "deniedMessage";

    public static final ExpireUserAccountsAuthenticator EXPIRE_USER_ACCOUNTS_AUTHENTICATOR = new ExpireUserAccountsAuthenticator();

    @Override
    public String getDisplayType() {
        return "Expire User Accounts Authenticator";
    }

    @Override
    public String getReferenceCategory() {
        return null;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Denies access for users and disables those whose expiration date has passed.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty deniedMessage = new ProviderConfigProperty();
        deniedMessage.setDefaultValue(DEFAULT_DENIED_MESSAGE);

        deniedMessage.setType(ProviderConfigProperty.STRING_TYPE);
        deniedMessage.setName(DENIED_MESSAGE_NAME);
        deniedMessage.setLabel("Expiration Message");
        deniedMessage.setHelpText("Message to show to a user whose account has expired but tries to log in.");
        return Collections.singletonList(deniedMessage);
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return EXPIRE_USER_ACCOUNTS_AUTHENTICATOR;
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
