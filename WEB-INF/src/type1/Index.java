package type1;


import douyu.mvc.Controller;
import douyu.mvc.Context;

//import java.util.ResourceBundle;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.ResourceBundle;
//import java.io.PrintWriter;
//import java.io.File;
import java.io.*;
import java.util.*;
import java.net.*;
import javax.servlet.http.*;
import javax.servlet.*;

import org.joda.time.DateTime;

import jxl.*;
import jxl.biff.*;
import jxl.write.*;

import models.GroupAndUser;
import models.AreaAndDrink;
import models.UserDrinkData;
import utils.DBC;

@Controller
public class Index {
    //private static final ResourceBundle RB = ResourceBundle.getBundle("LocalStrings");

    public void index(PrintWriter out) {
        //out.println(RB.getString("error.message.1"));
    }

    public void showInputGrid(Context c, int year, int month) {
        List<GroupAndUser> gaus = new LinkedList<GroupAndUser>();
        List<AreaAndDrink> aads = new LinkedList<AreaAndDrink>();
        Map<String, LinkedHashMap> urdMap = new LinkedHashMap<String, LinkedHashMap>();
        DateTime today = new DateTime();
        if (year == 0) {
            year = today.getYear();
        }
        if (month == 0) {
            month = today.getMonthOfYear();
        }
        try {
            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select ID,SYEAR,SMONTH,USERID,DRINKID,NUM from USERDRINKDATA where SYEAR=\'" + year + "\' and SMONTH=\'" + month + "\'");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME from GROUPANDUSER order by GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select ID,AREAID,AREANAME,DRINKNAME from AREAANDDRINK order by AREAID,ID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser gau = new GroupAndUser();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gaus.add(gau);
                }
            }

            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    AreaAndDrink aad = new AreaAndDrink();
                    aad.setId(Integer.parseInt(arr[0]));
                    aad.setAreaId(Integer.parseInt(arr[1]));
                    aad.setAreaName(arr[2]);
                    aad.setDrinkName(arr[3]);
                    aads.add(aad);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    LinkedHashMap urdMap2 = urdMap.get(arr[3]);
                    if (urdMap2 == null) {
                        urdMap2 = new LinkedHashMap<String, Integer>();
                        urdMap.put(arr[3] + "", urdMap2);
                    }
                    if (arr[5] == null || "".equals(arr[5]) || "0".equals(arr[5])) arr[5] = "";
                    urdMap2.put(arr[4] + "", arr[5]);
                }

                //System.out.println("outter map size is "+urdMap.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.out("/title.vm");
        c.out("/type1/typeset.vm");
        c.out("/type1/showInputGrid.vm");
    }

    public void showInputGrid4Year(Context c, int year) {
        List<GroupAndUser> gaus = new LinkedList<GroupAndUser>();
        List<AreaAndDrink> aads = new LinkedList<AreaAndDrink>();
        Map<String, LinkedHashMap> urdMap = new LinkedHashMap<String, LinkedHashMap>();
        DateTime today = new DateTime();
        if (year == 0) {
            year = today.getYear();
        }
        try {
            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select USERID,DRINKID,sum(NUM) as SNUM from USERDRINKDATA where SYEAR=\'" + year + "\' group by USERID,DRINKID");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME from GROUPANDUSER order by GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select ID,AREAID,AREANAME,DRINKNAME from AREAANDDRINK order by AREAID,ID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser gau = new GroupAndUser();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gaus.add(gau);
                }
            }

            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    AreaAndDrink aad = new AreaAndDrink();
                    aad.setId(Integer.parseInt(arr[0]));
                    aad.setAreaId(Integer.parseInt(arr[1]));
                    aad.setAreaName(arr[2]);
                    aad.setDrinkName(arr[3]);
                    aads.add(aad);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    LinkedHashMap urdMap2 = urdMap.get(arr[0]);
                    if (urdMap2 == null) {
                        urdMap2 = new LinkedHashMap<String, Integer>();
                        urdMap.put(arr[0] + "", urdMap2);
                    }
                    if (arr[2] == null || "".equals(arr[2]) || "0".equals(arr[2])) arr[2] = "";
                    urdMap2.put(arr[1] + "", arr[2]);
                }

                //System.out.println("outter map size is "+urdMap.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.out("/title.vm");
        c.out("/type1/typeset.vm");
        c.out("/type1/showInputGrid4Year.vm");
    }

    /**
     * public void showInputGrid(Context c){
     * DateTime today = new DateTime();
     * int year = today.getYear();
     * int month = today.getMonthOfYear();
     * System.out.println(year+"_"+month);
     * showInputGrid(c,year,month);
     * }
     * <p/>
     * public void showInputGrid(Context c,int month){
     * DateTime today = new DateTime();
     * int year = today.getYear();
     * showInputGrid(c,year,month);
     * }
     */
    public void update(PrintWriter out, Context c, int year, int month, String id, String num) {
        if (num == null || "".equals(num)) num = "0";
        if (id != null && !"".equals(id)) {
            String[] ids = id.split("_");
            try {
                DBC dbc = DBC.getInstance();
                String[][] arr = dbc.getArr("select * from USERDRINKDATA where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + ids[0] + " and DRINKID=" + ids[1]);
                if (arr == null || arr.length == 0) {
                    dbc.insertDB("insert into USERDRINKDATA(SYEAR,SMONTH,USERID,DRINKID,NUM)values(" + year + "," + month + "," + ids[0] + "," + ids[1] + "," + num + ")");
                } else {
                    dbc.updateDB("update USERDRINKDATA set NUM=" + num + " where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + ids[0] + " and DRINKID=" + ids[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            out.print("1");
        }
    }

    public void exportExcel(PrintWriter out, Context c, int year, int month) {
        try {
            List<GroupAndUser> gaus = new LinkedList<GroupAndUser>();
            List<AreaAndDrink> aads = new LinkedList<AreaAndDrink>();
            Map<String, LinkedHashMap<String, String>> urdMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
            Map<String, String> urdMapByGau = new LinkedHashMap<String, String>();
            Map<String, String> urdMapByAad = new LinkedHashMap<String, String>();
            int total = 0;
            DateTime today = new DateTime();
            if (year == 0) {
                year = today.getYear();
            }
            if (month == 0) {
                month = today.getMonthOfYear();
            }

            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select ID,SYEAR,SMONTH,USERID,DRINKID,NUM from USERDRINKDATA where SYEAR=\'" + year + "\' and SMONTH=\'" + month + "\'");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME from GROUPANDUSER order by GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select ID,AREAID,AREANAME,DRINKNAME from AREAANDDRINK order by AREAID,ID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser gau = new GroupAndUser();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gaus.add(gau);
                }
            }

            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    AreaAndDrink aad = new AreaAndDrink();
                    aad.setId(Integer.parseInt(arr[0]));
                    aad.setAreaId(Integer.parseInt(arr[1]));
                    aad.setAreaName(arr[2]);
                    aad.setDrinkName(arr[3]);
                    aads.add(aad);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    LinkedHashMap<String, String> urdMap2 = urdMap.get(arr[3]);
                    if (urdMap2 == null) {
                        urdMap2 = new LinkedHashMap<String, String>();
                        urdMap.put(arr[3] + "", urdMap2);
                    }
                    if (arr[5] == null || "".equals(arr[5]) || "0".equals(arr[5])) {//忽略统计
                        arr[5] = "";//将数值为null或者为0置为空
                    } else {
                        //GAU合计
                        String relAdd = urdMapByGau.get(arr[3]);
                        if (relAdd == null || "".equals(relAdd) || relAdd.length() < 1) {//尚未放入数据
                            relAdd = "0";
                        }
                        urdMapByGau.put(arr[3], (Integer.parseInt(relAdd) + Integer.parseInt(arr[5])) + "");
                        //AAD合计
                        relAdd = urdMapByAad.get(arr[4]);
                        if (relAdd == null || "".equals(relAdd) || relAdd.length() < 1) {//尚未放入数据
                            relAdd = "0";
                        }
                        urdMapByAad.put(arr[4], (Integer.parseInt(relAdd) + Integer.parseInt(arr[5])) + "");
                        total += Integer.parseInt(arr[5]);

                        //System.out.println("gau="+arr[3]+" and value="+urdMapByGau.get(arr[3]));
                        //System.out.println("aad="+arr[4]+" and value="+urdMapByAad.get(arr[4]));
                    }
                    urdMap2.put(arr[4] + "", arr[5]);
                }

                //System.out.println("outter map size is "+urdMap.size());
            }

            String baseDir = "c:\\outputexcels";
            File baseDirF = new File("c:\\outputexcels");
            if (!baseDirF.exists()) {
                baseDirF.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File xlsFile = new File(baseDir + "\\" + uuid.toString() + ".xls");
            FileOutputStream fos = new FileOutputStream(xlsFile);
            WritableWorkbook wwb = Workbook.createWorkbook(fos);
            WritableSheet sheet = wwb.createSheet(month + "", 0);

            int baseCol = 2;
            int baseRow = 1;
            for (int i = 0; i < aads.size(); i++) {
                Label label = new Label(baseCol + i, baseRow, aads.get(i).getAreaName());//区域行
                sheet.addCell(label);
            }
            sheet.addCell(new Label(0, 2, "部门及岗位"));
            sheet.addCell(new Label(1, 2, "姓名"));
            baseCol = 2;
            baseRow = 2;
            int lastCol = 0;
            for (int i = 0; i < aads.size(); i++) {
                lastCol = i;
                Label label = new Label(baseCol + i, baseRow, aads.get(i).getDrinkName());//酒名行
                sheet.addCell(label);
            }
            sheet.addCell(new Label(baseCol + lastCol + 1, baseRow, "合计"));//合计列抬头

            //for(String a : urdMapByGau.values()){
            //	System.out.println(a);
            //}

            baseCol = 0;
            baseRow = 3;

            int lastRow = 0;
            for (int i = 0; i < gaus.size(); i++) {
                lastRow = baseRow + i;
                sheet.addCell(new Label(baseCol, baseRow + i, gaus.get(i).getGroupName()));
                sheet.addCell(new Label(baseCol + 1, baseRow + i, gaus.get(i).getUserName()));
                if (urdMap != null && urdMap.size() > 0) {
                    LinkedHashMap<String, String> urdMap2 = urdMap.get(gaus.get(i).getId() + "");
                    for (int j = 0; j < aads.size(); j++) {
                        if (urdMap2 != null && urdMap2.size() > 0) {
                            String val = urdMap2.get(aads.get(j).getId() + "");
                            if (val != null && !"".equals(val) && val.length() != 0 && !"0".equals(val)) {
                                sheet.addCell(new Label(baseCol + 2 + j, baseRow + i, val));//基准列0，跨过部门、姓名列，酒类某列
                            }
                        }
                    }
                    sheet.addCell(new Label(lastCol + baseCol + 2 + 1, baseRow + i, urdMapByGau.get(gaus.get(i).getId() + "")));//合计列填值
                }
            }

            sheet.addCell(new Label(1, lastRow + 1, "合计"));//合计行
            for (int i = 0; i < aads.size(); i++) {
                sheet.addCell(new Label(i + 2, lastRow + 1, urdMapByAad.get(aads.get(i).getId() + "")));
            }

            sheet.addCell(new Label(2 + aads.size(), 3 + gaus.size(), total + ""));//最终合计数

            wwb.write();
            wwb.close();

            outputFile(c, xlsFile, xlsFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出1-本月
     */
    public void exportExcel4Currency(PrintWriter out, Context c) {
        try {
            List<GroupAndUser> gaus = new LinkedList<GroupAndUser>();
            List<AreaAndDrink> aads = new LinkedList<AreaAndDrink>();
            Map<String, LinkedHashMap<String, String>> urdMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
            Map<String, String> urdMapByGau = new LinkedHashMap<String, String>();
            Map<String, String> urdMapByAad = new LinkedHashMap<String, String>();
            int total = 0;
            DateTime today = new DateTime();
            int year = today.getYear();
            int month = today.getMonthOfYear();

            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select USERID,DRINKID,sum(NUM) as SNUM from USERDRINKDATA where SYEAR=\'" + year + "\' group by USERID,DRINKID");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME from GROUPANDUSER order by GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select ID,AREAID,AREANAME,DRINKNAME from AREAANDDRINK order by AREAID,ID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser gau = new GroupAndUser();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gaus.add(gau);
                }
            }

            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    AreaAndDrink aad = new AreaAndDrink();
                    aad.setId(Integer.parseInt(arr[0]));
                    aad.setAreaId(Integer.parseInt(arr[1]));
                    aad.setAreaName(arr[2]);
                    aad.setDrinkName(arr[3]);
                    aads.add(aad);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    LinkedHashMap<String, String> urdMap2 = urdMap.get(arr[0]);
                    if (urdMap2 == null) {
                        urdMap2 = new LinkedHashMap<String, String>();
                        urdMap.put(arr[0] + "", urdMap2);
                    }
                    if (arr[2] == null || "".equals(arr[2]) || "0".equals(arr[2])) {//忽略统计
                        arr[2] = "";//将数值为null或者为0置为空
                    } else {
                        //GAU合计
                        String relAdd = urdMapByGau.get(arr[0]);
                        if (relAdd == null || "".equals(relAdd) || relAdd.length() < 1) {//尚未放入数据
                            relAdd = "0";
                        }
                        urdMapByGau.put(arr[0], (Integer.parseInt(relAdd) + Integer.parseInt(arr[2])) + "");
                        //AAD合计
                        relAdd = urdMapByAad.get(arr[1]);
                        if (relAdd == null || "".equals(relAdd) || relAdd.length() < 1) {//尚未放入数据
                            relAdd = "0";
                        }
                        urdMapByAad.put(arr[1], (Integer.parseInt(relAdd) + Integer.parseInt(arr[2])) + "");
                        total += Integer.parseInt(arr[2]);
                        //System.out.println("gau="+arr[3]+" and value="+urdMapByGau.get(arr[3]));
                        //System.out.println("aad="+arr[4]+" and value="+urdMapByAad.get(arr[4]));
                    }
                    urdMap2.put(arr[1] + "", arr[2]);
                }

                //System.out.println("outter map size is "+urdMap.size());
            }

            String baseDir = "c:\\outputexcels";
            File baseDirF = new File("c:\\outputexcels");
            if (!baseDirF.exists()) {
                baseDirF.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File xlsFile = new File(baseDir + "\\" + uuid.toString() + ".xls");
            FileOutputStream fos = new FileOutputStream(xlsFile);
            WritableWorkbook wwb = Workbook.createWorkbook(fos);
            WritableSheet sheet = wwb.createSheet(month + "", 0);

            sheet.addCell(new Label(0, 0, "1-" + month + "月"));
            sheet.mergeCells(0, 0, 1, 1);
            int baseCol = 2;
            int baseRow = 1;
            for (int i = 0; i < aads.size(); i++) {
                Label label = new Label(baseCol + i, baseRow, aads.get(i).getAreaName());
                sheet.addCell(label);
            }
            sheet.addCell(new Label(0, 2, "部门及岗位"));
            sheet.addCell(new Label(1, 2, "姓名"));
            baseCol = 2;
            baseRow = 2;
            int lastCol = 0;
            for (int i = 0; i < aads.size(); i++) {
                lastCol = i;
                Label label = new Label(baseCol + i, baseRow, aads.get(i).getDrinkName());
                sheet.addCell(label);
            }
            sheet.addCell(new Label(baseCol + lastCol + 1, baseRow, "合计"));//合计列抬头

            //for(String a : urdMapByGau.values()){
            //	System.out.println(a);
            //}

            baseCol = 0;
            baseRow = 3;
            int lastRow = 0;
            for (int i = 0; i < gaus.size(); i++) {
                lastRow = baseRow + i;
                sheet.addCell(new Label(baseCol, baseRow + i, gaus.get(i).getGroupName()));
                sheet.addCell(new Label(baseCol + 1, baseRow + i, gaus.get(i).getUserName()));
                if (urdMap != null && urdMap.size() > 0) {
                    //for(String a : urdMap.keySet()){
                    //	System.out.println(a+"==?"+gaus.get(i).getId()+"");
                    //}
                    LinkedHashMap<String, String> urdMap2 = urdMap.get(gaus.get(i).getId() + "");
                    //for(String a : urdMap2.keySet()){
                    //	System.out.println(a);
                    //}
                    //System.out.println(aads.size());
                    for (int j = 0; j < aads.size(); j++) {
                        if (urdMap2 != null && urdMap2.size() > 0) {
                            String val = urdMap2.get(aads.get(j).getId() + "");
                            if (val != null && !"".equals(val) && val.length() != 0 && !"0".equals(val)) {
                                sheet.addCell(new Label(baseCol + 2 + j, baseRow + i, val));//基准列0，跨过部门、姓名列，酒类某列
                            }
                            //System.out.println("adding cell..."+urdMap2.get(aads.get(j).getId()+""));
                        }
                    }
                    //System.out.println("位于"+(lastCol+baseCol+2+1)+"列");
                    //System.out.println("lastCol="+(lastCol));
                    sheet.addCell(new Label(lastCol + baseCol + 2 + 1, baseRow + i, urdMapByGau.get(gaus.get(i).getId() + "")));//合计列填值
                }
            }

            sheet.addCell(new Label(1, lastRow + 1, "合计"));//合计行
            for (int i = 0; i < aads.size(); i++) {
                sheet.addCell(new Label(i + 2, lastRow + 1, urdMapByAad.get(aads.get(i).getId() + "")));
            }

            sheet.addCell(new Label(2 + aads.size(), 3 + gaus.size(), total + ""));//最终合计数

            wwb.write();
            wwb.close();

            outputFile(c, xlsFile, xlsFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void outputFile(Context c, File file, String name) {
        try {
            HttpServletResponse response = c.getHttpServletResponse();
            String fileName = URLEncoder.encode(name, "UTF-8");
            response.reset();
            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            int fileLength = (int) file.length();
            response.setContentLength(fileLength);
            /*如果文件长度大于0*/
            if (fileLength != 0) {
                /*创建输入流*/
                InputStream inStream = new FileInputStream(file);
                byte[] buf = new byte[4096];
                /*创建输出流*/
                ServletOutputStream servletOS = response.getOutputStream();
                int readLength;
                while (((readLength = inStream.read(buf)) != -1)) {
                    servletOS.write(buf, 0, readLength);
                }
                inStream.close();
                servletOS.flush();
                servletOS.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}