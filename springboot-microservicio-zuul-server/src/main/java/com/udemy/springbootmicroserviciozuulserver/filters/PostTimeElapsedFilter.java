package com.udemy.springbootmicroserviciozuulserver.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/*extendiendo de ZuulFilter la clase se registra como un filtro de Zuul*/
@Component
public class PostTimeElapsedFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(PostTimeElapsedFilter.class);

    @Override
    public String filterType() {
        return "post";
    } /*palabra clave 'post'*/

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true; /*aca se define si se ejecuta el metodo run o no, poniendo solo true
        se ejecuta siempre el metodo run*/
    }

    @Override /*aca va la logica*/
    public Object run() throws ZuulException {

        RequestContext ctx = RequestContext.getCurrentContext(); /*con el context obtenemos la request*/
        HttpServletRequest request = ctx.getRequest();
        log.info("Entrando a post filter");

        Long tiempoInicio = (Long) request.getAttribute("tiempoInicio");
        Long tiempoFinal = System.currentTimeMillis();
        Long tiempoTranscurrido = tiempoFinal - tiempoInicio;

        log.info("El tiempo transcurrido en segundos fue: " + tiempoTranscurrido.doubleValue()/1000);

        return null;
    }
}
