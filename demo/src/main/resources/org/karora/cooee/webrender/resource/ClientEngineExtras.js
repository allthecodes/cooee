var hadRenderred = false;
var movingDiv = null;
var tempMargin = -100;
var movingStep = 2;
EchoClientEngine.renderCustomLoadStatus = function(loadStatusDivElement) {
	if (hadRenderred) return;
	loadStatusDivElement.removeChild(loadStatusDivElement.firstChild);
	
	loadStatusDivElement.style.visibility = "hidden";
	loadStatusDivElement.style.position = "absolute";
	loadStatusDivElement.style.top = "45%";
	loadStatusDivElement.style.left = "45%";
	loadStatusDivElement.style.right = "45%";
	loadStatusDivElement.style.width = "100px";
	loadStatusDivElement.style.height = "20px";
	loadStatusDivElement.style.verticalAlign = "middle";
	loadStatusDivElement.style.overflow = "hidden";
	loadStatusDivElement.style.border = "1px inset #0055DD";

	movingDiv = document.createElement("div");
	movingDiv.style.position = "absolute";
	movingDiv.style.color = "#0055DD";
	movingDiv.style.top = "0px";
	movingDiv.style.marginLeft = "0px";
	movingDiv.style.width = "100px";
	movingDiv.style.height = "20px";
	movingDiv.style.backgroundColor = "#0055DD";

	loadStatusDivElement.appendChild(movingDiv);

	var textDiv = document.createElement("div");
	textDiv.style.position = "absolute";
	textDiv.style.top = "0px";
	textDiv.style.fontSize = "10px";
	textDiv.style.paddingTop = "3px";

	loadStatusDivElement.appendChild(textDiv);
	
	textDiv.innerHTML = "loading...";
	
	loadingAnimation();

	hadRenderred = true;
	loadStatusDivElement.style.visibility = "visible";
};

loadingAnimation = function() {
	if (EchoClientEngine.loadStatus != -1) {
		movingDiv.style.marginLeft = tempMargin + 'px';
		tempMargin += movingStep;
		if (tempMargin > 100) {
			movingStep *= -1;
			tempMargin = 100;
		} if (tempMargin < -100) {
			movingStep *= -1;
			tempMargin = -100;
		}
		setTimeout('loadingAnimation()',10);
	}
}