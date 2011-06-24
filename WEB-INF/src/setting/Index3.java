package setting;

import douyu.mvc.Controller;
import douyu.mvc.Context;

import java.util.ResourceBundle;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import utils.DBC;
import models.GroupAndUser2;

@Controller
public class Index3 {
	//private static final ResourceBundle RB = ResourceBundle.getBundle("LocalStrings");

	public void index(PrintWriter out) {
		//out.println(RB.getString("error.message.1"));
	}

	public void settingGroupAndUser(Context c){
		List<GroupAndUser2> gaus = new ArrayList<GroupAndUser2>();
		try{
			DBC dbc = DBC.getInstance();
			String[][] arrs = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME,GROUPTYPE from GROUPANDUSER2 order by GROUPTYPE,GROUPID,ID");
			if(arrs!=null){
				for(String[] arr : arrs){
					GroupAndUser2 gau = new GroupAndUser2();
					gau.setId(Integer.parseInt(arr[0]));
					gau.setGroupId(Integer.parseInt(arr[1]));
					gau.setGroupName(arr[2]);
					gau.setUserName(arr[3]);
					gau.setGroupType(Integer.parseInt(arr[4]));
					gaus.add(gau);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		c.out("/title.vm");
        c.out("/type2/typeset.vm");
		c.out("/setting/SettingGroupAndUser2.vm");
	}

	public void add(PrintWriter out, Context c,int inputGroupId,String inputGroupName,String inputUserName,int inputGroupType){
		//System.out.println(inputGroupId+","+inputGroupName+","+inputUserName);
		
		try{
			DBC dbc = DBC.getInstance();
			dbc.insertDB("insert into GROUPANDUSER2 (GROUPID,GROUPNAME,USERNAME,GROUPTYPE) values ("+inputGroupId+",\'"+inputGroupName+"\',\'"+inputUserName+"\',"+inputGroupType+")");
		}catch(Exception e){
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

	public void del(PrintWriter out,Context c,int id){
		try{
			DBC dbc = DBC.getInstance();
			dbc.deleteDB("delete from GROUPANDUSER2 where ID=\'"+id+"\'");
		}catch(Exception e){
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