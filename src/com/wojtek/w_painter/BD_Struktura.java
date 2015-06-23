package com.wojtek.w_painter;


public class BD_Struktura {
	
public static final String BD_plikBD = "paint.db";

public static final String BD_Tabela_Paint = "paintTable";
public static final String BD_Paint_id = " id";
public static final String BD_Paint_Nazwa  = "nazwa";

public static final String BD_Paint_All_Files[] = {BD_Paint_Nazwa}; 


//create table
public static final String SQLTabelaPaint = "CREATE TABLE IF NOT EXISTS "
		+ BD_Tabela_Paint
		+ "("
		//+ BD_Paint_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
		+ BD_Paint_Nazwa + " STRING "
		+ ")";
	
}
