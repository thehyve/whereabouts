package nl.thehyve.whereabouts.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.spi.HttpFacade;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class KeycloakConfig {

    @Primary
    @Configuration
    static
    class CustomKeycloakSpringBootConfigResolver implements KeycloakConfigResolver {

        private final AdapterConfig adapterConfig;
        private KeycloakDeployment keycloakDeployment;

        @Autowired
        CustomKeycloakSpringBootConfigResolver(AdapterConfig adapterConfig) {
            this.adapterConfig = adapterConfig;
        }

        @Override
        public KeycloakDeployment resolve(HttpFacade.Request request) {
            if (this.keycloakDeployment == null) {
                this.keycloakDeployment = KeycloakDeploymentBuilder.build(this.adapterConfig);
            }
            return this.keycloakDeployment;
        }
    }

}
