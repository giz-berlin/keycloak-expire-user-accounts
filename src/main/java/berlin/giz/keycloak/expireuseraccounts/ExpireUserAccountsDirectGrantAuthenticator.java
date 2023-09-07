package berlin.giz.keycloak.expireuseraccounts;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * {@link ExpireUserAccountsAuthenticator} for the direct grant flow.
 */
public class ExpireUserAccountsDirectGrantAuthenticator extends ExpireUserAccountsAuthenticator {

    private static final String USER_ACCOUNT_EXPIRED = "user_account_expired";

    @Override
    public void respondWithError(AuthenticationFlowContext context, String deniedMessage) {
        OAuth2ErrorRepresentation errorRepresentation = new OAuth2ErrorRepresentation(USER_ACCOUNT_EXPIRED, deniedMessage);
        Response errorResponse = Response
                .status(Response.Status.FORBIDDEN.getStatusCode())
                .entity(errorRepresentation)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
        context.failure(AuthenticationFlowError.INVALID_USER, errorResponse);
    }
}
