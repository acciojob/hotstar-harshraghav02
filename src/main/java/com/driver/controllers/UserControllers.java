package com.driver.controllers;


import com.driver.model.User;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserControllers {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public int addUser( User user){
        //Add a user to the database and return the autoGenerated Id by the user
        return userService.addUser(user);
    }

    @GetMapping("/getAvaialbleWebSeries")
    public Integer getAvailableCountOfWebSeriesViewable(@RequestParam("userId")Integer userId){

        return userService.getAvailableCountOfWebSeriesViewable(userId);
    }

}
