package cn.appsys.controller;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dev")
@SessionAttributes(value = {"devUserSession"})
public class DevUserController {
    @Autowired
    private DevUserService devUserService;


    @RequestMapping("/login")
    public String login(){
        return "devlogin";
    }
    @RequestMapping("dologin")
    public String dologin(@RequestParam(value="devCode")String devCode,@RequestParam(value="devPassword")String devPassword, Model model){
        System.out.println("contorller1");
        DevUser devUser=null;
        try {
            devUser=devUserService.findDevUser(devCode,devPassword);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (devUser!=null){
            model.addAttribute("devUserSession",devUser);
            return "redirect:/dev/flatform/main";

        }else {
            model.addAttribute("error","账号或密码错误");
            model.addAttribute("user",devUser);
            return "devlogin";
        }


    }
    @RequestMapping("/logout" )//注销
    public String logout(HttpSession session){
        session.invalidate();
        return "devlogin";
    }
    @RequestMapping("/flatform/main")
    public String tomain(HttpSession session){
        if (session.getAttribute("devUserSession")==null){
            return "redirect:/dev/login";
        }
        return "developer/main";
    }
}


