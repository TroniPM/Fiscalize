/*
//Adicionar a LINHA 70 do ExpandableListAdapter esse snnipet abaixo. 
        viewHolder.despesaTitulo.setText(exp_obj.nome);
        viewHolder.despesaValor.setText(exp_obj.valor);
		//Verificação para só aparecer icone de expandir se HOUVER link no ExpandableCollection
		if(exp_obj.link == null){
			((TextView) convertView.findViewById(R.id.expListViewL2_imageView)).setVisibility(View.INVISIBLE);
		}
//
//Adicionar a LINHA 376 do SenadorFragmentMain esse snnipet abaixo
									//Isso fará com que só vá para a tela de detalhamento se houver link
									if(obj_exp.link != null){
										//DO ACTION. CHAMAR TELA COM GASTOS DO ANO PARA ESSA DESPESA
									}

*/
/**
NÃO ALTERAR ESSES IDS, A MENOS QUE MUDE NA PÁGINA
*/
var label_1 = 'accordion-ceaps';
var label_2 = 'accordion-outros-gastos';
var label_3 = 'accordion-outros';
var label_4 = 'accordion-pessoal';
var label_5 = 'accordion-subsidios';
var siglaMoeda = "R$ ";

//dados
var senador_nome = '';
var senador_data_nascimento = '';
var senador_naturalidade = '';
var senador_gabinete = '';
var senador_telefones = '';
var senador_fax = '';
var senador_email = '';
var senador_escritorio_apoio = '';
var senador_foto_url = '';
var senador_partido = '';
var senador_ano = '';
var senador_anos_disponiveis = new Array();

//Arrays contendo titulos e dados
var array_ceap = new Array();
var title_ceap = "";
var array_outros_gastos = new Array();
var title_outros_gastos = "";
var array_outros_beneficios = new Array();
var title_outros_beneficios = "";
var array_pessoal = new Array();
var title_pessoal = "";
var array_subsidios_aposentadoria = new Array();
var title_subsidios_aposentadoria = "";

