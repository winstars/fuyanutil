package type2;


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

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.joda.time.DateTime;

import jxl.*;
import jxl.biff.*;
import jxl.write.*;

import models.GroupAndUser2;
import models.UserDrinkData;
import models.UserPerformance;
import utils.DBC;

@Controller
public class Index {
    //private static final ResourceBundle RB = ResourceBundle.getBundle("LocalStrings");

    public void index(PrintWriter out) {
        //out.println(RB.getString("error.message.1"));
    }

    public void showInputGrid(Context c, int year, int month) {
        List<GroupAndUser2> gaus = new LinkedList<GroupAndUser2>();
        Map<String, UserPerformance> upMap = new LinkedHashMap<String, UserPerformance>();
        Map<String, Double> saleNumSumMap = new LinkedHashMap<String, Double>();
        Map<String, Double> saleValueSumMap = new LinkedHashMap<String, Double>();
        Map<String, Double> ungivenValueMap = new LinkedHashMap<String, Double>();
        DateTime today = new DateTime();
        if (year == 0) {
            year = today.getYear();
        }
        if (month == 0) {
            month = today.getMonthOfYear();
        }
        try {
            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select ID,SYEAR,SMONTH,USERID,SALEVALUE,SALENUM,EXTRACTVALUE,EXTRAVALUE,GIVENVALUE,SALE1,SALE2,EXCHANGE,COMPLETESTATUS from USERPERFORMANCE where SYEAR=" + year + " and SMONTH=" + month + "");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME,GROUPTYPE from GROUPANDUSER2 order by GROUPTYPE,GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select USERID,sum(SALENUM),sum(SALEVALUE),sum(EXTRACTVALUE),sum(EXTRAVALUE),sum(GIVENVALUE),sum(EXCHANGE) from" +
                    " USERPERFORMANCE where SYEAR=" + year + " and SMONTH>=1 and SMONTH<" + month + " GROUP BY USERID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser2 gau = new GroupAndUser2();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gau.setGroupType(Integer.parseInt(arr[4]));
                    gaus.add(gau);
                    saleNumSumMap.put(arr[0] + "", 0d);
                    saleValueSumMap.put(arr[0] + "", 0d);
                    ungivenValueMap.put(arr[0] + "", 0d);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    UserPerformance up = new UserPerformance();
                    up.setId(Integer.parseInt(arr[0]));
                    up.setSyear(Integer.parseInt(arr[1]));
                    up.setSmonth(Integer.parseInt(arr[2]));
                    up.setUserid(Integer.parseInt(arr[3]));
                    up.setSaleValue(getNullValueForDouble(arr[4]));
//                    up.setSaleValueSum(getNullValueForDouble(arr[4]));
                    up.setSaleNum(getNullValueForDouble(arr[5]));
//                    up.setSaleNumSum(getNullValueForDouble(arr[5]));
                    up.setExtractValue(getNullValueForDouble(arr[6]));
                    up.setExtraValue(getNullValueForDouble(arr[7]));
                    up.setGivenValue(getNullValueForDouble(arr[8]));
                    up.setSale1(getNullValueForDouble(arr[9]));
                    up.setSale2(getNullValueForDouble(arr[10]));
                    up.setExchange(getNullValueForDouble(arr[11]));
                    up.setCompleteStatus(getNullValueForInteger(arr[12]));
                    upMap.put(up.getUserid() + "", up);
                }
            }


            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    saleNumSumMap.put(arr[0] + "", getNullValueForDouble(arr[1]));
                    saleValueSumMap.put(arr[0] + "", getNullValueForDouble(arr[2]));
                    ungivenValueMap.put(arr[0] + "", (getNullValueForDouble(arr[4]) - getNullValueForDouble(arr[5]) - getNullValueForDouble(arr[6])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c.out("/title.vm");
        c.out("/type2/typeset.vm");
        c.out("/type2/showInputGrid.vm");
    }

    private Integer getNullValueForInteger(String s) {
        Integer i = 0;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            i = 0;
        }
        return i;
    }

    private Double getNullValueForDouble(String s) {
        Double i = 0d;
        try {
            i = Double.parseDouble(s);
        } catch (Exception e) {
            i = 0d;
        }
        return i;
    }

