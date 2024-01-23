package org.morriswa.eecs447.control;

import org.morriswa.eecs447.dao.UserProfileDao;
import org.morriswa.eecs447.model.RegisterUser;
import org.morriswa.eecs447.utility.HttpResponseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {
    private final UserProfileDao userProfileDao;
    private final HttpResponseFactory response;
    @Autowired public UserProfileController(UserProfileDao userProfileDao, HttpResponseFactory response){
        this.userProfileDao = userProfileDao;
        this.response = response;
    }

    @PostMapping("register") public ResponseEntity<?> createUser(@RequestBody RegisterUser request){
        userProfileDao.registerUser(request.username(), request.password());
        return response.build(HttpStatus.OK, "User has been registered");
    }
}
