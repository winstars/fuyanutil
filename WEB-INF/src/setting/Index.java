package setting;

import douyu.mvc.Controller;
import douyu.mvc.Context;

import java.util.ResourceBundle;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import utils.DBC;
import models.GroupAndUser;

@Controller
public class Index {
    //private static final ResourceBundle RB = ResourceBundle.getBundle("LocalStrings");

    public void index(PrintWriter out) {
        //out.println(RB.getString("error.message.1"));
    }

    public void settingGroupAndUser(Context c) {
        List<GroupAndUser> gaus = new ArrayList<GroupAndUser>();
        try {
            DBC dbc = DBC.getInstance();
            String[][] arrs = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME from GROUPANDUSER order by GROUPID,ID");
            if (arrs != null) {
                for (String[] arr : arrs) {
                    GroupAndUser gau = new GroupAndUser();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gaus.add(gau);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.out("/title.vm");
        c.out("/type1/typeset.vm");
        c.out("/setting/SettingGroupAndUser.vm");
    }

    public void add(PrintWriter out, Context c, int inputGroupId, String inputGroupName, String inputUserName) {
        System.out.println(inputGroupId + "," + inputGroupName + "," + inputUserName);

        try {
            DBC dbc = DBC.getInstance();
            dbc.insertDB("insert into GROUPANDUSER (GROUPID,GROUPNAME,USERNAME) values (" + inputGroupId + ",\'" + inputGroupName + "\',\'" + inputUserName + "\')");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         try{
         c.getHttpServletResponse().sendRedirect("/setting/Index.SettingGroupAndUser");
         }catch(Exception e){
         e.printStackTrace();
         }**/
        out.print("1");

    }

    public void del(PrintWriter out, Context c, int id) {
        try {
            DBC dbc = DBC.getInstance();
            dbc.deleteDB("delete from GROUPANDUSER where ID=\'" + id + "\'");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         try{
         c.getHttpServletResponse().sendRedirect("/setting/Index.SettingGroupAndUser");
         }catch(Exception e){
         e.printStackTrace();
         }
         **/
        out.print("1");
    }
}