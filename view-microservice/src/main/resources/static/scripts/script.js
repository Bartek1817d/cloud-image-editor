var inputs = document.getElementsByTagName("input");
function checkboxEventHandler(event) {
	var checkbox = event.target;			
	var rangeInput = checkbox.parentNode.previousSibling.firstChild;
		if(checkbox.checked)
			rangeInput.disabled = false;
		else
			rangeInput.disabled = true;
}
function resetEventHandler(event) {
	for(i = 0; i < inputs.length; i++) {
		var input = inputs[i];
		if(input.type == "range")
			input.disabled = false;
	}
}
for(i = 0; i < inputs.length; i++) {
	var input = inputs[i];
	if(input.type == "checkbox")
		input.addEventListener("click", checkboxEventHandler);
	else if(input.type == "reset")
		input.addEventListener("click", resetEventHandler);
}
