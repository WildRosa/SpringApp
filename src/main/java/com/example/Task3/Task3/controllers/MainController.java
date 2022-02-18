package com.example.Task3.Task3.controllers;

import com.example.Task3.Task3.User;
import com.example.Task3.Task3.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;



@Controller
public class MainController {
   @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public String viewHomePage(){
        return"home";
    }

    @GetMapping("/home")
    public String login(){
        return "home";
    }

    @GetMapping("/registration")
    public String showSignUpForm(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/process_registration")
    public String processRegistration(User user){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encoderPassword = encoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        Date date = Date.valueOf(LocalDate.now());
        user.setStatus(true);
        user.setDateRegistration(date);
        userRepository.save(user);
        return "/register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepository.findAll();
        model.addAttribute("listUsers", listUsers);
        return "users";
    }

    @PostMapping("/users")
    public String create(@RequestParam(value = "block", defaultValue = "null") String block,
                         @RequestParam(value = "delete", defaultValue = "null") String delete,
                         @RequestParam(value = "unblock", defaultValue = "null") String unblock,
                         Model model){
        User user;
        if(!block.equals("null")){
            String [] usersForBlock= block.split(",");
            for(String i : usersForBlock){
                user = userRepository.getById((long) Integer.parseInt(i));
               // if(user.isAccountNonLocked()){
                    user.setAccountNonLocked(false);
                    userRepository.save(user);
                    if(SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getEmail())){
                        SecurityContextHolder.getContext().setAuthentication(null);
                    }

               // }


            }
        }
        else if(!unblock.equals("null")){
            String [] usersForUnblock= unblock.split(",");
            for(String i : usersForUnblock){
                user = userRepository.getById((long) Integer.parseInt(i));
                user.setAccountNonLocked(true);
                userRepository.save(user);
            }
        }
        else if(!delete.equals("null")){
            String [] usersForDelete = delete.split(",");
            for(String i : usersForDelete){
                user = userRepository.getById((long) Integer.parseInt(i));
                if(SecurityContextHolder.getContext().getAuthentication().getName().equals(user.getEmail())){
                    SecurityContextHolder.getContext().setAuthentication(null);
                }
                userRepository.deleteById((long) Integer.parseInt(i));

            }

        }

        return "redirect:/users";






    }

}


