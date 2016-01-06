<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>












function unvalidate(button){
	var confirm = window.confirm('Etes vous sur de vouloir revenir à la dernière version validée par le MJ?');
	if(confirm){
		$(button).parent().submit();
	}
}


