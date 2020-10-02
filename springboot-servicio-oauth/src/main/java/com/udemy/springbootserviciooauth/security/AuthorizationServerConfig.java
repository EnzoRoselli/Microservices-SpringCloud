package com.udemy.springbootserviciooauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer /*para habilitar esta clase como el servidor de autorizacion*/
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment environment;

    /*Se inyectan de los Bean de SpringSecurityConfig*/
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdditionalInfoToken additionalInfoToken;

    /*aca se configura el permiso que van a tener nuestros endpoints del servidor de autorizacion,
    de oauth2, para generar el token y validarlo*/
    /*en este metodo se configura la seguridad de dos endpoints(uno para generar el token y el otro
    para validarlo), ambos usan Header Authorization Basic: client id: client secret*/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        /*tokenKeyAccess es el endpoint para generar el token para autenticarnos, con la ruta /oauth/token
        * a esa url se manda las credenciales del usuario y del cliente de la aplicacion, configurado
        * en configure(clients)*/
        /*se usa permitAll() ya que la idea es que sea publico, para que cualquiera pueda autenticarse*/
        security.tokenKeyAccess("permitAll()")
        /*es un endpoint para validar que el cliente este autenticado*/
        .checkTokenAccess("isAuthenticated()");
    }

    /*aca se configura los clientes, las aplicaciones front que van a acceder a los microservicios
    * hay que registrar cada cliente con su client_id y su secret(la contraseña)*/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /*representa el identificador y la contraseña de nuestra aplicacion front(la aplicacion cliente)
        * que estan guardadas en un .txt en spring cloud config*/
        clients.inMemory().withClient(environment.getProperty("config.security.oauth.client.id"))
                .secret(passwordEncoder.encode(
                        environment.getProperty("config.security.oauth.client.secret")))
                /*configuramos el alcance de nuestra aplicacion front*/
                .scopes("read", "write")
                /*en authorizedGrantTypes se configura como va a ser el tipo de autenticacion para obtener
                el token,en este caso es con password, eso significa que el usuario tiene que mandar en sus
                credenciales el username y el password, el refresh_token nos permite tener un token
                renovado de acceso, antes de que caduque el actual*/
                .authorizedGrantTypes("password", "refresh_token")
                /*tiempo que dura el token*/
                .accessTokenValiditySeconds(3600)
                /*tiempo en que se genera el token renovado*/
                .refreshTokenValiditySeconds(3600);
    }

    /*aca se registra el authenticationManager en el AuthorizationServer, el
    * token storage(el componente que se encarga de guardar el token) que tiene que ser JWT
    * y tmb el AccessTokenConverter que se encarga de guardar los datos que queramos
    * del usuario en el token*/
    /*este configure() esta relacionado con el endpoint de oauth2, del servidor
    * de autorizacion(/oauth/token) que se encarga de generar el token*/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        /*para unir la informacion del accessTokenConverter(username, authorities, informacion del token) con la agregada
        * por nosotros en AdditionalInfoToken/enhance*/
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        /*aca es donde se une la informacion por defecto, y la informacion adicional*/
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(additionalInfoToken, accessTokenConverter()));

        /*se registra el authenticationManager*/
        endpoints.authenticationManager(authenticationManager)
        .tokenStore(tokenStorage())
        /*se registra el tokenConverter*/
        .accessTokenConverter(accessTokenConverter())
        /*aca es donde se agrega la informacion extra al token*/
        .tokenEnhancer(tokenEnhancerChain);
    }

    /*el token storage se encarga de crear el token y almacenarlo, y accessTokenConverter
    se encarga de convertir al token en JWT cargandole sus datos, y darle su firma*/
    @Bean
    public JwtTokenStore tokenStorage() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /*aca se crea el tokenConverter, que se encarga de tomar los datos del usuario, cargarlos,
     y convertirlos en el token JWT codificado en base64, y darle su firma*/
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();

        /*codigo secreto para firmar el token, luego se usa en el servidor de recurso
        * para validar la firma y asi dar acceso a los recursos*/
        tokenConverter.setSigningKey(environment.getProperty("config.security.oauth.jwt.key"));

        return tokenConverter;
    }
}
