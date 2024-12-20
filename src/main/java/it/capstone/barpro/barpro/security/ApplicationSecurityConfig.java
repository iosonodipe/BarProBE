package it.capstone.barpro.barpro.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
//QUESTA ANNOTAZIONE SERVE A COMUNICARE A SPRING CHE QUESTA  CLASSE Ã¨ UTILIZZATA PER CONFIGURARE LA SECURITY
@EnableWebSecurity()
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    @Bean
    PasswordEncoder stdPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    AuthTokenFilter authenticationJwtToken() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Utilizza la configurazione CORS
                .authorizeHttpRequests(authorize ->
                                authorize //CONFIGURAZIONE DELLA PROTEZIONE DEI VARI ENDPOINT
                                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/users/{username}/avatar").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                                        .requestMatchers(HttpMethod.PUT, "/api/users/{username}/avatar").authenticated()
                                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated() //SOLO UN UTENTE AUTENTICATO PUO MODIFICARE I SUOI DATI

                                        .requestMatchers(HttpMethod.GET, "/api/barmen").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/barmen/{id}").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/barmen/{username}/avatar").permitAll()
                                        .requestMatchers(HttpMethod.PUT, "/api/barmen/{id}").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/barmen").permitAll()
                                        .requestMatchers(HttpMethod.PUT, "/api/barmen/{username}/avatar").authenticated()

                                        .requestMatchers(HttpMethod.GET, "/api/bookings/user/{id}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/bookings/barman/{id}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/bookings").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/bookings").authenticated()
                                        .requestMatchers(HttpMethod.PUT, "/api/bookings/{id}").authenticated()
                                        .requestMatchers(HttpMethod.PATCH, "/api/bookings/{id}/confirm").permitAll()
                                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/{id}").authenticated()

                                        .requestMatchers(HttpMethod.GET, "/api/quotations/byUser/{id}").authenticated()
                                        .requestMatchers(HttpMethod.GET, "/api/quotations").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/quotations").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/quotations/{id}/respond").authenticated()
                                        .requestMatchers(HttpMethod.POST, "/api/quotations/{id}/accept").permitAll()
                                        .requestMatchers(HttpMethod.PUT, "/api/quotations/{id}").authenticated()
                                        .requestMatchers(HttpMethod.DELETE, "/api/quotations/{id}").authenticated()
                                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll()                        //.requestMatchers("/**").authenticated() //TUTTO CIO CHE PUO ESSERE SFUGGITO RICHIEDE L'AUTENTICAZIONE (SERVE A GESTIRE EVENTUALI DIMENTICANZE)
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //COMUNICA ALLA FILTERCHAIN QUALE FILTRO UTILIZZARE, SENZA QUESTA RIGA DI CODICE IL FILTRO NON VIENE RICHIAMATO
                .addFilterBefore(authenticationJwtToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender(@Value("${gmail.mail.transport.protocol}" )String protocol,
                                                @Value("${gmail.mail.smtp.auth}" ) String auth,
                                                @Value("${gmail.mail.smtp.starttls.enable}" )String starttls,
                                                @Value("${gmail.mail.debug}" )String debug,
                                                @Value("${gmail.mail.from}" )String from,
                                                @Value("${gmail.mail.from.password}" )String password,
                                                @Value("${gmail.smtp.ssl.enable}" )String ssl,
                                                @Value("${gmail.smtp.host}" )String host,
                                                @Value("${gmail.smtp.port}" )String port){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        mailSender.setUsername(from);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        props.put("smtp.ssl.enable",ssl);

        return mailSender;
    }

}

