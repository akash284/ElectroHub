package com.lcwa.electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //so that hum is class ko kahi bhi autowired kar sake
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    Logger logger= LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // ye api se pehle chalega
        // jwt token  mein jo header ara he usko verify karne k lie

        // data comes in below format
        // KEY :VALUE
        // AUTHORIZATION : BEARER ASFDGHJKL;ASDFHKLJLASADFHLK;FQEREPIOROEZNCXV
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {}: ",requestHeader);

        String username=null;
        String token=null;

        if(requestHeader!=null && requestHeader.startsWith("Bearer")){
            //valid jwt header he
            token=requestHeader.substring(7);

            try{
                username=jwtHelper.getUsernameFromToken(token);
                logger.info("Token username is  {} :",username);
            }catch(IllegalArgumentException ex){
                logger.info("Illegal argument while fetching the username  :"+ex.getMessage());
            }catch (ExpiredJwtException ex){
                logger.info("Given JWT is expired "+ex.getMessage());
            }catch (MalformedJwtException ex){
                logger.info("some changes has been done in token !! Invalid Token "+ ex.getMessage());
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
        else{
            logger.info("Invalid Header !! Header is not starting with Bearer");
        }

        // if username available he and pehle se securityContext mein koi authentication he ya nahi he
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            // username mein kuch hein
            // authentication== null mean abhi tak authentication  ni he
            // SecurityContext mein authentication set ni heto  use security framework accesss ni karne dega
             // securityAuthentication Context daldege tabhi api access hogi warna nahi

            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // ye milgya to ab hum check kar skte ye token/user valid he ya nai


            // validate token
            if(username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){

                // valid token he
                // SecurityContext k andar authentication set karenge
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //the WebAuthenticationDetails containing information about the current request
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }
        // doFilter request ko aage bhejdega api tak and itna sabkuch upr chala hoga to username nikl gya hoga and authentication set hogya hoga
        // upar wali if conditions ni chali to request jayegi to but security k andar data set ni hoga and aap apis ko accesss ni karpoage
       filterChain.doFilter(request,response);
    }
}
