<!DOCTYPE>
<html>
<head>
<link href="pdfjs/artDialog/skins/chrome.css" rel="stylesheet" />

<script type="text/javascript" src="pdfjs/artDialog/artDialog.js"></script>
<script type="text/javascript" src="pdfjs/artDialog/jquery.js"></script>
<script type="text/javascript" src="pdfjs/artDialog/artDialog.iframeTools.js"></script>
    <script type="text/javascript" src="pdfjs/artDialog/jquery-2.1.1.min.js"></script>
<script type="text/javascript">
function viewConfirmation(){
	//art.dialog('这是php点点通的教程', function () {alert('你点击确定了')},function(){alert('你点击取消了');});
	var encodeUrl = encodeURIComponent("/vocabulary/fileDownload?filePath=RocketMQ_experience.pdf");
	var urlSrc="pdfjs/web/viewer.html?file="+encodeUrl;
	art.dialog.open(urlSrc,{title: '打印',width:1000,height:800});
}

Array.prototype.Contains = function(element){
    for(var i=0; i<this.length; i++){
        if(this[i]==element){
            return true;
        }
    }
    return false;
};

</script>
</head>
<body>
	<h2>Hello World!</h2>
	<a href="toPage1.htm">To Page1</a>
	<button class="btn-1" type="button" id="btnViewConfirmation" onclick="viewConfirmation()" >查看</button>
<!-- 	<iframe
		src="/pdfJs/pdfjs/web/viewer.html?file=/pdfJs/file/download.htm" 
		width="100%" height="800"></iframe> -->
</body>
</html>