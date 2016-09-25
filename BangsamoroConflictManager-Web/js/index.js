
var home = {};
home.fetch=0;
home.cnt=0;
home.databaseURL="https://bangsamoro-conflict-manager.firebaseio.com/";

home.getLogin =function(){
var xhr = new XMLHttpRequest();
  xhr.open('GET', 'index.html', true);
  xhr.send();
  xhr.onreadystatechange = function() {
    if (xhr.readyState == 4) {


		if(xhr.responseText!='')
        	document.getElementById("box").innerHTML=xhr.responseText;
       

    } 
  } 
   

};

home.addData =function(){
var xhr = new XMLHttpRequest();
  xhr.open('GET', 'add.html', true);
  xhr.send();
  xhr.onreadystatechange = function() {
    if (xhr.readyState == 4) {


		if(xhr.responseText!='')
        	document.getElementById("box").innerHTML=xhr.responseText;
        document.getElementById('date').value=today();
        
       addInput("province",data.province);
       addInput("municipality",data.AgusandelNorte);
       addInput("barangay",data.adnBuenavista);
       addInput("category",data.category);
       addInput("manifest",data.manifest);
       addInput("humancost",data.humancost);
       addInput("actor",data.actor);
    } 
  } 
   

};
  function addInput(divName,choices) {
      var newDiv = document.createElement('div');
      var selectHTML = "";
      selectHTML="<select>";
      for(i = 0; i < choices.length; i = i + 1) {
          selectHTML += "<option value='" + choices[i] + "'>" + choices[i] + "</option>";
      }
      selectHTML += "</select>";
      newDiv.innerHTML = selectHTML;
      document.getElementById(divName).innerHTML=selectHTML;
  }


  function populateMunicipality(){
    var prov = document.getElementById("province").value;

    switch(prov) {
        case "Agusan del Norte":
            addInput("municipality",data.AgusandelNorte);
            break;
        case "Agusan del Sur":
            addInput("municipality",data.AgusandelSur);
            break;
        case "Basilan":
            addInput("municipality",data.Basilan);
            break;
        case "Bukidnon":
            addInput("municipality",data.Bukidnon);
            break;        
        case "Camiguin":
            addInput("municipality",data.Camiguin);
            break;
        case "Compostela Valley":
            addInput("municipality",data.CompostelaValley);
            break; 
        case "Davao del Norte":
            addInput("municipality",data.DavaodelNorte);
            break; 
        case "Davao del Sur":
            addInput("municipality",data.DavaodelSur);
            break; 
        case "Davao Occidental":
            addInput("municipality",data.DavaoOccidental);
            break; 
        case "Davao Oriental":
            addInput("municipality",data.DavaoOriental);
            break;       
        case "Dinagat Islands":
            addInput("municipality",data.DinagatIslands);
            break;       
        case "Lanao del Norte":
            addInput("municipality",data.LanaodelNorte);
            break;   

    } 
  }
  function populateBarangay(){
    var mun = document.getElementById("municipality").value;

    switch(mun) {
        case "Buenavista":
            addInput("barangay",data.adnBuenavista);
            break;     
        case "Butuan":
            addInput("barangay",data.adnButuan);
            break;  
        case "Bayugan":
            addInput("barangay",data.adsBayugan);
            break;                      
    } 
  }  

function today(){
    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth()+1; //January is 0!
    var yyyy = today.getFullYear();

    if(dd<10) {
        dd='0'+dd
    } 

    if(mm<10) {
        mm='0'+mm
    } 

    return mm+'/'+dd+'/'+yyyy;
}  
function isOdd(num) { return num % 2;}
home.backHome =function(){
	if (home.fetch>-1){
       	if (isOdd(home.fetch)){
			home.fetch= parseInt(home.fetch)-3;
	 	}else {
	 		home.fetch= parseInt(home.fetch)-4;

	 	}

	 }
	 if(home.fetch<0)
	 	home.fetch =0;	

	home.getHome();
 
}

			