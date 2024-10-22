package com.lcwa.electronic.store.services.impl;

import com.lcwa.electronic.store.config.AppConstants;
import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.UserDto;

import com.lcwa.electronic.store.entities.Role;
import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwa.electronic.store.helper.Helper;

import com.lcwa.electronic.store.repositories.RoleRepository;
import com.lcwa.electronic.store.repositories.UserRepository;
import com.lcwa.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    // dbase mein save karne k lie iska object bnaaya
    @Autowired
    private UserRepository userRepository;

    @Autowired  // mapping krne k lie one object to another
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // role assign krne k lie
    @Autowired
    private RoleRepository roleRepository;

    @Value("${user.profile.image.path}")
    private String imagePath;




    @Override
    public UserDto createUser(UserDto userDto) {


        // humein user se id ni leni h khudse generate karege

        // generate unique id in string format
        String userid = UUID.randomUUID().toString();
        userDto.setUserId(userid);


        // ye conversion hum mapper ki help se bhi kar sakte he
        // pr abhi manually karlete he


        // dto to entity conversion
        User user = DtoTOEntity(userDto);
        // password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));


        // assign normal role to user
        // by default jab bhi api se user banayege to usko humlog normal user banayege

        // get the normal role
        Role role=new Role();
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_"+ AppConstants.ROLE_NORMAL);
        Role roleNormal = roleRepository.findByName("ROLE_"+ AppConstants.ROLE_NORMAL).orElse(role);
//        // set the role
        user.setRoles(List.of(roleNormal));

        // this method only takes entity of user type not userDto so conversion needed
        User savedUser = userRepository.save(user);

        // entity to dto
        UserDto userdto1 = EntityToDto(savedUser);

        return userdto1;
    }


    @Override
    public UserDto updateUser(UserDto userDto, String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id not found" + userId));

        // userId and email ko update ki krre abhi k lie par kar sakte email if you want
        user.setName(userDto.getName());
        user.setAbout(userDto.getAbout());
        user.setGender(userDto.getGender());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setImageName(userDto.getImageName());

        //save data
        User updatedUser = userRepository.save(user);

        UserDto updatedDto = EntityToDto(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) throws IOException {

        // get user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id not found" + userId));

        // user delete kare to uski image bhi delete hojani chahiye
        // delete user profile image

        // images/users/imgname.png

        // mene agr ekbr upload ki file then usko serve ki phir delete kito exceptions ara he FileSystemException
        // but agr upload ki then delete ki to delete hora hein
        String fp = imagePath + user.getImageName();

        try{

            Path path= Paths.get(fp);
            Files.delete(path);
        }catch (NoSuchFileException ex){
            logger.error("user image not found  in folder !");
            ex.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        //delete user
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir ) {

        // implementing api sorting
        // pageRequest.of ye sort object leti he to voh pehle sort object  create karna hoga

        // sortDir k based par sort k object create hoga
        Sort sort = (sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());

        // paging krre to make application response faster when there are large number of records and we dont want to sent all of them in once
       // pageNumber defaults starts from 0
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        // findAll pageable object leti he and ye paging and sorting interface m ek method he
        //  ye interface provide methods to retrieve entities using the pagination and sorting abstraction
        Page<User>  page = userRepository.findAll(pageable);


        PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

        // ye ek reusable code h toh hum ise ek function m convert krdege
        // us method ko hum page k object dede and voh humein pageableResponse k object return krde

        // it gives the list or the content

        // ye data jo he voh controller k andr jayega
//        return userDto;
        return response;
    }


    @Override
    public UserDto getUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id not found" + userId));

        UserDto userDto = EntityToDto(user);
        return userDto;
    }


    @Override
    public UserDto getUserbyEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user with given mail id not found" + email));

        return EntityToDto(user);
    }

    @Override
    public List<UserDto> seachUser(String keyword) {

        List<User> allusers = userRepository.findByNameContaining(keyword);

        List<UserDto> dtolist = allusers.stream().map(user -> EntityToDto(user)).collect(Collectors.toList());

        return dtolist;
    }

    private UserDto EntityToDto(User savedUser) {

//        UserDto userdto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .about(savedUser.getAbout())
//                .imageName(savedUser.getImageName())
//                .build();

//        return userdto;

        // directly krdega ye map
         return modelMapper.map(savedUser,UserDto.class);
    }


    private User DtoTOEntity(UserDto userDto) {

//        User user=new User();  isko hum builder se bhi kar skte he
//
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .gender(userDto.getGender())
//                .about(userDto.getAbout())
//                .imageName(userDto.getImageName())
//                .build();
//
//        return user;

        return modelMapper.map(userDto,User.class);
    }
}
