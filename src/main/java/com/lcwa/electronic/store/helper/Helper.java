package com.lcwa.electronic.store.helper;

import com.lcwa.electronic.store.dtos.PageableResponse;
import com.lcwa.electronic.store.dtos.UserDto;
import com.lcwa.electronic.store.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    // u ek entity he v ek uska dto he
    // ex u->User v-> userDto


    // is method m hume page and target pass karna hoga ki hume Pageable kis type k chahiye
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){

        // only data
        List<U> entity = page.getContent();

        List<V> dtolist =entity.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        // object bnaya pageableResponse ka  of type V(userDtos ) basically dtos ka
        // kyuki hum dto k hi use kar rahe he data ko idhar se udhar transfer karne k lie

        PageableResponse<V> response=new PageableResponse<>();
        response.setContent(dtolist);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());

        return response;

    }
}
