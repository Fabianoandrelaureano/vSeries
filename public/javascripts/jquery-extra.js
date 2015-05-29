/**
 * 
 */

$(document).ready(function(){
    $('.lista').hide();
	$("div.span5").addClass('up_arrow')
    $("div.span5").click(function(){
    $(this).toggleClass('up_arrow').toggleClass('down_arrow');
    	 $(this).next().slideToggle('slow')
	 });
	
	$("#close").click(function(){
		$("#successPanel").fadeOut("slow");
		});
});