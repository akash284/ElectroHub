package com.lcwa.electronic.store.security;

// this class is used to perform jwt operations
// like
// generate JWT toke, Validate Token
// username niklna ho token se


import com.lcwa.electronic.store.dtos.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.catalina.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

    // Requirements : token ko generate karne k lie requirements kya kya he

    //1 validity : token  kitni der tak token valid rahega

    // validity in milli seconds
    public static final long TOKEN_VALIDITY=5*60*60*1000; //5 HOUR TO MILLISECOND

    // 2 secret key     : token ko banane k lie secret key use hogi
    //  ye kuch bhi rakh sakte hein

    // not reccomended to put here as its secret key badmein applcation.properties mein rakhdege
    public static final String SECRET_KEY="asdfghjklqwertyuiasdfghjklqwertyuiasdfghjklqwertyuiasdfghjklqwertyuiasdfghjklqwertyuiopzxcvbnmasdfghjklasdfghjlkasdfghjasdfghjklqwertyuiopzxcvbnmasdfghjklasdfghjlkasdfghjasdfghjklqwertyuiopzxcvbnmasdfghjklasdfghjlkasdfghjklqwertyuiopzxcvbnmasdfghjkl";


    // retrieve username from jwt token  -- > claims-->means data,statement
    public String getUsernameFromToken(String token){

        return getClaimFromToken(token,Claims::getSubject);

    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims=getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need secret key
    private Claims getAllClaimsFromToken(String token){

        //Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();  used earlier but got depreciated
        // but now builder need to be constructed first then we can call .parseClaimsJws

        // if api version is 0.something then this will work but in future if 1.something comes then we will not be able to do with this
        Claims body= Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getPayload();

        // in future versions 1.something
        // latest
        // ye use karre to niche bhi change karna padri thi islie abih ise ese hi rehnde do
//        SignatureAlgorithm hs512 = SignatureAlgorithm.HS512;
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), hs512.getJcaName());
//        Claims body = Jwts.parser().verifyWith(secretKeySpec).build().parseClaimsJws(token).getPayload();

        return body;
    }

    //check if the token has expired or not
    public boolean isTokenExpired(String token){
        final Date expiration=getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // retrive expiration date from jwt token
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    // generate token for user
    public String generateToken(UserDetails userDetails){
        
        Map<String,Object> claims=new HashMap<>();
        return doGenerateToken(claims,userDetails.getUsername());  //userDetails k pass sari info he get username,password
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

         return Jwts.builder()
                 .setClaims(claims)
                 .setSubject(subject)
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_VALIDITY))
                 .signWith(SignatureAlgorithm.HS512,SECRET_KEY).compact();
    }


}
