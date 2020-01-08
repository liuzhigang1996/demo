//打开对话框的属性

var fWidth=screen.availWidth;
var fHeight=screen.availHeight;
var sFeature="dialogWidth:"+fWidth+"px;dialogHeight:"+fHeight+"px;status:no;help:no;maximize:yes;minimize:yes;center:yes;status:no;resizable:yes;scroll:yes";

// trigger Event Key Down
document.onkeydown = nkey;

function isWhitespace (s)
{  
    var whitespace = " \t\n\r";
    var i;
    for (i = 0; i < s.length; i++)
    {   
        var c = s.charAt(i);
        if (whitespace.indexOf(c) >= 0){
            return true;
        }
    }
    return false;
}

//全部选中
function CheckAll(form)
{
  for (var i=0;i<form.elements.length;i++)
    {
    var e = form.elements[i];
    if (e.name != 'chkall')
       e.checked = form.chkall.checked;
    }
}

//禁用CTRL+N打開新窗口
function openNewWindow(){
    if(event.keyCode==78&&event.ctrlKey)
    alert('对不起，本程序禁示弹出新窗口。')
    return false;
}



//提交后按鈕變灰
function setFormAllDisabled(formName)
  {
  var form1 = document.getElementById(formName);
  if(form1!=null){
    for (var i=0;i<form1.elements.length;i++){
      var fieldObject = form1.elements[i];
      if (fieldObject.tagName.toLowerCase() =='input'){
           fieldObject.disabled=true;
      }
    }
    return true;
  } 
}
 
//监控按下键盘的操作
function keyDown()
{
    //禁止Ctrl+N
    if(event.keyCode==78 && event.ctrlKey)
    {
        event.returnValue = false;
        return false;
    }
    
    //禁止Ctrl+P
    if(event.keyCode==80 && event.ctrlKey)
    {
        alert('Ctrl+P is forbidden!');
        event.returnValue = false;
        return false;
    }
    
}


//搜索数组中的指定元素，若存在则返回其下标，否则返回-1
function ArraySearch(arrays, key) {
    
    var low = 0;
    var mid =0;
    var high = arrays.length-1;
      
    var dd =0;
    while (low <= high) {
         mid =Math.floor( (low + high)/2);
    
        var midObj;
        
        midObj =arrays[mid];

        if (midObj < key)
        {
            low = mid + 1;
        }
        else if (midObj > key)
        {
            high = mid - 1;
        }
        else
        {
            return mid; // key found
        }
    }
    return -1;  // key not found.
 }

//检查一个对象是否存在
function isExist(s)
{
    
    if (eval(s)==null)
    {
        return false;
    }
        
    return true;
}


//清除字符串前导空格和拖尾空格
function trimString(s)
{
    if(s==null||s=="")
    {
        return "";
    }

    var str=new String(s);
    var index=0;

    while(str.charAt(0)==" ")
    {
        str=str.substring(1);
    }

    index=str.length-1;

    while(str.charAt(index)==" ")
    {
        str=str.substring(0,index);
        index=str.length-1;
    }
    
    return str;
}

//将一个列表中的当前选项上移一格，未做类型检查，请小心使用，传人的参数必须为一个SELECT对象

function moveUpItems(listObject)
{
    var i;
    for(i=0;i<listObject.options.length;i++)
    {
        if(listObject.options[i].selected)
        {
            moveUpItem(listObject,i);
        }
    }
}

function moveUpItem(listObject,anIndex)
{
    var selectedValue,selectedText;
    if(anIndex<0)
    {
        alert("请选择要移动的项");
        return false;
    }
    else if(anIndex==0)
    {
         alert("已经在最顶端");
         return false;
    }
    else
    {
        selectedValue=listObject.options[anIndex].value;
        selectedText=listObject.options[anIndex].text;
        listObject.options[anIndex].value=listObject.options[anIndex-1].value;
        listObject.options[anIndex].text=listObject.options[anIndex-1].text;
        listObject.options[anIndex-1].value=selectedValue;
        listObject.options[anIndex-1].text=selectedText;
        listObject.options[anIndex].selected=false;
        listObject.options[anIndex-1].selected=true;
        return true;
    } 
}

