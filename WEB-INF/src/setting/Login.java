package setting;

import douyu.mvc.Context;
import douyu.mvc.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class Login {
    public void index(Context c,PrintWriter out) {
        c.out("/setting/loginPage.vm");
    }

    public boolean checkLogin(Context c,HttpSession session,HttpServletResponse response){
        if(session.getAttribute("LOGIN_USER_NAME")==null){
            try {
                response.sendRedirect("/setting/Login");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  false;
        }else{
            return true;
        }
    }

    public void login(Context c,PrintWriter out,String loginUserName,String loginPwd){
        if("fuyan841009".equals(loginUserName) && "841009".equals(loginPwd)){
            c.getHttpServletRequest().getSession().setAttribute("LOGIN_USER_NAME",loginUserName);
            out.print("1");
        }else{
            out.print("0");
        }
    }
}
