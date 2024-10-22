package com.lcwa.electronic.store;

import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.repositories.UserRepository;
import com.lcwa.electronic.store.security.JwtHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void testToken(){
		System.out.println("Testing jwt token");

		User user = userRepository.findByEmail("akash237@gmail.com").get();  //kyuki ye use available he

		String generated_jwt_token = jwtHelper.generateToken(user);
		System.out.println(generated_jwt_token);

		System.out.println("Getting username from token");
		System.out.println(jwtHelper.getUsernameFromToken(generated_jwt_token));

		System.out.println("Token Exipred ?");
		System.out.println(jwtHelper.isTokenExpired(generated_jwt_token));

	}

}
