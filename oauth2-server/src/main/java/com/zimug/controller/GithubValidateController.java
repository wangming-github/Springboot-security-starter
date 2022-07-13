package com.zimug.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class GithubValidateController {

    @Autowired
    private GithubClientService githubClientService;

    @RequestMapping("/authorization_code")
    public void authorization_code(HttpServletRequest request, HttpServletResponse response, String code) throws ServletRequestBindingException, IOException {
        //github登录验证，并获取access_token
        Map<String, String> resp = githubClientService.queryAccessToken(code);
        //跳转本系统的登录流程，获取用户信息，实现两个系统用户的对接
        String url = "http://localhost:8888/qriver-admin/login/doLogin";
        this.sendByPost(response, url, resp.get("access_token"), "github");
        //this.sendByPost(response, url,"access_token","github");
    }

    public void sendByPost(HttpServletResponse response, String url, String principal, String authType) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println(" <HEAD><TITLE>Post 方法</TITLE></HEAD>");
        out.println(" <BODY>");
        out.println("<form name=\"submitForm\" action=\"" + url + "\" method=\"post\">");

        out.println("<input type=\"hidden\" name=\"principal\" value=\"" + principal + "\"/>");
        out.println("<input type=\"hidden\" name=\"authType\" value=\"" + authType + "\"/>");
        out.println("</from>");
        out.println("<script>window.document.submitForm.submit();</script> ");
        out.println(" </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

}
