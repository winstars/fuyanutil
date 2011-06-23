<HTML>
<HEAD>
<TITLE>录入月度合并各部门销售清单(${month}月)</TITLE>
<script language="javascript" src="/js/jquery-1.6.1.min.js"></script>
</HEAD>

<BODY BGCOLOR="white">
<div style="text-align:center;valign:middle;font-size:26px"><p><b>录入月度合并各部门销售清单(${month}月)</b></p></div>
<br><br>
<table>
<tr>
#foreach (${aad} in ${aads})
<td>${aad.id}</td>
#end
<tr>
<tr>

<tr>
</table>
</BODY>

</HTML>