package com.lcwa.electronic.store.controllers;

import com.lcwa.electronic.store.dtos.ApiResponseMessage;
import com.lcwa.electronic.store.dtos.ImageResponse;
import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.UserDto;
import com.lcwa.electronic.store.entities.User;
import com.lcwa.electronic.store.services.FileService;
import com.lcwa.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")

public class UserController {


    Logger logger= LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;


    // to accept the image and transfer it to dto
    @Autowired
    private FileService fileService;
    //create user

    @Value("${user.profile.image.path}")
    private String imageUploadPath;

    // data ko accept krege client se
    // @Valid annotation to enable the validation so that it can check how the data is coming
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {

        UserDto userdto1 = userService.createUser(userDto);

        return new ResponseEntity<>(userdto1, HttpStatus.CREATED);

    }

    //update
    // yaha par bhi data accept krre he to yaha bhi validation lgayge
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,
                                              @Valid @RequestBody UserDto userDto) {

        UserDto updatedUserDto = userService.updateUser(userDto, userId);

        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);

    }

    //delete
//    @DeleteMapping("{/userId}")
//    public ResponseEntity<String> deleteUser(@PathVariable String userId){

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId) throws IOException {

        userService.deleteUser(userId);

        ApiResponseMessage message =
                ApiResponseMessage
                        .builder()
                        .message("User is deleted succesfully")
                        .success(true)
                        .status(HttpStatus.OK)
                        .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // get all users
    // applied pagination also
    //response m ab hum pageNumber,pageSize,totalElements,islastPage ye sab bhi bhejre he islie pageableResponse type k object create kia
    // sari request controller par ati he islie yha pe sari field lelie like pageNumber,pageSize
    @GetMapping
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNUmber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "ASC", required = false) String sortDir
    ) {
        PageableResponse<UserDto> allUser = userService.getAllUser(pageNUmber, pageSize, sortBy, sortDir);

        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    // get single user
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {

        UserDto user = userService.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    // get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getbyEmail(@PathVariable String email) {

        UserDto userbyEmail = userService.getUserbyEmail(email);

        return new ResponseEntity<>(userbyEmail, HttpStatus.OK);
    }
    //search user

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keyword) {

        List<UserDto> userDtos = userService.seachUser(keyword);

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    // upload user image
    // response mein image name return kruga AND SATH MEIN OR BHI INFO
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> UploadUserImage(@RequestParam("userImage") MultipartFile image,
                                                         @PathVariable String userId) throws IOException {

        String imageName = fileService.UploadFile(image, imageUploadPath);

        // get the user
        UserDto user = userService.getUser(userId);
        // set the image name
        user.setImageName(imageName);
        //updated the user k corresponding image
        UserDto userDto = userService.updateUser(user,userId);

        ImageResponse imageResponse = ImageResponse
                .builder()
                .imageName(imageName)
                .success(true)
                .message("Image is uploaded succesfully")
                .status(HttpStatus.CREATED)
                .build();

        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);


    }

    // Serve user image

    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {

        // GET USER
        UserDto user = userService.getUser(userId);
        logger.info("user image name {} :",user.getImageName());
        // GET THE IMAGE
        InputStream resources = fileService.getResources(imageUploadPath, user.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        // COPY THE DATA
        StreamUtils.copy(resources,response.getOutputStream());

    }
}
