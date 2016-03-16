
function attributePlayer(personnageId, tableId){
	$('#obstructionMask').show();
	$('#modalDiv').append('<img src="'+context+'/img/wait.gif" class="waitImg" alt="Attente Serveur..." width="35" height="35"/>');
	$('#modalDiv').show();
	
	var data = new Object();
	data.personnageId = personnageId;
	data.tableId = tableId;
	$('#modalDiv').load(context+'/AttributePlayerInTable', data,  function(){
		$("#cancelAttribution").on("click", cancelAttribution);
	});
}


function cancelAttribution(){
	$('#modalDiv').hide();
	$('#obstructionMask').hide();
	$('#modalDiv').empty();
}


function showHideMessage(authorSpan){
	var messageContent = $(authorSpan).parent().parent().children('.messageContent');
	var image = $(authorSpan).children('img')[0];
	var imgSrc = image.src;
	if(imgSrc.indexOf("img/expandable.jpg")>=0){
		messageContent.show();
		image.src = imgSrc.substring(0, imgSrc.lastIndexOf("expandable.jpg"))+"expanded.jpg";
	}else{
		messageContent.hide();
		image.src = imgSrc.substring(0, imgSrc.lastIndexOf("expanded.jpg"))+"expandable.jpg";
	}
}


function deleteMessage(imageForm){
	var validCallback = function(){
		$(imageForm).parent().submit();
	};
	showConfirm( 'Voulez-vous supprimer ce message?', validCallback);
}