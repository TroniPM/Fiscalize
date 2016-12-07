package com.troni.fiscalize.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.troni.fiscalize.people.senador.Senador;
import com.troni.fiscalize.session.Session;

import java.util.ArrayList;

/**
 * Created by PMateus on 16/09/2015.
 * For project SIG@Viewer.
 * Contact: <paulomatew@gmail.com>
 */
public class SqliteManager {
    /*
    http://randomkeygen.com/
    TODO trocar keys a cada versão do app
     */
    /* Nome do Banco de Dados */
    private final String UNIQ_KEY = "5q7TA#Y";
    private final String DATABASE_NAME = UNIQ_KEY + "Fiscalize";
    /*Entidades do BD*/
    private final String table_name_user = "usuario", table_name_periodo = "periodo", table_name_materia = "materia";

    /* Modo de acesso ao banco de dados
     *
     * Configura as permissões de acesso ao banco de dados.
     *
     * 0 - Modo privado (apenas essa aplicação pode usar o banco).
     * 1 - Modo leitura para todos (outras aplicações podem usar o banco).
     * 2 - Modo escrita para todos (outras aplicações podem usar o banco).
     *
     */
    private final int DATABASE_ACESS = 0;

    private final String table_name_senador_lista = "senador_lista";
    private final String sen_lista_col_id = "id", sen_lista_col_nome = "nome", sen_lista_col_numero = "numero";
    private final String SQL_CREATE_TABLE_SENADOR_LISTA = "CREATE TABLE IF NOT EXISTS " + table_name_senador_lista + "( " +
            sen_lista_col_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            sen_lista_col_numero + " INTEGER UNIQUE, " +
            sen_lista_col_nome + " TEXT );";

    /* Classe com métodos para executar os comandos SQL e manipular o banco de dados. */
    private SQLiteDatabase banco;
    private Context _context;

    public SqliteManager(Context context) {
        Session.createLog(this.getClass().getName(), "CONSTRUCTOR SqliteManager()", null);

        this._context = context;
        removerBdVersoesAnteriores();

        this.banco = context.openOrCreateDatabase(DATABASE_NAME, DATABASE_ACESS, null);

        this.banco.execSQL(SQL_CREATE_TABLE_SENADOR_LISTA);

    }

    public void removerBdVersoesAnteriores() {
        /*final String bd1 = "ficalize";

        if (DATABASE_NAME.equals(bd1)) {
            this._context.deleteDatabase(bd1);
        }*/
    }

    public void close() {
        banco.close();
        banco = null;
    }
/*
    public boolean isTableExists(String tableName) {

        Cursor cursor = banco.rawQuery("select name from sqlite_master where name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }*/