    public void update(PrintWriter out, Context c, int year, int month, int userid, double saleNum, double saleValue, int completeStatus, double extractValue,
                       double extraValue, double givenValue, double exchange) {
        try {
            DBC dbc = DBC.getInstance();
            String[][] arr = dbc.getArr("select * from USERPERFORMANCE where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + userid);
            if (arr == null || arr.length == 0) {
                dbc.insertDB("insert into USERPERFORMANCE(SYEAR,SMONTH,USERID,SALENUM,SALEVALUE,COMPLETESTATUS,EXTRACTVALUE,EXTRAVALUE,GIVENVALUE,EXCHANGE)" +
                        "values(" + year + "," + month + "," + userid + "," + saleNum + "," + saleValue + "," + completeStatus + "," + extractValue + "," +
                        extraValue + "," + givenValue + "," + exchange + ")");
            } else {
                dbc.updateDB("update USERPERFORMANCE set SALENUM=" + saleNum + ",SALEVALUE=" + saleValue + ",COMPLETESTATUS=" +
                        completeStatus + ",EXTRACTVALUE=" + extractValue + ",EXTRAVALUE=" + extraValue + ", GIVENVALUE=" + givenValue + ", EXCHANGE=" + exchange +
                        "where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + userid);
            }
            out.print("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("0");
        }

    }

    public void update2(PrintWriter out, Context c, int year, int month, int userid, double sale1, double sale2) {
        try {
            DBC dbc = DBC.getInstance();
            String[][] arr = dbc.getArr("select * from USERPERFORMANCE where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + userid);
            if (arr == null || arr.length == 0) {
                dbc.insertDB("insert into USERPERFORMANCE(SYEAR,SMONTH,USERID,SALE1,SALE2)" +
                        "values(" + year + "," + month + "," + userid + "," + sale1 + "," + sale2 + ")");
            } else {
                dbc.updateDB("update USERPERFORMANCE set SALE1=" + sale1 + ",SALE2=" + sale2 +
                        "where SYEAR=" + year + " and SMONTH=" + month + " and USERID=" + userid);
            }
            out.print("1");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("0");
        }

    }


    public void exportExcel(PrintWriter out, Context c, int year, int month) {
        try {
            List<GroupAndUser2> gaus = new LinkedList<GroupAndUser2>();
            Map<String, UserPerformance> upMap = new LinkedHashMap<String, UserPerformance>();
            Map<String, Double> saleNumSumMap = new LinkedHashMap<String, Double>();
            Map<String, Double> saleValueSumMap = new LinkedHashMap<String, Double>();
            Map<String, Double> ungivenValueMap = new LinkedHashMap<String, Double>();
            DateTime today = new DateTime();
            if (year == 0) {
                year = today.getYear();
            }
            if (month == 0) {
                month = today.getMonthOfYear();
            }

            DBC dbc = DBC.getInstance();
            String[][] arrs1 = dbc.getArr("select ID,SYEAR,SMONTH,USERID,SALEVALUE,SALENUM,EXTRACTVALUE,EXTRAVALUE,GIVENVALUE,SALE1,SALE2,EXCHANGE,COMPLETESTATUS from USERPERFORMANCE where SYEAR=" + year + " and SMONTH=" + month + "");
            String[][] arrs2 = dbc.getArr("select ID,GROUPID,GROUPNAME,USERNAME,GROUPTYPE from GROUPANDUSER2 order by GROUPTYPE,GROUPID,ID");
            String[][] arrs3 = dbc.getArr("select USERID,sum(SALENUM),sum(SALEVALUE),sum(EXTRACTVALUE),sum(EXTRAVALUE),sum(GIVENVALUE),sum(EXCHANGE) from" +
                    " USERPERFORMANCE where SYEAR=" + year + " and SMONTH>=1 and SMONTH<" + month + " GROUP BY USERID");

            if (arrs2 != null) {
                for (String[] arr : arrs2) {
                    GroupAndUser2 gau = new GroupAndUser2();
                    gau.setId(Integer.parseInt(arr[0]));
                    gau.setGroupId(Integer.parseInt(arr[1]));
                    gau.setGroupName(arr[2]);
                    gau.setUserName(arr[3]);
                    gau.setGroupType(Integer.parseInt(arr[4]));
                    gaus.add(gau);
                    saleNumSumMap.put(arr[0] + "", 0d);
                    saleValueSumMap.put(arr[0] + "", 0d);
                    ungivenValueMap.put(arr[0] + "", 0d);
                }
            }

            if (arrs1 != null) {
                for (String[] arr : arrs1) {
                    UserPerformance up = new UserPerformance();
                    up.setId(Integer.parseInt(arr[0]));
                    up.setSyear(Integer.parseInt(arr[1]));
                    up.setSmonth(Integer.parseInt(arr[2]));
                    up.setUserid(Integer.parseInt(arr[3]));
                    up.setSaleValue(getNullValueForDouble(arr[4]));
//                    up.setSaleValueSum(getNullValueForDouble(arr[4]));
                    up.setSaleNum(getNullValueForDouble(arr[5]));
//                    up.setSaleNumSum(getNullValueForDouble(arr[5]));
                    up.setExtractValue(getNullValueForDouble(arr[6]));
                    up.setExtraValue(getNullValueForDouble(arr[7]));
                    up.setGivenValue(getNullValueForDouble(arr[8]));
                    up.setSale1(getNullValueForDouble(arr[9]));
                    up.setSale2(getNullValueForDouble(arr[10]));
                    up.setExchange(getNullValueForDouble(arr[11]));
                    up.setCompleteStatus(getNullValueForInteger(arr[12]));
                    upMap.put(up.getUserid() + "", up);
                }
            }


            if (arrs3 != null) {
                for (String[] arr : arrs3) {
                    saleNumSumMap.put(arr[0] + "", getNullValueForDouble(arr[1]));
                    saleValueSumMap.put(arr[0] + "", getNullValueForDouble(arr[2]));
                    ungivenValueMap.put(arr[0] + "", (getNullValueForDouble(arr[4]) - getNullValueForDouble(arr[5]) - getNullValueForDouble(arr[6])));
                }
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

            sheet.addCell(new Label(0, 0, "葡萄酒中心月度绩效考核"));
            sheet.mergeCells(0, 0, 11, 0);
            sheet.addCell(new Label(0, 1, year + "年" + month + "月"));
            sheet.mergeCells(0, 1, 11, 1);

            sheet.addCell(new Label(0, 2, "职务"));
            sheet.addCell(new Label(1, 2, "姓名"));
            sheet.addCell(new Label(2, 2, "本月销售数量"));
            sheet.addCell(new Label(3, 2, "1-本月销售数量累计"));
            sheet.addCell(new Label(4, 2, "本月销售金额"));
            sheet.addCell(new Label(5, 2, "1-本月销售金额累计"));
            sheet.addCell(new Label(6, 2, "考核完成情况"));
            sheet.addCell(new Label(7, 2, "计提金额"));
            sheet.addCell(new Label(8, 2, "考核奖励"));
            sheet.addCell(new Label(9, 2, "本月奖励"));
            sheet.addCell(new Label(10, 2, "累计未奖励"));
            sheet.addCell(new Label(11, 2, "补发奖励"));

            int baseRow = 3;
            int i = 0;
            for (String[] arr2 : arrs2) {
                if ("1".equals(arr2[4]) || "2".equals(arr2[4])) {
                    sheet.addCell(new Label(0, baseRow + i, "1".equals(arr2[4]) ? "后勤管理人员" : "业务员"));
                    sheet.addCell(new Label(1, baseRow + i, arr2[3]));
                    UserPerformance up = upMap.get(arr2[0] + "");
                    if (up != null) {
                        sheet.addCell(new Label(2, baseRow + i, up.getSaleNum() + ""));
                        sheet.addCell(new Label(3, baseRow + i, (up.getSaleNum() + saleNumSumMap.get(arr2[0])) + ""));
                        sheet.addCell(new Label(4, baseRow + i, up.getSaleValue() + ""));
                        sheet.addCell(new Label(5, baseRow + i, (up.getSaleValue() + saleValueSumMap.get(arr2[0])) + ""));
                        sheet.addCell(new Label(6, baseRow + i, ("0".equals(up.getCompleteStatus() + "") ? "" : ("1".equals(up.getCompleteStatus() + "") ? "完成" : "未完成")) + ""));
                        sheet.addCell(new Label(7, baseRow + i, up.getExtractValue() + ""));
                        sheet.addCell(new Label(8, baseRow + i, up.getExtraValue() + ""));
                        sheet.addCell(new Label(9, baseRow + i, up.getGivenValue() + ""));
                        sheet.addCell(new Label(10, baseRow + i, (ungivenValueMap.get(arr2[0]) + up.getExtraValue() - up.getGivenValue() - up.getExchange()) + ""));
                        sheet.addCell(new Label(11, baseRow + i, up.getExchange() + ""));
                    } else {
                        sheet.addCell(new Label(3, baseRow + i, (saleNumSumMap.get(arr2[0])) + ""));
                        sheet.addCell(new Label(5, baseRow + i, (saleValueSumMap.get(arr2[0])) + ""));
                        sheet.addCell(new Label(10, baseRow + i, (ungivenValueMap.get(arr2[0])) + ""));
                    }
                } else {
                    sheet.addCell(new Label(0, baseRow + i, "1".equals(arr2[4]) ? "后勤管理人员" : "业务员"));
                    sheet.mergeCells(0, baseRow + i, 0, baseRow + i+1);
                    sheet.addCell(new Label(1, baseRow + i, arr2[3]));
                    sheet.mergeCells(1, baseRow + i, 1, baseRow + i+1);
                    sheet.addCell(new Label(2, baseRow + i, "销售"));
                    sheet.addCell(new Label(2, baseRow + i+1, "提货券"));
                    UserPerformance up = upMap.get(arr2[0] + "");
                    if (up != null) {
                        sheet.addCell(new Label(3, baseRow + i, up.getSale1()+""));
                        sheet.addCell(new Label(3, baseRow + i+1, up.getSale2()+""));
                    }
                    sheet.addCell(new Label(4, baseRow + i, "提成"));
                    sheet.addCell(new Label(4, baseRow + i+1, "提成"));
                    if (up != null) {
                        sheet.addCell(new Label(5, baseRow + i, up.getSale1()*2+""));
                        sheet.addCell(new Label(5, baseRow + i+1, up.getSale2()*0.5+""));
                    }
                    i++;
                }
                i++;
            }

            sheet.addCell(new Label(0, baseRow + i, "葡萄酒中心负责人："));
            sheet.addCell(new Label(3, baseRow + i, "财务部审核："));
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