var Conta = function(label, valor, link) {
  this.label = label.trim();
  this.valor = valor.trim();
  this.link = link;

  if(this.link != null){
	  this.link = this.link.trim();
	  this.link = this.link.replace(/href=\"/, "");
	  this.link = this.link.replace(/\"/, "");
  }
};

var Ano = function(label, link) {
  this.label = label.trim();
  this.link = link;

  if(this.link != null){
	  this.link = this.link.trim();
	  this.link = this.link.replace(/href=\"/, "");
	  this.link = this.link.replace(/\"/, "");
  }
};
Number.prototype.formatMoney = function(c, d, t){
var n = this,
    c = isNaN(c = Math.abs(c)) ? 2 : c,
    d = d == undefined ? "." : d,
    t = t == undefined ? "," : t,
    s = n < 0 ? "-" : "",
    i = String(parseInt(n = Math.abs(Number(n) || 0).toFixed(c))),
    j = (j = i.length) > 3 ? j % 3 : 0;
   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
 };
//Função para a classe conta
Conta.prototype.printar = function(){
  return this.label + " = " + this.valor;
};

Ano.prototype.printar = function(){
  return this.label + " --> " + this.link;
};

//Salvar conteúdo como arquivo
function save(s){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:attachment/text,' + encodeURI(s);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'gastos.txt';
	hiddenElement.click();
}
/**
*
*
*/
//extractAllData();
/**
*
*
*/
function extractAllData(){
	var list = document.getElementsByTagName('table');

	if(list!=null && list.length > 0){
		getSenadorData();

		getUrlFoto();

		getPartido();

		getCurrentAno();

		//Extraindo dados
		if(list.length>=1){
			array_ceap = getContentFromTable(list[0]);
			title_ceap = getTitle(label_1);
		}
		if(list.length>=2){
			array_outros_gastos = getContentFromTable(list[1]);
			title_outros_gastos = getTitle(label_2);
		}
		if(list.length>=3){
			array_outros_beneficios = getContentFromTable(list[2]);
			title_outros_beneficios = getTitle(label_3);
		}
		if(list.length>=4){
			array_pessoal = getContentFromTable(list[3]);
			title_pessoal = getTitle(label_4);
		}
		if(list.length>=5){
			array_subsidios_aposentadoria = getContentFromTable(list[4]);
			title_subsidios_aposentadoria = getTitle(label_5);
		} else {
			title_subsidios_aposentadoria = getTitle(label_5);
			var cc = new Conta(document.getElementById(label_5).innerText.split("\n")[1], "", null);
			array_subsidios_aposentadoria.push(cc);
		}

		getAnos();

		//Salvando
		//save(createJSON());
		return 1;
	}
	return 0;
}

function getValueOfHTMLObject(obj){
	if(obj==null)
		return null;
	return (obj.innerText.trim()!="") ? obj.innerText.trim() : obj.textContent.trim();
}

function getContentFromTable(table){
	var icon = "↳ ";
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
				if(tr[j].getElementsByTagName("td")[0].getElementsByTagName("i").length==1){
					despesaTitulo = icon + despesaTitulo;
				}
			} else {
			    if(table.rows[j].cells[0].getElementsByTagName("i").length==1){
            		despesaTitulo = icon + despesaTitulo;
            	}
			}

			var despesaValor = table.rows[j].cells[1].innerText.trim();

			if(despesaValor !=null && despesaValor.trim()==""){
				despesaValor = tr[j].getElementsByTagName("td")[1].textContent.trim();
			}

			convertStringToMoney(despesaValor);
			//Esquema para atribuir corretamente caso algum campo do "meio" não possua link. Só coloco link em quem realmente tem.
			var a = table.rows[j].cells[1].getElementsByTagName("a");
			if(a != null && a.length==1){
				var cc = new Conta(despesaTitulo, despesaValor, links[0]);
				links.shift();
			} else {
				var cc = new Conta(despesaTitulo, despesaValor, null);
			}


			//var cc = new Conta(despesaTitulo, despesaValor, links[j-1]);
			//var cc = new Conta(tr[j].getElementsByTagName('td')[0].innerText, tr[j].getElementsByTagName('td')[1].innerText, links[j-1]);
			aux.push(cc);
		}
	}
	return aux;
}

function getPartido(){
	var small = document.getElementsByTagName("small")[0].innerText.trim();
	senador_partido = small;

}

function getCurrentAno(){
	var title = document.getElementsByTagName("title")[0].innerText.toString().replace(/\n/g, " " ).split(" ");
	senador_ano = title[title.length - 1].trim();
}

function convertStringToMoney(str){
	var aux = str.replace(".","").replace(",",".");
	var num = "";
	try{
		num = parseFloat(aux);
		if(!isNaN(num)){
			num = num.formatMoney(2, ',', '.')
			return siglaMoeda + num;
		}
	}catch(err){
	}
	return aux;
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

function contains(base, str){
	return (base.toLowerCase().indexOf(str.toLowerCase())>-1);
}

function getTitle(divId){
	var v1 = document.getElementById(divId);
	return (v1.innerText.split("\n")[0]);
}

function getUrlFoto(){
	var c = document.createElement('canvas');
    var ctx=c.getContext("2d");
    var img=document.getElementsByClassName("foto")[0].getElementsByTagName( 'img' )[0];//.childNodes[0];

	senador_foto_url = img.src;
}

function openJanela(url){
	var win = window.open(url, '_blank');
	win.focus();
}

function convertImageToCanvas(image) {
	var canvas = document.createElement("canvas");
	canvas.width = Math.floor(image.width/2);
	canvas.height = Math.floor(image.height/2);

	canvas.getContext("2d").drawImage(image, 0, 0,Math.floor( image.width/2 ),Math.floor(image.height/2));

	return canvas;
}

function getSenadorData(){
	var dl = document.getElementsByTagName("dl")[0].innerText;

	if(dl != null) {
		dl = dl.split("\n");
		if(dl.length>=2){
			senador_nome = dl[1];
		}
		if(dl.length>=4){
			senador_data_nascimento = dl[3];
		}
		if(dl.length>=6){
			senador_naturalidade = dl[5];
		}
		if(dl.length>=8){
			senador_gabinete = dl[7];
		}
		if(dl.length>=10){
			senador_telefones = dl[9];
		}
		if(dl.length>=12){
			senador_fax = dl[11];
		}
		if(dl.length>=14){
			senador_email = dl[13];
		}
	/**
	* As informações podem variar. O mais normal é ter de nome até e-mail...
	* Por esse caso, não é interessante colocar os "apoios"
	*/
	}
}

function dadosPessoais(){
	var str = "Nome: "+senador_nome+"\n";
	str = str+"Data de Nascimento: "+senador_data_nascimento+"\n";
	str = str+"Naturalidade: "+senador_naturalidade+"\n";
	str = str+"Gabinete: "+senador_gabinete+"\n";
	str = str+"Telefones: "+senador_telefones+"\n";
	str = str+"Fax: "+senador_fax+"\n";
	str = str+"E-mail: "+senador_email+"\n";
	str = str+"Escritório de Apoio: "+senador_escritorio_apoio+"\n";

	alert(str);
}

function getAnos(){
	var aux = document.getElementsByClassName("sen-barra-filtro pull-right")[0].getElementsByTagName('a');

	for (j = 0; j < aux.length; j++) {
		var cc = new Ano(aux[j].innerText, aux[j].href);
		senador_anos_disponiveis.push(cc);
	}
}

function arrayContaToString(title, array){
	var auxStr = "";
	for (j = 0; j < array.length; j++) {
		auxStr = auxStr + array[j].printar()+"\n";
	}
	alert(title+"\n"+auxStr);
}

function createJSON(){
	var _json = '{"nome":"'+senador_nome+'",';
		_json += '"nascimento":"'+senador_data_nascimento+'",';
		_json += '"naturalidade":"'+senador_naturalidade+'",';
		_json += '"gabinete":"'+senador_gabinete+'",';
		_json += '"telefones":"'+senador_telefones+'",';
		_json += '"fax":"'+senador_fax+'",';
		_json += '"link":"'+window.location.href +'",';
		_json += '"email":"'+senador_email+'",';
		_json += '"foto_url":"'+senador_foto_url+'",';
		_json += '"partido":"'+senador_partido+'",';
		_json += '"currentAno":"'+senador_ano+'",';
		_json += '"titulo_ceap":"'+title_ceap+'",';
		_json += '"titulo_outros":"'+title_outros_gastos+'",';
		_json += '"titulo_beneficios":"'+title_outros_beneficios+'",';
		_json += '"titulo_pessoal":"'+title_pessoal+'",';
		_json += '"titulo_subsidio":"'+title_subsidios_aposentadoria+'",';
		//_json += '"apoio":"'+senador_escritorio_apoio+'",';
		_json += '"anos":[';
			_json += JSON.stringify(senador_anos_disponiveis).replace(/\[/g, "").replace(/\]/g, "");
		_json += ']';
		_json += ',';
				
		_json += '"dados1":[';
			_json += JSON.stringify(array_ceap).replace(/\[/g, "").replace(/\]/g, "");
		_json += '],';
		_json += '"dados2":[';
			_json += JSON.stringify(array_outros_gastos).replace(/\[/g, "").replace(/\]/g, "");
		_json += '],';
		_json += '"dados3":[';
			_json += JSON.stringify(array_outros_beneficios).replace(/\[/g, "").replace(/\]/g, "");
		_json += '],';
		_json += '"dados4":[';
			_json += JSON.stringify(array_pessoal).replace(/\[/g, "").replace(/\]/g, "");
		_json += '],';
		_json += '"dados5":[';
			_json += JSON.stringify(array_subsidios_aposentadoria).replace(/\[/g, "").replace(/\]/g, "");
		_json += ']';

	//Finalizo arquivo
	_json += '}';
	return _json;
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

function sendJSONtoAndroid(s) {
	Android.getJSON(s);
}
function teste_js(teste){
	return "funcionouuuuu!";
}



/*for (j = 0; j < list.length; j++) {
	var auxStr = list[j].rows[0].cells[0].innerText;
	alert(auxStr);
}*/