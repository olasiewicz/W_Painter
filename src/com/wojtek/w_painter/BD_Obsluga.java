package com.wojtek.w_painter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

public class BD_Obsluga {

	Context context;
	public SQLiteDatabase baza;
	private String sciezkaBaza;
	private String nazwaP = "";
	private int int_id = 0;
	ContentValues rekord;
	FileOutputStream outStream;

	// first constructor
	public BD_Obsluga(StartActivity aktywnosc) {
		this.context = aktywnosc;
	}

	// second constructor
	public BD_Obsluga() {

	}

	// create the folder PainterW and demo file inside
	public void utworzDemoPlik() {
		Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.admin);

		File plik = Environment.getExternalStorageDirectory();

		File picsDir = new File(plik, "PainterW");
		picsDir.mkdirs(); // make if not exist

		File plik1 = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/PainterW/" + "1.jpg");

		try {
			outStream = new FileOutputStream(plik1);

			bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// create database file in folder PanterW
	public void utworzBD() {
		sciezkaBaza = Environment.getExternalStorageDirectory().getPath()
				+ "/PainterW/" + BD_Struktura.BD_plikBD;
		baza = context.openOrCreateDatabase(sciezkaBaza,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		baza.execSQL(BD_Struktura.SQLTabelaPaint);

		String[] kolumna = BD_Struktura.BD_Paint_All_Files;
		Cursor k_kolumna;
		k_kolumna = baza.query(BD_Struktura.BD_Tabela_Paint, kolumna, null,
				null, null, null, null);

		// if database is empty - create first record which name is the same
		// like demo file
		if (k_kolumna.moveToLast() == false) {

			rekord = new ContentValues();
			rekord.put(BD_Struktura.BD_Paint_Nazwa, "1");
			baza.insert(BD_Struktura.BD_Tabela_Paint, null, rekord);
		}

		baza.close();
	}

	// add new record to db which name is next unique integer
	public void dodajRecord() {
		try {
			sciezkaBaza = Environment.getExternalStorageDirectory().getPath()
					+ "/PainterW/" + BD_Struktura.BD_plikBD;
			baza = context.openOrCreateDatabase(sciezkaBaza,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			ContentValues rekord = new ContentValues();
			rekord.put(BD_Struktura.BD_Paint_Nazwa, nazwaP);
			baza.insert(BD_Struktura.BD_Tabela_Paint, null, rekord);
		} catch (NullPointerException e) {

			rekord = new ContentValues();
			rekord.put(BD_Struktura.BD_Paint_Nazwa, "1");
			baza.insert(BD_Struktura.BD_Tabela_Paint, null, rekord);
		}
		baza.close();
	}

	// generate unique name
	public void pobierzID() {
		sciezkaBaza = Environment.getExternalStorageDirectory().getPath()
				+ "/PainterW/" + BD_Struktura.BD_plikBD;
		baza = context.openOrCreateDatabase(sciezkaBaza,
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		String[] kolumna = BD_Struktura.BD_Paint_All_Files;
		Cursor k_kolumna;
		k_kolumna = baza.query(BD_Struktura.BD_Tabela_Paint, kolumna, null,
				null, null, null, null);

		try {
			k_kolumna.moveToLast();

			int_id = Integer.valueOf(k_kolumna.getString(0));

			int_id += 1;

			nazwaP = String.valueOf(int_id);
			Toast.makeText(context, nazwaP, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {

			Toast.makeText(context, R.string.bd_Obsluga_failedToLoadID,
					Toast.LENGTH_SHORT).show();

			e.printStackTrace();
		}

		baza.close();

	}

	// create new file in PainterW folder - which the same name like record in
	// db
	public void utworzPlik(Bitmap bitmapa, View zawartosc) {

		File plik = Environment.getExternalStorageDirectory();
		File picsDir = new File(plik, "PainterW");
		picsDir.mkdirs(); // make if not exist

		File plik1 = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/PainterW/" + nazwaP + ".jpg");

		try {
			plik1.createNewFile();

			FileOutputStream strumien = new FileOutputStream(plik1);
			bitmapa.compress(CompressFormat.JPEG, 100, strumien);
			strumien.close();
		} catch (IOException e) {

			e.printStackTrace();
			Toast.makeText(context, R.string.bd_Obsluga_failedToCreateFile,
					Toast.LENGTH_SHORT).show();
		}

	}

}
