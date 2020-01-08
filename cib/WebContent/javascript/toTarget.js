
function toTarget(targetUrl){
    document.form1.action = targetUrl;
    document.form1.target = "mainFrame";
    document.form1.submit();
}
