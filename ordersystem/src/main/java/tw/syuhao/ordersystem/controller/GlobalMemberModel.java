package tw.syuhao.ordersystem.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;
import tw.syuhao.ordersystem.entity.Users;

@ControllerAdvice
public class GlobalMemberModel {
    @ModelAttribute("member")
    public Users exposeMember(HttpSession session) {
        Users u = (Users) session.getAttribute("user");
        if (u != null) u.setPassword(null);
        return u;
    }
}
