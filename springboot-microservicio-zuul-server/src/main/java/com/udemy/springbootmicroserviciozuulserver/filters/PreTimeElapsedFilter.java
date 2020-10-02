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
public class PreTimeElapsedFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(PreTimeElapsedFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

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
        log.info(request.getMethod().concat(" request enrouteado a ").concat(request.getRequestURL().toString()));

        Long tiempoInicio = System.currentTimeMillis();
        request.setAttribute("tiempoInicio", tiempoInicio);

        return null;
    }
}