//将一个列表中的当前选项下移一格，未做类型检查，请小心使用，传人的参数必须为一个SELECT对象
function moveDownItems(listObject)
{
    var i;
    for(i=listObject.options.length-1;i>=0;i--)
    {
        if(listObject.options[i].selected)
        {
            moveDownItem(listObject,i); 
        }
    }
}

function moveDownItem(listObject,anIndex)
{
    var selectedValue,selectedText;
    if(anIndex<0)
    {
        alert("请选择要移动的项");
        return false;
    }
    else if(anIndex==listObject.options.length-1)
    {
         alert("已经在最底端");
         return false;
    }
    else
    {
        selectedValue=listObject.options[anIndex].value;
        selectedText=listObject.options[anIndex].text;
        listObject.options[anIndex].value=listObject.options[anIndex+1].value;
        listObject.options[anIndex].text=listObject.options[anIndex+1].text;
        listObject.options[anIndex+1].value=selectedValue;
        listObject.options[anIndex+1].text=selectedText;
        listObject.options[anIndex].selected=false;
        listObject.options[anIndex+1].selected=true;
        return true;
    }
}

//在SELECT对象中添加不重复的项
function addUniqueItem(anItem,itemSets)
{
     for(var i=0;i<itemSets.length;i++)
     {
         if(anItem.value==(itemSets[i]).value)
         {
             alert("不允许添加重复项");
             return false;
         }
     }
     itemSets.add(anItem);
     return true;
}

//清空SELECT对象
function clearItems(selectObject)
{
   selectObject.options.length=0;
}

//全选SELECT对象
function selectAll(selectObject)
{
  for(var i=0;i<selectObject.options.length;i++)
  {
     selectObject.options[i].selected=true;
  }  
}

//判断radioButton组被checked的Button位置,参数：document.form1.radiobutton，返回checked button 位置
function radioChecked(radioObj)
{
    if(radioObj.length)
    {
        for (var i = 0; i < radioObj.length; i++) 
        {
            if (radioObj[i].checked)
            {
                return i;
            }
        }
    }
    else
    if (radioObj.checked)
    {
        return 0;
    }
    
    return -1;
} 

//在两个SELECT之间交换选择项
function swapOptions(fromSelect,toSelect)
{
    var tmpValue;
    var tmpText;
    var tmpItem;
    var i;
    var added=false;
    
    for(i=fromSelect.options.length;i>0;i--)
    {
        if(fromSelect.options[i-1].selected)
        {
            tmpValue=fromSelect.options[i-1].value;
            tmpText=fromSelect.options[i-1].text;
            fromSelect.options.remove(i-1);
            tmpItem=document.createElement("OPTION");
            tmpItem.value=tmpValue;
            tmpItem.text=tmpText;
            if(added)
            {
                toSelect.options.add(tmpItem,toSelect.options.length-1);
            }
            else
            {
                toSelect.options.add(tmpItem);
                added=true;
            }
        }
    }	
}

function isArray(obj){return(typeof(obj.length)=="undefined"||typeof(obj)=="string")?false:true;}

function putFieldValues(fieldName, fieldValue) {
    if(document.all(fieldName)!=null && fieldValue !=null){
        var fieldObject = document.all(fieldName);
        if(isArray(fieldObject)){
            if(!isArray(fieldValue)){
                fieldValue = new Array(fieldValue);
            }
            if(fieldObject.tagName!=null && fieldObject.tagName.toLowerCase() =='select'){
                for (i = 0; i < fieldValue.length; i++) {
                    for (j = 0; j < fieldObject.length; j++) {
                        if(fieldObject.options[j].value==fieldValue[i]){
                            fieldObject.options[j].selected = true;
                        }
                    }
                }
            }else if(fieldObject[0].type.toLowerCase() =='checkbox' || fieldObject[0].type.toLowerCase() =='radio'){
                for (i = 0; i < fieldValue.length; i++) {
                    for (j = 0; j < fieldObject.length; j++) {
                        if(fieldObject[j].value==fieldValue[i]){
                            fieldObject[j].checked = true;
                        }
                    }
                }
            }
        }else{
            if(fieldObject.type.toLowerCase() =='text' || fieldObject.tagName.toLowerCase() =='textarea'){
                if(isArray(fieldValue)){
                    fieldValue=fieldValue[0];
                }
                fieldObject.value=fieldValue;
            }
        }
    }
}

