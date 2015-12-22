<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


$('#ajaxError').ajaxError(displayAjaxError);

function displayAjaxError(){
	displayWaitMask();
	$('#ajaxErrorDate').empty();
	var date = new Date();
	$('#ajaxErrorDate').append(''+date.getHours()+':'+date.getMinutes()+':'+date.getSeconds())
	$('#ajaxError').show();
}









function unvalidate(button){
	var confirm = window.confirm('Etes vous sur de vouloir revenir à la dernière version validée par le MJ?');
	if(confirm){
		$(button).parent().submit();
	}
}


