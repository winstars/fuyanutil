package setting;

import douyu.mvc.Controller;
import douyu.mvc.Context;

import java.util.ResourceBundle;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import utils.DBC;
import models.AreaAndDrink;

@Controller
public class Index2 {
	//private static final ResourceBundle RB = ResourceBundle.getBundle("LocalStrings");

	public void index(PrintWriter out) {
		//out.println(RB.getString("error.message.1"));
	}

	public void settingAreaAndDrink(Context c){
		List<AreaAndDrink> aads = new ArrayList<AreaAndDrink>();
		try{
			DBC dbc = DBC.getInstance();
			String[][] arrs = dbc.getArr("select ID,AREAID,AREANAME,DRINKNAME from AREAANDDRINK order by AREAID,ID");
			if(arrs!=null){
				for(String[] arr : arrs){
					AreaAndDrink aad = new AreaAndDrink();
					aad.setId(Integer.parseInt(arr[0]));
					aad.setAreaId(Integer.parseInt(arr[1]));
					aad.setAreaName(arr[2]);
					aad.setDrinkName(arr[3]);
					aads.add(aad);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		c.out("/title.vm");
		c.out("/type1/typeset.vm");
		c.out("/setting/SettingAreaAndDrink.vm");
	}

	public void add(PrintWriter out, Context c,int inputAreaId,String inputAreaName,String inputDrinkName){
		System.out.println(inputAreaId+","+inputAreaName+","+inputDrinkName);
		
		try{
			DBC dbc = DBC.getInstance();
			dbc.insertDB("insert into AREAANDDRINK (AREAID,AREANAME,DRINKNAME) values ("+inputAreaId+",\'"+inputAreaName+"\',\'"+inputDrinkName+"\')");
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
			dbc.deleteDB("delete from AREAANDDRINK where ID=\'"+id+"\'");
		}catch(Exception e){
			e.printStackTrace();
		}
		/**
		try{
			c.getHttpServletResponse().sendRedirect("/setting/Index.SettingAreaAndDrink");
		}catch(Exception e){
			e.printStackTrace();
		}
		**/
		out.print("1");
	}
}