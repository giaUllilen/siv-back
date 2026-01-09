package pe.interseguro.siv.admin.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import pe.interseguro.siv.admin.config.filter.APIKeyAuthFilter;
import pe.interseguro.siv.admin.config.filter.JwtFilter;
import pe.interseguro.siv.admin.config.filter.LoginFilter;
import pe.interseguro.siv.common.util.Constantes;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("#{ environment['siv.cors.origins'] }")
    private String origins;

    @Value("#{ environment['siv.cors.headers'] }")
    private String headers;

    @Value("#{ environment['siv.cors.methods'] }")
    private String methods;

    @Value("#{ environment['siv.header.apikey-name'] }")
    private String principalRequestHeader;

    @Value("#{ environment['siv.header.apikey-value'] }")
    private String principalRequestValue;
    
    @Value("#{ environment['siv.header.apikey-visapi-name'] }")
    private String apiKeyNameVisApi;
    
    @Value("#{ environment['siv.header.apikey-visapi-value'] }")
    private String apiKeyValueVisApi;

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Entro a WebSecurityConfig#configure(http)");

		/*APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(new AuthenticationManager() {
			
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String principal = (String) authentication.getPrincipal();
                if (!principalRequestValue.equals(principal))
                {
                    throw new BadCredentialsException("API Key inválido.");
                }
                authentication.setAuthenticated(true);
                return authentication;
			}
		});*/

        //-- Desactivar cookies
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //-- Activar CORS con valores por defecto
        /**
         * El enfoque basado en tokens con Cross Origin Resource Sharing (CORS) habilitado para exponer las API
         * a diferentes servicios y dominios
         */
        http.cors().configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowCredentials(true);
                config.setAllowedOrigins(Arrays.asList(origins.split(",")));
                config.setAllowedHeaders(Arrays.asList(headers.split(",")));
                config.setAllowedMethods(Arrays.asList(methods.split(",")));
                return config;
            }
        });

		/*http.antMatcher(Constantes.COMPARTIDO_URL).csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilter(filter).authorizeRequests().anyRequest().authenticated();*/

        //-- Desactivar el filtro de Cross-site request forgery (CSRF). Porque CORS se ha activado
        http.csrf().disable();

        //-- Se permite solo LOGIN_URL y el resto de peticiones requiere autenticación
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, Constantes.LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST, Constantes.URL_EXCEPTION_TOKEN_VIDA_FREE).permitAll()
                .antMatchers(HttpMethod.POST, Constantes.COMPARTIDO_URL).permitAll()
                .antMatchers(HttpMethod.GET, Constantes.CONSENTIMIENTO_RECOTIZAR_VALIDAR).permitAll()
                .antMatchers(HttpMethod.POST, Constantes.URL_EXCEPTION_INDENOVA_WEBHOOK_NOTIFICACION).permitAll()
                .antMatchers(HttpMethod.POST, Constantes.URL_EXCEPTION_TRANSMISION_COTIZACION).permitAll()
                .anyRequest().authenticated();
        

        http
                //-- Las peticiones LOGIN_URL pasarán previamente por este filtro
                .addFilterBefore(new LoginFilter(Constantes.LOGIN_URL, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //-- Las peticiones SERVICIOS_COMPARTIDOS_URL pasarán previamente por este filtro
                // .addFilterBefore(new APIKeyAuthFilter(Constantes.LOGIN_URL, authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                //-- LAS peticiones RESTANTES pasarán previamente por este filtro
                .addFilterBefore(new JwtFilter(authenticationManager()).inicializarConfiguracion(apiKeyNameVisApi, apiKeyValueVisApi), UsernamePasswordAuthenticationFilter.class);

        LOGGER.info("Salio a WebSecurityConfig#configure(http)");
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(NoOpPasswordEncoder.getInstance()) //-- Spring Security 5. Obliga a poner el password encriptado. Este method es para que omita ese paso
                .withUser(Constantes.LOGIN_BYPASS_USER)
                .password(Constantes.LOGIN_BYPASS_PASSWORD)
                .roles(Constantes.LOGIN_BYPASS_ROLE);
    }


}
