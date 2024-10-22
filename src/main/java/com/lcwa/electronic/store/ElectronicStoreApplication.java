package com.lcwa.electronic.store;


import com.lcwa.electronic.store.config.AppConstants;
import com.lcwa.electronic.store.entities.Role;
import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.repositories.RoleRepository;
import com.lcwa.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}


	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// jese hi application run kare to 2 role create hojaye
	// ADMIN,NORMAL


	// manually user create krege and use database m store krege with the help of encrypted password
	@Override
	public void run(String... args) throws Exception {


		// Jab bhi hum database mein role store kar rahe hein to
		// ROLE_ prefix se save karege

		// Role ni he tabhi hum save karege warna ni krege

//		yaha se

		Role roleAdmin = roleRepository.findByName("ROLE_"+AppConstants.ROLE_ADMIN).orElse(null);
		System.out.println(roleAdmin);
		if(roleAdmin==null){

			Role role1=new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_"+AppConstants.ROLE_ADMIN);

			roleRepository.save(role1);
		}
		Role roleNormal = roleRepository.findByName("ROLE_"+ AppConstants.ROLE_NORMAL).orElse(null);

		if(roleNormal==null){

			Role role2=new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setName("ROLE_"+AppConstants.ROLE_NORMAL);
			roleRepository.save(role2);
		}

		// ek admin user create krre
		// manually create kar re he not from api se

		User user = userRepository.findByEmail("akash237@gmail.com").orElse(null);

		if(user==null){
			user=new User();
			user.setUserId(UUID.randomUUID().toString());
			user.setName("akash");
			user.setEmail("akash237@gmail.com");
			user.setPassword(passwordEncoder.encode("akash"));
			user.setRoles(List.of(roleAdmin));
			user.setGender("Male");
			user.setAbout("This is admin role");
			user.setImageName("sharma.png");
			System.out.println(user.getEmail());
			System.out.println(user.getPassword());
			userRepository.save(user);

	}

	}
}
