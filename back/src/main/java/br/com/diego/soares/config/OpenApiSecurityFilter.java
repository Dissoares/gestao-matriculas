package br.com.diego.soares.config;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;

public class OpenApiSecurityFilter implements OASFilter {

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        String url = ConfigProvider.getConfig()
                .getOptionalValue("quarkus.oidc.auth-server-url", String.class)
                .orElse(null);

        if (url == null
                || openAPI.getComponents() == null
                || openAPI.getComponents().getSecuritySchemes() == null) {
            return;
        }

        var scheme = openAPI.getComponents().getSecuritySchemes().get("keycloak");
        if (scheme != null
                && scheme.getFlows() != null
                && scheme.getFlows().getPassword() != null) {
            scheme.getFlows().getPassword().setTokenUrl(url + "/protocol/openid-connect/token");
        }
    }
}
