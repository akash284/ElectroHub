package com.lcwa.electronic.store.controllers;

import com.lcwa.electronic.store.dtos.JwtRequest;
import com.lcwa.electronic.store.dtos.JwtResponse;
import com.lcwa.electronic.store.dtos.RefreshTokenDto;
import com.lcwa.electronic.store.dtos.UserDto;
import com.lcwa.electronic.store.repositories.RefreshTokenRepository;
import com.lcwa.electronic.store.security.JwtHelper;
import com.lcwa.electronic.store.services.RefreshTokenService;
import org.apache.catalina.User;
import org.apache.catalina.connector.Response;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {



    private Logger logger=LoggerFactory.getLogger(AuthenticationController.class);

    // method to generate token

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserDetailsService userDetailsService;
   @Autowired
   private RefreshTokenService refreshTokenService;


   @PostMapping("/regenerate-token")
   public ResponseEntity<JwtResponse> regenerateToken(@RequestBody RefreshTokenRequest request){

       RefreshTokenDto refreshTokenDto = refreshTokenService.findByToken(request.getRefreshToken());
       RefreshTokenDto refreshTokenDto1 = refreshTokenService.verifyRefreshToken(refreshTokenDto);

       // refresh token expired nahi hua he
       // ab hume jwt token banane ki need he

       // user mil gaya ki uska tha ye refresh token
       UserDto user = refreshTokenService.getUser(refreshTokenDto1);
       UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());


       // generating  new token
       String jwtToken = jwtHelper.generateToken(userDetails);
       JwtResponse response = JwtResponse.builder()
               .token(jwtToken)
               .refreshToken(refreshTokenDto)
               .user(user)
               .build();

      return  ResponseEntity.ok(response);

   }
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        logger.info("Username is : {} and password is {} ",request.getEmail(),request.getPassword());


        // doing authentication
        this.doAuthenticate(request.getEmail(),request.getPassword());

        // valid user he(authenticated hein)

       UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());


        // generate tokens
        String token = jwtHelper.generateToken(userDetails);// it takes userDetails to hum voh lelege

        // refresh token
        RefreshTokenDto refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        //response
        JwtResponse jwtresponse = JwtResponse.
                builder()
                .token(token)
                .user(modelMapper.map( userDetails, UserDto.class))
                .refreshToken(refreshToken)
                .build();
// send karna hein response
        
        return new ResponseEntity<>(jwtresponse, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        try{

            Authentication authentication=new UsernamePasswordAuthenticationToken(email,password);
            authenticationManager.authenticate(authentication);
        }catch(BadCredentialsException ex){
            throw new BadCredentialsException("Invalid Username and Password!!");
        }
    }
}
