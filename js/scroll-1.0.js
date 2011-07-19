 //判断浏览器类型
 /*
 ua = navigator.userAgent.toLowerCase(),
        check = function(r){
            return r.test(ua);
        },
        DOC = document,
        isStrict = DOC.compatMode == "CSS1Compat",
        isOpera = check(/opera/),
        isChrome = check(/chrome/),
        isWebKit = check(/webkit/),
        isSafari = !isChrome && check(/safari/),
        isSafari2 = isSafari && check(/applewebkit\/4/), // unique to Safari 2
        isSafari3 = isSafari && check(/version\/3/),
        isSafari4 = isSafari && check(/version\/4/),
        isIE = !isOpera && check(/msie/),
        isIE7 = isIE && check(/msie 7/),
        isIE8 = isIE && check(/msie 8/),
        isIE6 = isIE && !isIE7 && !isIE8, 
 */
        var Sys = {};
        var ua = navigator.userAgent.toLowerCase();
        if (window.ActiveXObject)
            Sys.ie = ua.match(/msie ([\d.]+)/)[1];
        else if (document.getBoxObjectFor)
            Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1]
        else if (window.MessageEvent && !document.getBoxObjectFor)
            Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1]
        else if (window.opera)
            Sys.opera = ua.match(/opera.([\d.]+)/)[1]
        else if (window.openDatabase)
            Sys.safari = ua.match(/version\/([\d.]+)/)[1];
        
        //alert(Sys.ie);
        //以下进行测试
        //if(Sys.ie) document.write('IE: '+Sys.ie);
        //if(Sys.firefox) document.write('Firefox: '+Sys.firefox);
        //if(Sys.chrome) document.write('Chrome: '+Sys.chrome);
        //if(Sys.opera) document.write('Opera: '+Sys.opera);
        //if(Sys.safari) document.write('Safari: '+Sys.safari);


        //入口函数
        //divID：包含table的div。该div内只能有一个要锁定的table
        //rowCount：要锁定的行数，从最上一行开始数。0表示不锁定行。
        //columnCount：要锁定的列数，从最左面一列开始数。0表示不锁定列。
        function myScroll(divID,rowCount,columnCount,tableID)
        {
            //GridView的外面还有一个div，这个比较烦
            if (tableID)
            {}else
            {
                tableID = divID + " table";
            }
            //alert("锁定行列即将开始！");
            //copy 三个div和table
            CreatDiv(divID,rowCount,columnCount,tableID)
       
            //copy属性、css和事件
            SetTableInfo(divID,rowCount,columnCount,tableID);
            
            //设置table的宽度和高度
            SetTableWH(divID,rowCount,columnCount,tableID);
            
            //定位，浮动的要锁定的table的上面。
            SetTableXY(divID,rowCount,columnCount,tableID);
            
        }
        
        function CreatDiv(divID,rowCount,columnCount,tableID)
        {
            //锁定行列的三个DIV、三个table
            //alert("创建DIV和Table");
            var DivTop=j("<div></div>");
            var DivLeft=j("<div></div>");
            var DivTopLeft=j("<div></div>");
            
            var TblTop=j("<table></table>");
            var TblLeft=j("<table></table>");
            var TblTopLeft=j("<table></table>");
            
            DivTop.attr("id","Div_Top"); 
            DivLeft.attr("id","Div_Left");
            DivTopLeft.attr("id","Div_TopLeft");
            
            TblTop.attr("id","Tbl_Top");
            TblLeft.attr("id","Tbl_Left");
            TblTopLeft.attr("id","Tbl_TopLeft");

            TblTop.attr("style","width: 0px; height: 0px;");
            TblLeft.attr("style"," width: 0px; height: 0px;");
            TblTopLeft.attr("style","width: 0px; height: 0px;");
            
            //加滚动事件
            j("#"+divID).scroll(function (){
                divTop=j("#Div_Top").scrollLeft(this.scrollLeft);
                divLeft=j("#Div_Left").scrollTop(this.scrollTop);
            });
            
            if (rowCount > 0)
            {
                for (i =0;i<rowCount;i++)
                {
                    //添加要锁定的行，因为可能是多列，所以做了一个循环
                    TblTop.append("<tr>"+j("#"+ tableID + " tr:eq("+ i +")")[0].innerHTML+"</tr>");
                }
                
                DivTop.append(TblTop);
                j("body").append(DivTop);
                    
            }
            
            var tmpIsTD;
            
            //alert("添加要锁定的列");
            //添加要锁定的列
            if (columnCount > 0)
            {
                var tableRowsCount ;
                var tmpTD;
                
                tableRowsCount = j("#"+ tableID + " tr").length;   //锁定表格的行数
                for (a =0;a<tableRowsCount;a++)
                {
                    tmpTD="";
                    for (b =0;b<columnCount;b++)
                    {
                        //添加要锁定的列。第一行可能是th而不是td
                        tmpIsTD = j("#"+ tableID + " tr:eq("+ a +") td:eq("+ b +")")[0];
                        if (tmpIsTD )
                            tmpTD += "<td>" + tmpIsTD.innerHTML + "</td>";
                        else
                            tmpTD += "<th>" + j("#"+ tableID + " tr:eq("+ a +") th:eq("+ b +")")[0].innerHTML + "</th>";
                        
                    }
                    
                    TblLeft.append("<tr>"+tmpTD+"</tr>");
                    //if (a >100)  //锁定列的行数太多的话，速度会很慢。暂且设置只支持100行
                    //    break;
                }
                DivLeft.append(TblLeft);
                j("body").append(DivLeft);
                    
            }
            
            //alert("添加行列重叠的部分");
            //添加行列重叠的部分
            if (rowCount > 0 && columnCount >0)
            {
                 for (a =0;a<rowCount;a++)
                 {
                    tmpTD="";
                    for (b =0;b<columnCount;b++)
                    {
                        //添加要锁定的列。第一行可能是th而不是td
                        tmpIsTD = j("#"+ tableID + " tr:eq("+ a +") td:eq("+ b +")")[0];
                        if (tmpIsTD )
                            tmpTD += "<td>" + tmpIsTD.innerHTML + "</td>";
                        else    
                            tmpTD += "<th>" + j("#"+ tableID + " tr:eq("+ a +") th:eq("+ b +")")[0].innerHTML + "</th>";
                   
                    }
                    
                    TblTopLeft.append("<tr>"+tmpTD+"</tr>");
                }
                
                DivTopLeft.append(TblTopLeft);
                j("body").append(DivTopLeft);
            }
            
        }
        
        function SetTableWH(divID,rowCount,columnCount,tableID)
        {
            //设置div、table的宽度高度
            //alert("开始设置div table的宽度和高度！");
            var divMain = j("#" + divID)[0];
            //var dTop = j("#" + tableID + " tr:first th")[0];
            
            //alert(j("#" + tableID + "").height());
            
            var tmpDivWidth;    //div的总宽度
            var tmpDivHeight;   //div的总高度
            
            var tmpRowWidth;        //锁定行的table的宽度
            var tmpRowHeight = 0;   //锁定行的table的高度
            var tmpColWidth = 0;    //锁定列的table的宽度
            var tmpColHeight;       //锁定列的table的高度
            
            var tmpIsTD;
             
            tmpDivWidth = j("#" + divID).width() +1;
            tmpDivHeight = j("#" + divID).height() +1;
            tmpRowWidth = j("#" + tableID ).outerWidth(true) + 5;
            tmpColHeight = j("#" + tableID ).outerHeight(true);
            
            for (i=0;i<rowCount;i++)
            {   //锁定行的总高度，锁定多行的话需要累加
                tmpIsTD = j("#" + tableID + " tr:eq("+i+") td");
                if (tmpIsTD[0])
                    tmpRowHeight += getRowMaxHeight(tmpIsTD);
                else
                    tmpRowHeight += getRowMaxHeight(j("#" + tableID + " tr:eq("+i+") th"));
            }
            
            
            for (i=0;i<columnCount;i++)
            {
                //锁定列的总宽度
                tmpIsTD = j("#" + tableID + " tr:eq(0) td:eq("+i+")");
                if (tmpIsTD[0])
                    tmpColWidth += tmpIsTD.outerWidth(true);
                else
                    tmpColWidth += j("#" + tableID + " tr:eq(0) th:eq("+i+")").outerWidth(true);
            }
            
          
            //设置行的宽高
            if (rowCount >0)
            {
                j("#Div_Top").width(tmpDivWidth - 18);
                j("#Tbl_Top").width(tmpRowWidth);

                j("#Div_Top,#Tbl_Top").height(tmpRowHeight + 2);
            }
            
            //设置列宽高
            if (columnCount >0)
            {
                j("#Div_Left,#Tbl_Left").width(tmpColWidth + 2);
                
                j("#Div_Left").height(tmpDivHeight - 18);
                j("#Tbl_Left").height(tmpColHeight - 10);
            }
                
            //交叉的部分
            if (rowCount >0 && columnCount >0)
            {
                j("#Div_TopLeft,#Tbl_TopLeft").width(tmpColWidth +2 );
                j("#Div_TopLeft,#Tbl_TopLeft").height(tmpRowHeight +2 );
            }   
           
            //设置行的每个td的宽度
            //alert("设置行的每个td的宽度" +  j("#Tbl_Top tr:eq(0) th").length);
            tmpIsTD = j("#" + tableID + " tr:eq(0) td");
            if (tmpIsTD[0])
            {
//                tmpIsTD.each(function (i){
//                    j("#Tbl_Top tr:eq(0) td:eq("+ i +")").width(this.width());
//                });
            }
            else
            {
                j("#" + tableID + " tr:eq(0) th").each(function (i){
                    j("#Tbl_Top tr:eq(0) th:eq("+ i +")").width(j(this).width());
                });
            }
             
            
            tmpIsTD = j("#Tbl_Top tr:eq(0) td");
            if (tmpIsTD[0])
            {
                tmpIsTD.each(function (i){
                        j(this).width(j("#" + tableID + " tr:eq(0) td:eq(" + i + ")").width());
                    });
            }
            else
            {
                j("#Tbl_Top tr:eq(0) th").each(function (i){
                        j(this).width(j("#" + tableID + " tr:eq(0) th:eq(" + i + ")").width());
                    });
            }
            
            var trIndex = 0;
            var tmpH = 0;
            //var tdIndex = 0; 
            //设置锁定列的每个td的高度
            tmpIsTD = j("#Tbl_Left tr");
            if (tmpIsTD[0])
            {
                tmpIsTD.each(function (i){
                    //trIndex = parseInt(i / columnCount);
                    //tmpH = j("#" + tableID + " tr").height;
//                    tmpH = getRowMaxHeight( j("#" + tableID + " tr:eq(" + trIndex + ")"));

//                    j(this).height(tmpH );
//                    alert(j(this).height() + "_"+ tmpH);
//                    
//                    if(Sys.ie) j(this).height(tmpH -2);
//                    if(Sys.firefox) j(this).height(tmpH );
//                    if(Sys.chrome) j(this).height(tmpH - 4);
//                    if(Sys.opera) j(this).height(tmpH - 1);
//                    if(Sys.safari) j(this).height(tmpH - 1);
                    
                    var tra = j(this);
                    //var trb =  j("#" + tableID + " tr");
                    var ha = tra.height;
                    var hb = 0;
                    tmpIsTD = j("#" + tableID + " tr:eq("+i+") td");
                    if ( tmpIsTD[0])
                        hb = getRowMaxHeight2( tmpIsTD);
                    else
                        hb = getRowMaxHeight2( j("#" + tableID + " tr:eq("+i+") th"));
                        
                    //alert("外部："+trb.height() + "_"+tmpH);
                    
                    //if (tmpH > hb ) hb = tmpH;
                    
                    if (ha>hb) 
                        tra.height(ha);
                    else
                        tra.height(hb);
                        
                    
                });
            }
            
//            tmpIsTD = j("#Tbl_Left th");
//            if (tmpIsTD[0])
//            {
//                j("#Tbl_Left tr th").each(function (i){
//                    trIndex = parseInt(i / columnCount);
//                    //tmpH = j("#" + tableID + " tr:eq(" + trIndex + ")").height;
//                    tmpH = getRowMaxHeight( j("#" + tableID + " tr:eq(" + trIndex + ")"));
//                    j(this).height(tmpH - 1);
//                    if(Sys.ie) j(this).height(tmpH - 5);
//                    if(Sys.firefox) j(this).height(tmpH -1);
//                    if(Sys.chrome) j(this).height(tmpH - 6);
//                    if(Sys.opera) j(this).height(tmpH - 1);
//                    if(Sys.safari) j(this).height(tmpH - 1);
//                });
//            }
              
        }
        
        function SetTableXY(divID,rowCount,columnCount,tableID)
        {
            //alert("设置位置");
            
            var offset = j("#"+divID).offset(); //左上角的坐标
            
            if(rowCount >0  )
                j("#Div_Top").offset(offset);
            
            if ( columnCount >0)
                j("#Div_Left").offset(offset);
                 
            if (rowCount >0 && columnCount >0)
                j("#Div_TopLeft").offset(offset);
                
        }
        
        function SetTableInfo(divID,rowCount,columnCount,tableID)
        {
            //alert("开始复制属性");
            
            //复制三个div的属性
            j("#Div_Top,#Div_Left,#Div_TopLeft").width(j("#"+divID).width());
            //j("#Div_Top,#Div_Left,#Div_TopLeft").each(function (){
                //myMergeAttributes(this, j("#"+divID)[0]);
            //    j(this).width();
            //});
            
            //移除scroll事件
            //j("#Div_Top,#Div_Left,#Div_TopLeft").removeAttr("onscroll");
            j("#Div_Top,#Div_Left,#Div_TopLeft").removeAttr("style");
            
            j("#Div_Top,#Div_Left,#Div_TopLeft").attr("style","clear:one; overflow: hidden; width: 0px; height: 0px;position:absolute;left:0px;top:0px;background-color:#ffffff");
           
            //alert("复制三个table的属性");
            //复制三个table的属性
            j("#Tbl_Top,#Tbl_Left,#Tbl_TopLeft").each(function (){
                myMergeAttributes(this,j("#" + tableID)[0]);
            });
            
            //alert("复制锁定行的每个tr的属性 ");
            //复制锁定行的每个tr的属性
            j("#Tbl_Top tr").each(function (i){
                //alert(this.innerHTML);
                myMergeAttributes(this,j("#"+tableID + " tr:eq("+i+")")[0]);
            });
            
            //alert("复制锁定行的每个td的属性");
            //复制锁定行的每个td的属性
            var trIndex = 0;
            var tdIndex = 0;
            var tdCount = 0;
            
            var tmpIsTD;
            
            //可能会锁定多行，所以要一行一行的判断
            for (var rowIndex = 0 ;rowIndex<rowCount;rowIndex++)
            {
                //判断锁定表的每一行，如果是th，如果是td
                tmpIsTD = j("#"+ tableID + " tr:eq("+ rowIndex +") th");
                if (tmpIsTD[0])
                {
                    tdCount =  tmpIsTD.length; 
                    tmpIsTD.each(function (i){
                        tdIndex = i % tdCount;
                        //alert(this.innerHTML);
                        myMergeAttributes(this,j("#Tbl_Top tr:eq("+ rowIndex +") th:eq("+ tdIndex +")")[0]);
                    });
                }
                 
                tmpIsTD = j("#"+ tableID + " tr:eq("+ rowIndex +") td");
                if (tmpIsTD[0])
                {
                    tdCount =  tmpIsTD.length; 
                    tmpIsTD.each(function (i){
                        tdIndex = i % tdCount;
                        //alert(this.innerHTML);
                        myMergeAttributes(this,j("#Tbl_Top tr:eq("+ rowIndex +") td:eq("+ tdIndex +")")[0]);
                    });
                }
                
                 
            }
            
            //alert("复制锁定列的每个tr的属性");
            //复制锁定列的每个tr的属性
            j("#Tbl_Left tr").each(function (i){
                myMergeAttributes(this,j("#"+tableID + " tr:eq("+i+")")[0]);
            });
            
           
            
            //复制锁定列的每个td的属性
            trIndex = 0;
            tdIndex = 0;
            
            var rowIndex = 0;
            
            for ( rowIndex = 0 ;rowIndex<columnCount;rowIndex++)
            {
                tmpIsTD = j("#Tbl_Left tr:eq("+ rowIndex +") th");
                if (tmpIsTD[0])
                {
                    tmpIsTD.each(function (i){
                        tdIndex = i % columnCount;
                        myMergeAttributes(this,j("#"+tableID + " tr:eq("+rowIndex+") th:eq("+ tdIndex +")")[0]);
                    });
                }
                else
                {
                    //没有th了，退出循环
                    break;
                }
            }
            
            if (rowIndex < columnCount - 1)
            {
                tmpIsTD = j("#Tbl_Left td");
                if (tmpIsTD[0])
                {
                    tmpIsTD.each(function (i){
                        trIndex = parseInt(i / columnCount);
                        tdIndex = i % columnCount;
                        myMergeAttributes(this,j("#"+tableID + " tr:eq("+ ( rowIndex + trIndex ) +") td:eq("+ tdIndex +")")[0]);
                    });
                }
            }
            
            //复制锁定行列列的每个tr的属性
            j("#Div_TopLeft tr").each(function (i){
                myMergeAttributes(this,j("#"+tableID + " tr:eq("+i+")")[0]);
            });
         
            //锁定行列的每个td的属性
            trIndex = 0;
            tdIndex = 0;
            
            for ( rowIndex = 0 ;rowIndex<columnCount;rowIndex++)
            {
                tmpIsTD = j("#Div_TopLeft tr:eq("+ rowIndex +") th");
                if (tmpIsTD[0])
                {
                    tmpIsTD.each(function (i){
                        tdIndex = i % columnCount;
                        myMergeAttributes(this,j("#"+tableID + " tr:eq("+rowIndex+") th:eq("+ tdIndex +")")[0]);
                    });
                }
                else
                {
                    //没有th了，退出循环
                    break;
                }
            }
            
            if (rowIndex < columnCount - 1)
            {
                tmpIsTD = j("#Div_TopLeft td");
                if (tmpIsTD[0])
                {
                    tmpIsTD.each(function (i){
                        trIndex = parseInt(i / columnCount);
                        tdIndex = i % columnCount;
                        myMergeAttributes(this,j("#"+tableID + " tr:eq("+ ( rowIndex + trIndex ) +") td:eq("+ tdIndex +")")[0]);
                    });
                }
            }
        }
        
        //获取指定行里的所有TD的最高的高度
        function getRowMaxHeight(tr)
        {
            //获取指定的行的最大的高度
            //alert(tr.length);
            var maxHeight = 0;
            tr.each(function (){
                //alert(j(this).height());
                if (j(this).outerHeight() > maxHeight)
                    maxHeight = j(this).outerHeight(true);
            });
            return maxHeight;
        }
        
        //获取指定行里的所有TD的最高的高度
        function getRowMaxHeight2(tr)
        {
            //获取指定的行的最大的高度
            var maxHeight = 0;
            tr.each(function (){
                //alert("td_"+ j(this)[0].innerHTML +"_"+j(this).outerHeight());
                if(Sys.ie)
                {
                    if (Sys.ie == "8.0")
                    {
                        if (j(this).outerHeight() > maxHeight)
                            maxHeight = j(this).outerHeight();
                    }
                    else
                    {
                        if (j(this).height() > maxHeight)
                            maxHeight = j(this).height();
                    }
                }
                else
                {
                    if (j(this).outerHeight() > maxHeight)
                        maxHeight = j(this).outerHeight();
                }
            });
            
            //计算边框
            if(Sys.ie == "8.0")
            {
            }
            else
            {
                //alert(j(tr[0]).outerWidth()+" - "+j(tr[0]).width());
                var bk = (j(tr[0]).outerHeight() - j(tr[0]).height());
                maxHeight = maxHeight -  ((bk < 6)?bk:1);
            }
            return maxHeight;
        }
        
        //赋值对象属性的函数。IE 里可以用 mergeAttributes，但是其他浏览器不支持，所以只好用遍了的办法。
        function myMergeAttributes(target,source)
        {
            if(Sys.ie)
            {
                target.mergeAttributes(source);
            }
            else
            {
                attrs = source.attributes,
			    i = attrs.length - 1;
    		     
		        for(;i>=0;i--){
			        var name = attrs[i].name;
			        
				    switch (name.toLowerCase())
				    {
				        case 'id':
    			        case 'height':
    			        case 'width':
				   	        continue;
				   	    
                    }
                    
				    if (attrs[i].value != null   )
				    {
			            target.setAttribute(name, attrs[i].value);
			        }
                }
            }
            
            
        }
