package cn.appsys.controller;

import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.AppInfoService;
import cn.appsys.service.BackendUserService;
import cn.appsys.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
@SessionAttributes(value = {"userSession"})
public class BackendUserController {
    @Autowired
    private BackendUserService backendUserService;
    @Autowired
    private DataDictionaryService dataDictionaryService;

    @RequestMapping("/login")
    public String login(){
        return "backendlogin";
    }
    @RequestMapping("/dologin")
    public String dologin(@RequestParam(value = "userCode")String userCode,
                          @RequestParam(value = "userPassword") String userPassword,
                          Model model, HttpSession session){
        System.out.println("ctr1");
        BackendUser backendUser=backendUserService.findBackendUser(userCode,userPassword);
        System.out.println("ctr2");
        DataDictionary dataDictionary=null;
        if (backendUser!=null){
           dataDictionary=dataDictionaryService.findData(backendUser.getUserType());
           backendUser.setUserTypeName(dataDictionary.getValueName());
          session.setAttribute("userSession",backendUser);
            System.out.println(backendUser.toString());
            return "backend/main";
        }else {
            model.addAttribute("error","账号或密码错误");
            model.addAttribute("user",backendUser);
            return "backendlogin";
        }

    }
    @RequestMapping("/logout" )//注销
    public String logout(HttpSession session){
        session.invalidate();
        return "backendlogin";
    }
    @RequestMapping("/backend/main")
    public String main(HttpSession session){
        if (session.getAttribute("userSession")==null){
            return "redirect:/manager/login";
        }
        return "backend/main";
    }
}
