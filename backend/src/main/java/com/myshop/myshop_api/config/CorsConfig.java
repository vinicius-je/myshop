package com.myshop.myshop_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera o frontend (Vite) a chamar a API pelo browser.
 *
 * <p>Por padrão aceita qualquer origem ({@code *}): a porta do Vite muda
 * (5173, 5174...) e a API não usa cookies nem credenciais. Para restringir,
 * defina {@code app.cors.allowed-origins} (padrões separados por vírgula),
 * ex.: {@code https://loja.exemplo.com}.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] origensPermitidas;

    public CorsConfig(@Value("${app.cors.allowed-origins:*}") String[] origensPermitidas) {
        this.origensPermitidas = origensPermitidas;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(origensPermitidas)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
