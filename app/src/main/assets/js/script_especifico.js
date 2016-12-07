var Lancamento = function(ident, fornecedor, descricao, data, valor) {
  this.ident = myTrim(ident);
  this.fornecedor = myTrim(fornecedor);
  this.descricao = myTrim(descricao);
  this.data = myTrim(data);
  this.valor = myTrim(valor);
 };
function myTrim(vr){
	if(vr !=null ){
		return vr.trim();
	}
	return vr;
}
Lancamento.prototype.printar = function(){
  return "cpf/cnpj= "+this.ident + "\nfornecedor: " + this.fornecedor+ "\ndescricao: " + this.descricao+ "\ndata: " + this.data+ "\nvalor: " + this.valor;
};
//Salvar conteúdo como arquivo
function save(s){
	var hiddenElement = document.createElement('a');
	hiddenElement.href = 'data:attachment/text,' + encodeURI(s);
	hiddenElement.target = '_blank';
	hiddenElement.download = 'gastos.txt';
	hiddenElement.click();
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
	}
	catch(err) {
		alert(err);
	}

	return null;
}

function getTitulo(){
	var titulo = document.getElementsByTagName("h1");
	if(titulo.length > 1 ){
	    titulo_mes = document.getElementsByTagName("h1")[1].innerText.trim();
	   }
}

function getContentFromTable(table){
	var aux = new Array();
	if(table !=null){
        var tr = table.getElementsByTagName("tr");

        for (j = 1; j < table.rows.length; j++) {
            var id = table.rows[j].cells[0].innerText.trim();
            if(id != null && id.trim()==""){
                id = tr[j].getElementsByTagName("td")[0].textContent.trim();
            }
            var forn = table.rows[j].cells[1].innerText.trim();
            if(forn != null && forn.trim()==""){
                forn = tr[j].getElementsByTagName("td")[1].textContent.trim();
            }
            var desc = table.rows[j].cells[2].innerText.trim();
            if(desc != null && desc.trim()==""){
                desc = tr[j].getElementsByTagName("td")[2].textContent.trim();
            }
            var data = table.rows[j].cells[3].innerText.trim();
            if(data != null && data.trim()==""){
                data = tr[j].getElementsByTagName("td")[3].textContent.trim();
            }
            var valor = table.rows[j].cells[4].innerText.trim();
            if(valor != null && valor.trim()==""){
                valor = tr[j].getElementsByTagName("td")[4].textContent.trim();
            }

            var cc = new Lancamento(id, forn, desc, data, valor);

            aux.push(cc);
        }
	}
	return aux;
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

function createJSON(){
	var _json = '{ "titulo":"'+titulo_mes+'", "lancamentos":[';
		_json += JSON.stringify(content_mes).replace(/\[/g, "").replace(/\]/g, "");
	_json += ']';
	_json += '}';
	
	return _json;
}

var content_mes = new Array();
var titulo_mes = "";

function extractAllData(){
	var list = document.getElementsByTagName('table');

    if(list!=null && list.length > 0){
        getTitulo();
        content_mes = getContentFromTable(list[0]);
        //save(createJSON());
        return 1;
    } else {
        return 0;
    }
	//save(createJSON());
}

/*for (j = 0; j < list.length; j++) {
	var auxStr = list[j].rows[0].cells[0].innerText;
	alert(auxStr);
}*/
