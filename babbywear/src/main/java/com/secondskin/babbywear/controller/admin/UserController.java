package com.secondskin.babbywear.controller.admin;


import com.secondskin.babbywear.model.UserInfo;
import com.secondskin.babbywear.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;






    @GetMapping("/user-panel")
    public String showUser(@RequestParam(defaultValue = "0")int pageNo,
                           @RequestParam(defaultValue = "10")int pageSize,
                           Model model){
        Page<UserInfo> page = userService.findAll(pageNo,pageSize);
        model.addAttribute("users",page);
        return "admin/user-list";
    }


   @GetMapping("/block/{id}")
   public String blockAndUnblockUser(@PathVariable("id") Long id){
       userService.blockById(id);
       return "redirect:/user/user-panel";


   }

   @GetMapping("/unblock/{id}")
    public String unblockUser(@PathVariable Long id){
       userService.unBlockById(id);
       return "redirect:/user/user-panel";
   }
}