    /**
     * Caso o website portal transparência mude os ids, alterar este array e resetar BD.
     */
    public void insertSenadores() {
        ArrayList<Senador> array = new ArrayList<Senador>();
        array.add(new Senador("Aécio Neves - PSDB/MG", "391"));
        array.add(new Senador("Aloysio Nunes Ferreira - PSDB/SP", "846"));
        array.add(new Senador("Alvaro Dias - PV/PR", "945"));
        array.add(new Senador("Ana Amélia - PP/RS", "4988"));
        array.add(new Senador("Angela Portela - PT/RR", "4697"));
        array.add(new Senador("Antonio Anastasia - PSDB/MG", "5529"));
        array.add(new Senador("Antonio Carlos Valadares - PSB/SE", "3"));
        array.add(new Senador("Armando Monteiro - PTB/PE", "715"));
        array.add(new Senador("Ataídes Oliveira - PSDB/TO", "5164"));
        array.add(new Senador("Benedito de Lira - PP/AL", "3823"));
        array.add(new Senador("Cidinho Santos - PR/MT", "5108"));
        array.add(new Senador("Ciro Nogueira - PP/PI", "739"));
        array.add(new Senador("Cristovam Buarque - PPS/DF", "3398"));
        array.add(new Senador("Dalirio Beber - PSDB/SC", "5132"));
        array.add(new Senador("Dário Berger - PMDB/SC", "5537"));
        array.add(new Senador("Davi Alcolumbre - DEM/AP", "3830"));
        array.add(new Senador("Deca - PSDB/PB", "5199"));
        array.add(new Senador("Edison Lobão - PMDB/MA", "16"));
        array.add(new Senador("Eduardo Amorim - PSC/SE", "4721"));
        array.add(new Senador("Eduardo Braga - PMDB/AM", "4994"));
        array.add(new Senador("Eduardo Lopes - PRB/RJ", "4767"));
        array.add(new Senador("Elmano Férrer - PTB/PI", "5531"));
        array.add(new Senador("Eunício Oliveira - PMDB/CE", "612"));
        array.add(new Senador("Fátima Bezerra - PT/RN", "3713"));
        array.add(new Senador("Fernando Bezerra Coelho - PSB/PE", "5540"));
        array.add(new Senador("Fernando Collor - PTC/AL", "4525"));
        array.add(new Senador("Flexa Ribeiro - PSDB/PA", "3634"));
        array.add(new Senador("Garibaldi Alves Filho - PMDB/RN", "87"));
        array.add(new Senador("Gladson Cameli - PP/AC", "4558"));
        array.add(new Senador("Gleisi Hoffmann - PT/PR", "5006"));
        array.add(new Senador("Hélio José - PMDB/DF", "5100"));
        array.add(new Senador("Humberto Costa - PT/PE", "5008"));
        array.add(new Senador("Ivo Cassol - PP/RO", "5004"));
        array.add(new Senador("Jader Barbalho - PMDB/PA", "35"));
        array.add(new Senador("João Alberto Souza - PMDB/MA", "950"));
        array.add(new Senador("João Capiberibe - PSB/AP", "3394"));
        array.add(new Senador("Jorge Viana - PT/AC", "4990"));
        array.add(new Senador("José Agripino - DEM/RN", "40"));
        array.add(new Senador("José Aníbal - PSDB/SP", "878"));
        array.add(new Senador("José Maranhão - PMDB/PB", "3361"));
        array.add(new Senador("José Medeiros - PSD/MT", "5112"));
        array.add(new Senador("José Pimentel - PT/CE", "615"));
        array.add(new Senador("Kátia Abreu - PMDB/TO", "1249"));
        array.add(new Senador("Lasier Martins - PDT/RS", "5533"));
        array.add(new Senador("Lídice da Mata - PSB/BA", "4575"));
        array.add(new Senador("Lindbergh Farias - PT/RJ", "3695"));
        array.add(new Senador("Lúcia Vânia - PSB/GO", "643"));
        array.add(new Senador("Magno Malta - PR/ES", "631"));
        array.add(new Senador("Maria do Carmo Alves - DEM/SE", "1023"));
        array.add(new Senador("Marta Suplicy - PMDB/SP", "5000"));
        array.add(new Senador("Omar Aziz - PSD/AM", "5525"));
        array.add(new Senador("Otto Alencar - PSD/BA", "5523"));
        array.add(new Senador("Pastor Valadares - PDT/RO", "5617"));
        array.add(new Senador("Paulo Bauer - PSDB/SC", "3741"));
        array.add(new Senador("Paulo Paim - PT/RS", "825"));
        array.add(new Senador("Paulo Rocha - PT/PA", "374"));
        array.add(new Senador("Pedro Chaves - PSC/MS", "5116"));
        array.add(new Senador("Raimundo Lira - PMDB/PB", "2207"));
        array.add(new Senador("Randolfe Rodrigues - REDE/AP", "5012"));
        array.add(new Senador("Regina Sousa - PT/PI", "5182"));
        array.add(new Senador("Reguffe - S/Partido/DF", "5236"));
        array.add(new Senador("Renan Calheiros - PMDB/AL", "70"));
        array.add(new Senador("Ricardo Ferraço - PSDB/ES", "635"));
        array.add(new Senador("Roberto Muniz - PP/BA", "5052"));
        array.add(new Senador("Roberto Requião - PMDB/PR", "72"));
        array.add(new Senador("Roberto Rocha - PSB/MA", "677"));
        array.add(new Senador("Romário - PSB/RJ", "5322"));
        array.add(new Senador("Romero Jucá - PMDB/RR", "73"));
        array.add(new Senador("Ronaldo Caiado - DEM/GO", "456"));
        array.add(new Senador("Rose de Freitas - PMDB/ES", "2331"));
        array.add(new Senador("Sérgio Petecão - PSD/AC", "4560"));
        array.add(new Senador("Simone Tebet - PMDB/MS", "5527"));
        array.add(new Senador("Tasso Jereissati - PSDB/CE", "3396"));
        array.add(new Senador("Telmário Mota - PDT/RR", "5535"));
        array.add(new Senador("Valdir Raupp - PMDB/RO", "3372"));
        array.add(new Senador("Vanessa Grazziotin - PCdoB/AM", "558"));
        array.add(new Senador("Vicentinho Alves - PR/TO", "4763"));
        array.add(new Senador("Waldemir Moka - PMDB/MS", "1176"));
        array.add(new Senador("Wellington Fagundes - PR/MT", "1173"));
        array.add(new Senador("Wilder Morais - PP/GO", "5070"));
        array.add(new Senador("Zeze Perrella - PTB/MG", "5144"));

        for (int i = 0; i < array.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(this.sen_lista_col_nome, array.get(i).nome);
            values.put(this.sen_lista_col_numero, array.get(i).numero);

            try {
                this.banco.insertOrThrow(this.table_name_senador_lista, null, values);
            } catch (SQLException e) {
                Session.createLog(SqliteManager.class.getName(), "insertSENADOR: |" + array.get(i).nome + "| já existe", null);
            }
        }
    }

