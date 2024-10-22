package com.lcwa.electronic.store.services;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.UserDto;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


/* NOTES : HUM DIRECTLY entities ko use ni krege data ko transfer karne k lie


hum entities k use ni krege data ko transfer karne k lie bcz entities represent karti persistent data ko
entity ki help  se hum data ko database tak leja sakte he
database se nikal sakte hein
pr agr hume data ko controller se service tak lejana he to hum entity ko use ni krege  hum dto k use  krege
hum use Krege Dto--> Data Transfer Object



 */

public interface UserService {


    // create user

    // ye User ek entity he to ye ek data ko pass karne k lie bhi use hora hein
    // and ye entity humare persistent data ko represent karri he (database k andr table ko bhi)


    // is method ko hum controller mein use krege   to controller k andar user ko pass krege and user class apne data ko leke aaygi service layer tak

    UserDto createUser(UserDto userDto);

    //update user

    UserDto updateUser(UserDto userDto, String userId);

    // delete user
    void deleteUser(String userId) throws IOException;

    // get all users
    // response m or bhi chiz bhejne k lie
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    // get single user by id
    UserDto getUser(String userId);

    //get single user by email
    UserDto getUserbyEmail(String email);

    // search user

    List<UserDto> seachUser(String keyword);
}
