package com.udemy.springbootserviciooauth.security;

import com.udemy.springbootserviciooauth.services.IUserService;
import com.udemy.springbootserviciouserscommons.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component /*para despues inyectarse en el Authorization Server*/
/*interfaz necesaria para añadir informacion al token*/
public class AdditionalInfoToken implements TokenEnhancer {

    @Autowired
    private IUserService userService;

    /*se usa para agregar informacion extra al token ademas del username y las authorities*/
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

        Map<String, Object> info = new HashMap<>();

        /*del oAuth2Authentication podemos obtener informacion del payload del token,
        * y con oAuth2AccessToken podmeos obtener informacion del token en si*/
        User user = userService.findByUsername(oAuth2Authentication.getName());

        /*informacion a cargar en el token*/
        info.put("name", user.getName());
        info.put("surname", user.getSurname());
        info.put("email", user.getEmail());

        /*Se usa DefaultOAuth2AccessToken ya que esta es la implementacion concreta de la
        * interfaz OAuth2AccessToken*/
        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }
}
