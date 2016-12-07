var img_string = '';
var titulo_despesa = '';
var moeda = "R$ ";
var content_meses = new Array();
/*for (j = 0; j < list.length; j++) {
	var auxStr = list[j].rows[0].cells[0].innerText;
	alert(auxStr);
}*/
var Mes = function(label, valor, link) {
  this.label = label.trim();
  this.valor = valor.trim();
  this.link = link;

  if(this.link != null){
	  this.link = this.link.trim();
	  this.link = this.link.replace(/href=\"/, "");
	  this.link = this.link.replace(/\"/, "");
  }
};
Mes.prototype.printar = function(){
  return this.label + " = " + this.valor+"\n"+this.link;
};

//extractAllData();
function extractAllData(){
    /*$(document).ready(function(){
        getGrafico();
    });*/

    getGrafico();
    getTitulo();
    var list = document.getElementsByTagName('table');

    if(list!=null && list.length > 0){
        content_meses = getContentFromTable(list[0]);
        //save(createJSON());
        return 1;
    } else {
        return 0;
    }
}

function mainScript(){

	//Send variable to android activity
	try {//Coloco no try/catch pra mesmo se rodar fora do android funcionar...
		var conseguiu = extractAllData();//Extraio todos os dados da tabela
		if(conseguiu==1){

			var jsonStringFinal = createJSON(); //crio uma string na sintaxe json

			return jsonStringFinal;
		} else {
			return null;
		}
		//alert("HTML side SUCCESS");


		//return jsonStringFinal;
	}
	catch(err) {
		//alert(err);
	}

	return null;
}
//Salvar conteúdo como arquivo
function save(s){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:attachment/text,' + encodeURI(s);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'gastos.txt';
	hiddenElement.click();
}

function getTitulo(){
	var titulo = document.getElementsByTagName("h1");
	if(titulo.length > 1 ){
	    titulo_despesa = document.getElementsByTagName("h1")[1].innerText.trim();
	   }
}

function contains(base, str){
	return (base.toLowerCase().indexOf(str.toLowerCase())>-1);
}

function arrayContaToString(title, array){
	var auxStr = "";
	for (j = 0; j < array.length; j++) {
		auxStr = auxStr + array[j].printar()+"\n";
	}
	alert(title+"\n"+auxStr);
}

function capitalizeString(str){
    return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
}

function encode_utf8(s) {
    return unescape(encodeURIComponent(s));
}

function decode_utf8(s) {
    return decodeURIComponent(escape(s));
}

function getLinks(table){
	var array = new Array();
	if(table!=null){
		var auxStr = table.innerHTML.toString().match(/href="(.*?)"/gi);
	}
	//Se tabela tiver link...
	//var patt = new RegExp(/href="/);
	//if (patt.test(table.innerHTML.toString())){
	if (auxStr!=null){
		//Pego todos os links
		for (j = 0; j < auxStr.length; j++) {
			/**
			* Variação da página. Pode haver um link no corpo do titulo.
			* Essa é apenas uma variação. Caso seja necessário, adicionar mais aqui...
			* Isso acontece APENAS de 2014 para trás, no caso dos correios.
			*/
			if(contains(auxStr[j], "/leg/")){
				continue;
			}
			array.push(auxStr[j].toString());
		}
		for (j = 0; j < 5; j++) {
			array.push(null);
		}
	} else {
		//Adiciono valores apenas para não dar crash
		for (j = 0; j < 15; j++) {
			array.push(null);
		}
	}
	return array;
}

function getContentFromTable(table){
	var aux = new Array();
	var links = new Array();
	links = getLinks(table);

	if(table !=null){
		var tr = table.getElementsByTagName("tr");

		for (j = 1; j < tr.length; j++) {
			//As vezes no navegador chrome dá bug e não reconhece innerText de algumas tabelas (?), então utilizo outra propriedade
			var despesaTitulo = table.rows[j].cells[0].innerText.trim();
            if(despesaTitulo != null && despesaTitulo.trim()==""){
                despesaTitulo = tr[j].getElementsByTagName("td")[0].textContent.trim();
            }

            var despesaValor = table.rows[j].cells[1].innerText.trim();

            if(despesaValor !=null && despesaValor.trim()==""){
                despesaValor = tr[j].getElementsByTagName("td")[1].textContent.trim();
            }

            despesaValor = moeda + despesaValor;

			//Esquema para atribuir corretamente caso algum campo do "meio" não possua link. Só coloco link em quem realmente tem.
			var a = table.rows[j].cells[1].getElementsByTagName("a");
			if(a != null && a.length==1){
				var cc = new Mes(despesaTitulo, despesaValor, links[0]);
				links.shift();
			} else {
    			var cc = new Mes(despesaTitulo, despesaValor, null);
			}
    		aux.push(cc);
	    }
    }

    return aux;
}

function getGrafico(){
	var svg = document.querySelector( "svg" );
	var svgData = new XMLSerializer().serializeToString( svg );
	var canvas = document.createElement('canvas');
	canvg(canvas, svgData);
	var imgBase64 = canvas.toDataURL("image/png", 1.0);

	img_string = imgBase64;
	return imgBase64;
}

function createJSON(){
	var _json = '{"grafico":"'+img_string+'",';
	_json += '"titulo":"'+titulo_despesa+'",';
	_json += '"link":"'+window.location.href +'",';
	_json += '"meses":[';
		_json += JSON.stringify(content_meses).replace(/\[/g, "").replace(/\]/g, "");
	_json += ']';
	_json += '}';

	return _json;
}

/*for (j = 0; j < list.length; j++) {
	var auxStr = list[j].rows[0].cells[0].innerText;
	alert(auxStr);
}*/











