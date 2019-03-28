package dev.maturano.agenda.constant;

public class DatabaseConstants {
    public static final String DATABASE_NAME = "AgendaComSQLite";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_CONTACT = "contato";
    public static final String CREATE_TABLE_CONTACT =
            "CREATE TABLE contato (id INTEGER PRIMARY KEY, nome TEXT, email TEXT, telefone TEXT);";
}