    public ArrayList<String> selectSenadoresGetNome() {
        Cursor cursor = this.banco.query(
                this.table_name_senador_lista, new String[]{this.sen_lista_col_nome}, null, null, null, null, null
        );
        if (cursor.moveToFirst()) {

            ArrayList<String> array = new ArrayList<String>();
            do {
                String nome = cursor.getString(cursor.getColumnIndex(this.sen_lista_col_nome));

                array.add(nome);

            } while (cursor.moveToNext());

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return array;
        }
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return new ArrayList<String>();
    }

    public Senador selectSenadorById(int id) {
        Cursor cursor = this.banco.query(
                this.table_name_senador_lista, null, sen_lista_col_id + "= ? ", new String[]{String.valueOf(id)}, null, null, null
        );

        if (cursor.moveToFirst()) {
            Senador sen = new Senador();
            sen.nome = cursor.getString(cursor.getColumnIndex(this.sen_lista_col_nome));
            sen.numero = cursor.getString(cursor.getColumnIndex(this.sen_lista_col_numero));
            sen.id = cursor.getInt(cursor.getColumnIndex(this.sen_lista_col_id));
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return sen;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return new Senador();
        }
    }

    /**
     * @param est
     * @return true se inseriu com sucesso, false se não inseriu
     */
    //Log
    /*public boolean insertUsuario(Estudante est) {
        //Mapa com a coluna e os valores de cada coluna.
        ContentValues values = new ContentValues();
        values.put(this.user_col_login, est.login);
        values.put(this.user_col_senha, est.senha);
        values.put(this.user_col_hasdata, est.hasdata);
        values.put(this.user_col_tentativas_login, 0);

        long retorno;
        try {
            retorno = this.banco.insertOrThrow(this.table_name_user, null, values);
        } catch (Exception e) {
            retorno = 0;
        }

        if (retorno != 0 && retorno != -1) {
            if (Session.DEBUG)
                Log.v(this.getClass().getName(), "insertUsuario(): " + "Login=" + est.login + " | Senha= " + est.senha + " | Id= " + retorno);
            return true;
        } else
            return false;
    }

    public boolean insertPeriodoComMaterias(Estudante est, Periodo per) {
        ContentValues values = new ContentValues();
        values.put(this.periodo_col_id_user, est.id);
        values.put(this.periodo_col_descricao, per.descricao);
        values.put(this.periodo_col_peso, per.peso);

        int retorno;
        try {
            retorno = (int) this.banco.insertOrThrow(this.table_name_periodo, null, values);
        } catch (Exception e) {
            retorno = 0;
        }

        if (retorno != 0 && retorno != -1) {
            per.id = String.valueOf(retorno);

            for (int i = 0; i < per.materias.size(); i++) {
                boolean inseriu = insertMateria(per, per.materias.get(i));
                if (!inseriu)
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    //Log
    public boolean insertMateria(Periodo per, Materia mat) {
        //Mapa com a coluna e os valores de cada coluna.
        ContentValues values = new ContentValues();
        values.put(this.materia_col_id_periodo, per.id);
        values.put(this.materia_col_peso, mat.peso);
        values.put(this.materia_col_nome, mat.nome);
        values.put(this.materia_col_professor, mat.professor);
        values.put(this.materia_col_faltas, mat.faltas);
        values.put(this.materia_col_atualizacao, mat.valor);
        values.put(this.materia_col_va1, mat.notas.get(0));
        values.put(this.materia_col_va2, mat.notas.get(1));
        values.put(this.materia_col_va3, mat.notas.get(2));
        values.put(this.materia_col_media, mat.notas.get(3));
        values.put(this.materia_col_vafn, mat.notas.get(4));
        values.put(this.materia_col_mfin, mat.notas.get(5));
        values.put(this.materia_col_situacao, mat.situacao);
        values.put(this.materia_col_avalnormal, mat.avaliacao.get(0));
        values.put(this.materia_col_avalfinal, mat.avaliacao.get(1));

        //Executa um insert no banco de dados usando o mapa de valores.
        //Retorna -1 caso ocorra algum erro no INSERT.
        int retorno;
        try {
            retorno = (int) this.banco.insertOrThrow(this.table_name_materia, null, values);
        } catch (Exception e) {
            retorno = 0;
        }

        if (retorno != 0 && retorno != -1) {
            if (Session.DEBUG)
                Log.v(this.getClass().getName(), "insertMateria(): " + "Nome=" + mat.nome + " | Per= " + per.id + " | Id= " + retorno);
            return true;
        } else
            return false;
    }

    public Materia selectMateria(String idPer, String matNome) {
        Cursor cursor = this.banco.query(
                this.table_name_materia, null, materia_col_id_periodo + "=? AND " + materia_col_nome + "=?", new String[]{idPer, matNome}, null, null, null
        );

        if (cursor.moveToFirst()) {
            Materia mat = new Materia();
            mat.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.materia_col_id)));
            mat.id_periodo = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.materia_col_id_periodo)));
            mat.peso = cursor.getString(cursor.getColumnIndex(this.materia_col_peso));
            mat.nome = cursor.getString(cursor.getColumnIndex(this.materia_col_nome));
            mat.professor = cursor.getString(cursor.getColumnIndex(this.materia_col_professor));
            mat.faltas = cursor.getString(cursor.getColumnIndex(this.materia_col_faltas));
            mat.valor = cursor.getString(cursor.getColumnIndex(this.materia_col_atualizacao));
            mat.situacao = cursor.getString(cursor.getColumnIndex(this.materia_col_situacao));

            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va1)));
            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va2)));
            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va3)));
            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_media)));
            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_vafn)));
            mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_mfin)));

            mat.avaliacao.add(cursor.getString(cursor.getColumnIndex(this.materia_col_avalnormal)));
            mat.avaliacao.add(cursor.getString(cursor.getColumnIndex(this.materia_col_avalfinal)));

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return mat;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return null;
        }
    }

    public ArrayList<Materia> selectAllMateriaByIdPeriodo(String idPer) {
        Cursor cursor = this.banco.query(
                this.table_name_materia, null, materia_col_id_periodo + " = ?", new String[]{idPer}, null, null, null
        );

        if (cursor.moveToFirst()) {
            ArrayList<Materia> materiaArray = new ArrayList<>();
            do {
                Materia mat = new Materia();
                mat.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.materia_col_id)));
                mat.id_periodo = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.materia_col_id_periodo)));
                mat.peso = cursor.getString(cursor.getColumnIndex(this.materia_col_peso));
                mat.nome = cursor.getString(cursor.getColumnIndex(this.materia_col_nome));
                mat.professor = cursor.getString(cursor.getColumnIndex(this.materia_col_professor));
                mat.faltas = cursor.getString(cursor.getColumnIndex(this.materia_col_faltas));
                mat.valor = cursor.getString(cursor.getColumnIndex(this.materia_col_atualizacao));
                mat.situacao = cursor.getString(cursor.getColumnIndex(this.materia_col_situacao));

                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va1)));
                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va2)));
                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_va3)));
                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_media)));
                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_vafn)));
                mat.notas.add(cursor.getString(cursor.getColumnIndex(this.materia_col_mfin)));

                mat.avaliacao.add(cursor.getString(cursor.getColumnIndex(this.materia_col_avalnormal)));
                mat.avaliacao.add(cursor.getString(cursor.getColumnIndex(this.materia_col_avalfinal)));

                materiaArray.add(mat);

            } while (cursor.moveToNext());

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return materiaArray;
        }
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    public Periodo selectPeriodoByName(String idUser, String descPer) {
        Cursor cursor = this.banco.query(
                this.table_name_periodo, null, periodo_col_id_user + "=? AND " + periodo_col_descricao + "=?", new String[]{idUser, descPer}, null, null, null
        );

        if (cursor.moveToFirst()) {
            Periodo per = new Periodo();
            per.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.periodo_col_id)));
            per.id_user = String.valueOf(cursor.getString(cursor.getColumnIndex(this.periodo_col_id_user)));
            per.descricao = cursor.getString(cursor.getColumnIndex(this.periodo_col_descricao));
            per.peso = cursor.getString(cursor.getColumnIndex(this.periodo_col_peso));

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return per;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return null;
        }
    }

    public ArrayList<Periodo> selectAllPeriodoByUserId(String idUser) {
        Cursor cursor = this.banco.query(
                this.table_name_periodo, null, periodo_col_id_user + " = ?", new String[]{idUser}, null, null, null
        );

        if (cursor.moveToFirst()) {
            ArrayList<Periodo> periodosArray = new ArrayList<>();
            do {
                Periodo per = new Periodo();
                per.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.periodo_col_id)));
                per.id_user = String.valueOf(cursor.getString(cursor.getColumnIndex(this.periodo_col_id_user)));
                per.descricao = cursor.getString(cursor.getColumnIndex(this.periodo_col_descricao));
                per.peso = cursor.getString(cursor.getColumnIndex(this.periodo_col_peso));
                per.materias = selectAllMateriaByIdPeriodo(per.id);
                periodosArray.add(per);

            } while (cursor.moveToNext());

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return periodosArray;
        }
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    //Log
    public Estudante selectUsuario(String login) {
        Cursor cursor = this.banco.query(
                this.table_name_user, null, user_col_login + " = ?", new String[]{login}, null, null, null
        );

        if (cursor.moveToFirst()) {
            Estudante est = new Estudante();
            est.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.user_col_id)));
            est.login = cursor.getString(cursor.getColumnIndex(this.user_col_login));
            est.senha = cursor.getString(cursor.getColumnIndex(this.user_col_senha));
            est.nome = cursor.getString(cursor.getColumnIndex(this.user_col_nome));
            est.cpf = cursor.getString(cursor.getColumnIndex(this.user_col_cpf));
            est.curso = cursor.getString(cursor.getColumnIndex(this.user_col_curso));
            est.hasdata = cursor.getString(cursor.getColumnIndex(this.user_col_hasdata));
            if (Session.DEBUG)
                Log.v(this.getClass().getName(),
                        "selectUsuario(): " +
                                "Login=" + est.login +
                                " | Senha= " + est.senha +
                                " | cpf= " + est.cpf +
                                " | Id= " + est.id);
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return est;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return null;
        }
    }

    //Log
    public boolean updateUsuarioInfo(Estudante est) {
        ContentValues values = new ContentValues();
        values.put(this.user_col_nome, est.nome);
        values.put(this.user_col_cpf, est.cpf);
        values.put(this.user_col_curso, est.curso);

        String where = this.user_col_login + " = ?";

        int retorno = this.banco.update(table_name_user, values, where, new String[]{est.login});

        if (retorno != 0 && retorno != -1) {

            if (Session.DEBUG)
                Log.v(this.getClass().getName(),
                        "updateUsuarioInfo(): " +
                                "Login=" + est.login +
                                " | Senha= " + est.senha +
                                " | Nome= " + est.nome +
                                " | cpf= " + est.cpf +
                                " | curso= " + est.curso +
                                " | Id= " + retorno);
            return true;
        } else
            return false;
    }

    //Log
    public boolean updateUsuario(String currentLogin, Estudante est) {
        ContentValues values = new ContentValues();
        values.put(this.user_col_login, est.login);
        values.put(this.user_col_senha, est.senha);

        int retorno = this.banco.update(table_name_user, values, this.user_col_login + " = ?", new String[]{currentLogin});

        if (retorno != 0 && retorno != -1) {
            if (Session.DEBUG)
                Log.v(this.getClass().getName(), "updateUsuario(): " + "Login=" + est.login + " | Senha= " + est.senha + " | Id= " + retorno);
            return true;
        } else
            return false;
    }

    public boolean updateHasData(Estudante est) {
        ContentValues values = new ContentValues();
        values.put(this.user_col_hasdata, est.hasdata);

        String where = this.user_col_login + " = ?";

        int retorno = this.banco.update(table_name_user, values, where, new String[]{est.login});

        if (retorno != 0 && retorno != -1)
            return true;
        else
            return false;
    }

    public boolean updateMateria(Estudante est, Periodo per, Materia mat) {
        String id = String.valueOf(getIdEstudanteByEstudante(est));
        Cursor cursor = this.banco.query(
                this.table_name_periodo, new String[]{this.periodo_col_id},
                this.periodo_col_id_user + " = ? AND " + this.periodo_col_descricao + " = ?", new String[]{id, per.descricao}, null, null, null
        );
        cursor.moveToFirst();
        id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.periodo_col_id)));

        //Mapa com a coluna e os valores de cada coluna.
        ContentValues values = new ContentValues();
        values.put(this.materia_col_id_periodo, id);
        values.put(this.materia_col_peso, mat.peso);
        values.put(this.materia_col_nome, mat.nome);
        values.put(this.materia_col_professor, mat.professor);
        values.put(this.materia_col_faltas, mat.faltas);
        values.put(this.materia_col_atualizacao, mat.valor);
        values.put(this.materia_col_va1, mat.notas.get(0));
        values.put(this.materia_col_va2, mat.notas.get(1));
        values.put(this.materia_col_va3, mat.notas.get(2));
        values.put(this.materia_col_media, mat.notas.get(3));
        values.put(this.materia_col_vafn, mat.notas.get(4));
        values.put(this.materia_col_mfin, mat.notas.get(5));
        values.put(this.materia_col_situacao, mat.situacao);
        values.put(this.materia_col_avalnormal, mat.avaliacao.get(0));
        values.put(this.materia_col_avalfinal, mat.avaliacao.get(1));

        String where = this.materia_col_id_periodo + " = ? AND " + this.materia_col_nome + " = ?";

        int retorno = this.banco.update(table_name_materia, values, where, new String[]{id, mat.nome});

        try {
            cursor.close();
        } catch (Exception e) {
        }

        if (retorno != 0 && retorno != -1)
            return true;
        else
            return false;
    }

    //Log
    public Estudante getEstudanteFilled(String login) {
        Estudante oldEst = selectUsuario(login);
        if (oldEst != null) {
            if (Session.DEBUG)
                Log.v(this.getClass().getName(), "getEstudanteFilled(): " + "Login=" + oldEst.login + " | Senha= " + oldEst.senha + " | Nome= " + oldEst.nome + " | Id= " + oldEst.id);


            oldEst.periodos = selectAllPeriodoByUserId(oldEst.id);
            if (oldEst.periodos != null && oldEst.periodos.size() > 0) {
                oldEst.ordenarPorPesoCrescente();
            }

            return oldEst;
        }
        if (Session.DEBUG)
            Log.v(this.getClass().getName(), "getEstudanteFilled(): retornando NULL");
        return null;
    }

    //Log
    public int getIdEstudanteByEstudante(Estudante est) {
        Cursor cursor = this.banco.query(
                this.table_name_user, new String[]{this.user_col_id}, user_col_login + " = ?", new String[]{est.login}, null, null, null
        );
        cursor.moveToFirst();
        if (Session.DEBUG)
            Log.v(this.getClass().getName(),
                    "getIdEstudanteByEstudante(): " +
                            "Login=" + est.login +
                            " | Senha= " + est.senha +
                            " | Nome= " + est.nome +
                            " | cpf= " + est.cpf +
                            " | curso= " + est.curso +
                            " | Id= " + cursor.getInt(cursor.getColumnIndex(this.user_col_id)));

        int vr = cursor.getInt(cursor.getColumnIndex(this.user_col_id));
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return vr;
    }

    //Log
    public boolean insertEstudanteFirstTime(Estudante est) {
        if (!updateUsuarioInfo(est))
            return false;
        int id = getIdEstudanteByEstudante(est);
        est.id = String.valueOf(id);

        if (Session.DEBUG)
            Log.v(this.getClass().getName(), "insertEstudanteFirstTime(): " + "Login=" + est.login + " | Nome= " + est.nome + " | Id= " + est.id);

        if (!est.id.equals("-1")) {
            for (int i = 0; i < est.periodos.size(); i++) {
                if (!insertPeriodoComMaterias(est, est.periodos.get(i))) {
                    return false;
                }
            }
            if (updateHasData(est)) {
                return true;
            } else {
                return false;
            }


        }
        return false;
    }

    //Log
    public ArrayList<Estudante> selectAllUsuarios() {
        Cursor cursor = this.banco.query(
                this.table_name_user, null, null, null, null, null, null
        );
        if (cursor.moveToFirst()) {
            ArrayList<Estudante> estudantesArray = new ArrayList<>();
            do {
                Estudante est = new Estudante();
                est.id = String.valueOf(cursor.getInt(cursor.getColumnIndex(this.user_col_id)));
                est.login = cursor.getString(cursor.getColumnIndex(this.user_col_login));
                est.senha = cursor.getString(cursor.getColumnIndex(this.user_col_senha));
                est.nome = cursor.getString(cursor.getColumnIndex(this.user_col_nome));
                est.cpf = cursor.getString(cursor.getColumnIndex(this.user_col_cpf));
                est.curso = cursor.getString(cursor.getColumnIndex(this.user_col_curso));
                est.hasdata = cursor.getString(cursor.getColumnIndex(this.user_col_hasdata));

                if (Session.DEBUG)
                    Log.v(this.getClass().getName(),
                            "selectAllUsuarios(): " +
                                    "Nome=" + est.nome +
                                    " | Curso=" + est.curso +
                                    " | Login=" + est.login +
                                    " | Senha= " + est.senha +
                                    " | cpf= " + est.cpf +
                                    " | Id= " + est.id);

                estudantesArray.add(est);

            } while (cursor.moveToNext());

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return estudantesArray;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return null;
        }
    }

    public void dropAllDataUsuario(String login) {
        int id = getIdEstudanteByLogin(login);
        ArrayList<Integer> perId = getIdPeriodosByIdUsuario(id);
        if (perId != null) {
            for (int i = 0; i < perId.size(); i++) {
                removeMateriasByIdPeriodo(perId.get(i));
            }
            removePeriodoByIdUsuario(id);
        }
        removeUsuarioById(id);

    }

    public int getIdEstudanteByLogin(String login) {
        Cursor cursor = this.banco.query(
                this.table_name_user, new String[]{this.user_col_id}, user_col_login + " = ?", new String[]{login}, null, null, null
        );
        cursor.moveToFirst();
        if (Session.DEBUG)
            Log.v(this.getClass().getName(),
                    "getIdEstudanteByLogin(): " +
                            "Id= " + cursor.getInt(cursor.getColumnIndex(this.user_col_id)));
        int vr = cursor.getInt(cursor.getColumnIndex(this.user_col_id));
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return vr;
    }

    public boolean getUserLastAttemptWasFailedByLogin(String login) {
        Cursor cursor = this.banco.query(
                this.table_name_user, null, user_col_login + " = ?", new String[]{login}, null, null, null
        );
        cursor.moveToFirst();
        if (Session.DEBUG)
            Log.v(this.getClass().getName(),
                    "getIdEstudanteByLogin(): " + "login: " + login + " ," +
                            "tentativas was success = " + cursor.getInt(cursor.getColumnIndex(this.user_col_tentativas_login)));
        int bool = cursor.getInt(cursor.getColumnIndex(this.user_col_tentativas_login));
        if (bool == 0)
            return false;
        else
            return true;
    }

    public boolean setUserLastAttemptWasFailedByLogin(String login, int key) {
        ContentValues values = new ContentValues();
        String where = this.user_col_login + " = ?";
        values.put(this.user_col_tentativas_login, key);
        int retorno = this.banco.update(table_name_user, values, where, new String[]{login});

        if (retorno != 0 && retorno != -1)
            return true;
        else
            return false;
    }

    public String getSenhaEstudanteByLogin(String login) {
        Cursor cursor = this.banco.query(
                this.table_name_user, new String[]{this.user_col_senha}, user_col_login + " = ?", new String[]{login}, null, null, null
        );
        cursor.moveToFirst();
        if (Session.DEBUG)
            Log.v(this.getClass().getName(),
                    "getSenhaEstudanteByLogin(): " +
                            "senha= " + cursor.getString(cursor.getColumnIndex(this.user_col_senha)));
        String vr = cursor.getString(cursor.getColumnIndex(this.user_col_senha));
        try {
            cursor.close();
        } catch (Exception e) {
        }
        return vr;
    }

    public ArrayList<Integer> getIdPeriodosByIdUsuario(int id) {
        Cursor cursor = this.banco.query(
                this.table_name_periodo, new String[]{this.periodo_col_id}, periodo_col_id_user + " = ?", new String[]{String.valueOf(id)}, null, null, null
        );

        if (cursor.moveToFirst()) {
            ArrayList<Integer> periodosIdArray = new ArrayList<>();
            do {
                periodosIdArray.add(cursor.getInt(cursor.getColumnIndex(this.periodo_col_id)));

            } while (cursor.moveToNext());

            try {
                cursor.close();
            } catch (Exception e) {
            }
            return periodosIdArray;
        } else {
            try {
                cursor.close();
            } catch (Exception e) {
            }
            return null;
        }

    }

    public void removeMateriasByIdPeriodo(int id) {
        this.banco.delete(table_name_materia, materia_col_id_periodo + " = ?", new String[]{String.valueOf(id)});
    }

    public void removePeriodoByIdUsuario(int id) {
        this.banco.delete(table_name_periodo, periodo_col_id_user + " = ?", new String[]{String.valueOf(id)});
    }

    public void removeUsuarioById(int id) {
        this.banco.delete(table_name_user, user_col_id + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean deleteDataBase() {
        boolean tentativa;
        try {
            tentativa = this._context.deleteDatabase(DATABASE_NAME);
        } catch (Exception e) {
            tentativa = false;
        }
        return tentativa;
    }

    public boolean deleteTablesLessUsers() {
        boolean tentativa;
        try {
            this.banco.execSQL("DROP TABLE IF EXISTS " + table_name_materia);
            this.banco.execSQL("DROP TABLE IF EXISTS " + table_name_periodo);
            tentativa = true;
        } catch (Exception e) {
            tentativa = false;
        }
        return tentativa;
    }*/
}