//網葉打印
function  printPage(objIds){
    if(objIds.length == 0) {
    }else {
        if( objIds.length == 1) {
            var target = window.document.all(objIds);
            target.style.display="none";
        } else {	
            var objArray = objIds.split(",");
        
            for ( var i=0;i<objArray.length;i++) {
                var target = window.document.all(objArray[i]);
                target.style.display="none";	
            }
        }    
    }	

    /*       
    var imagesarray =window.document.images
    for(var i=0;i< imagesarray.length;i++) {
       imagesarray[i].style.display="none";
    }
    */
    window.print();
    if(objIds.length == 0) {
    }else {
        if( objIds.length == 1) {
            var target = window.document.all(objIds);
            target.style.display="";
        } else {	
            var objArray = objIds.split(",");
        
            for ( var i=0;i<objArray.length;i++) {
                var target = window.document.all(objArray[i]);
                target.style.display="";	
            }
        }    
    }
    /*	
    for(var i=0;i< imagesarray.length;i++){
    imagesarray[i].style.display="";
    }
    */
}


function setFormDisabled(formName)
{
  var form1 = document.getElementById(formName);
  if(form1!=null){
    for (var i=0;i<form1.elements.length;i++){
      var fieldObject = form1.elements[i];
      if (fieldObject.tagName.toLowerCase() =='input'){
    if (fieldObject.type.toLowerCase() =='button'||fieldObject.type.toLowerCase()=='submit'||fieldObject.type.toLowerCase()=='reset'){
           fieldObject.disabled=true;
        }
      }
    }
    return true;
  }
}

function setFieldDisabled(fieldName)
{
  var fieldObject = document.getElementById(fieldName);
  if(fieldObject!=null){
    fieldObject.disabled=true;
  }
  return true;
}

function setFieldEnabled(fieldName)
{
  var fieldObject = document.getElementById(fieldName);
  if(fieldObject!=null){
    fieldObject.disabled=false;
  }
  return true;
}

//stop right click.
function stop(){
	//return false;
}
document.oncontextmenu=stop;

function nkey(e)
{
        if(event.keyCode==13 && event.srcElement.type!='button' && event.srcElement.type!='submit' && event.srcElement.type!='reset' && event.srcElement.type!='textarea' && event.srcElement.type!=''){
            event.keyCode=9;
            return(true);
        }

        if (event.keyCode == 8)
        {
            if(event.srcElement.form == null || event.srcElement.isTextEdit == false){
                event.keyCode = 8;
                return false;
            }else{
                event.keyCode = 8;
                return true;
            }
        }
  
        if (event.keyCode == 27)
        {
            alert('ESC Key disable');
            event.keyCode = 8;
            return false;
        }
        if (event.keyCode == 93)
        {
            alert('Menu Key disable');
            event.keyCode = 8;
            return false;
        }
        if (event.keyCode == 122)
        {
            alert('F11 Key disable');
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 78)
        {                	
            alert('CTRL + N disable');       // Disable CTRL N
            event.keyCode = 8;
            return false;
        }	
        if (event.ctrlKey && event.keyCode == 66)
        {                	
            alert('CTRL + B disable');       // Disable CTRL B
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 68)
        {                	
            alert('CTRL + D disable');       // Disable CTRL D
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 76) // Disable CTRL L
        {                	
            alert('CTRL + L disable');     
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 79) // Disable CTRL O
        {                	
            alert('CTRL + O disable');     
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 82) // Disable CTRL R
        {                	
            alert('CTRL + R disable');     
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 72)            // Disable CTRL H
        {                	
            alert('CTRL + H disable');     
            event.keyCode = 8;
            return false;
        }
        if (event.ctrlKey && event.keyCode == 73)            // Disable CTRL I
        {                	
            alert('CTRL + I disable');     
            event.keyCode = 8;
            return false;
        }
         if(event.keyCode == 116)       // F5 reload
                {
              alert('F5 disable');     
          event.keyCode = 8;
          return false;
         }

         if(event.keyCode == 117)       // F6 reload
         {
          alert('F6 disable');     
          event.keyCode = 8;
          return false;
         }

        if ( window.event.altKey && event.keyCode == 36)     // Disable ALT HOME
        {                      
          alert('ALT + HOME disable');                       
        } 
}

function toTargetFormgoToTarget(targetUrl){
    document.form1.action = targetUrl;
    document.form1.target = "mainFrame";
    document.form1.submit();
}
