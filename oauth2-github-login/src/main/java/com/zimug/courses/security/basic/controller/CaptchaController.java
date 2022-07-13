package com.zimug.courses.security.basic.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wf.captcha.SpecCaptcha;
import com.zimug.commons.content.ResponseType;
import com.zimug.courses.security.basic.auth.imagecode.CaptchaCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 实现的效果是，页面初始化即加载验证码。以后每一次点击，都会更新验证码。
 * 注意：一定设置width和height，否则图片无法显示。
 * 需要为“/kaptcha”配置permitAll公开访问权限，否则无法访问到
 *
 * @author maizi
 */
@RestController
@Slf4j
public class CaptchaController {

    @Resource
    DefaultKaptcha captchaProducer;

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/kaptcha", method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");

        String capText = captchaProducer.createText();
        CaptchaCode captchaImageVO = new CaptchaCode(capText, 2 * 60);
        //将验证码存到session
        log.info("将验证码[{}]存到session", captchaImageVO.toString());
        session.setAttribute(ResponseType.CAPTCHA_SESSION_KEY, captchaImageVO);

        //将图片返回给前端
        try (ServletOutputStream out = response.getOutputStream();) {
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "jpg", out);
            out.flush();
        }//使用try-with-resources不用手动关闭流
    }

    @RequestMapping("/easy-captcha")
    public void hello(HttpServletResponse response) throws IOException {
        // png类型
        SpecCaptcha captcha = new SpecCaptcha(130, 48);
        String text = captcha.text();// 获取验证码的字符
        char[] chars = captcha.textChar();// 获取验证码的字符数组

        System.out.println("验证码：" + text);
        System.out.println(chars);
        // 输出验证码
        captcha.out(response.getOutputStream());
    }

}