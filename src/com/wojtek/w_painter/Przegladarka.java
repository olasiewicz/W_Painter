package com.wojtek.w_painter;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Przegladarka extends Activity {

	Bitmap bitmap;
	SQLiteDatabase baza;
	String plik;
	// class Cursor help get the dates from database
	Cursor k_kolumny;
	ImageView imageView_Podglad;
	int ile = 0;
	int i = 0;
	String string_nazwaGrafiki;
	FileReader reader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_przegladarka);

		imageView_Podglad = (ImageView) findViewById(R.id.imageViewPodglad);

		// in this file is saved patch to directory which contain database
		plik = Environment.getExternalStorageDirectory().getPath()
				+ "/PainterW/" + "paint.db";
		// database is opened
		baza = openOrCreateDatabase(plik, SQLiteDatabase.CREATE_IF_NECESSARY,
				null);
		String kolumny[] = { "nazwa" };

		k_kolumny = baza.query("paintTable", kolumny, null, null, null, null,
				null);

		ile = k_kolumny.getCount();
		k_kolumny.moveToFirst();
		baza.close();

		bitmap = loadFoto(k_kolumny.getString(0));
		imageView_Podglad.setImageBitmap(bitmap);

		i = k_kolumny.getPosition();

	}

	public Bitmap loadFoto(String nazwa_pliku) {
		string_nazwaGrafiki = Environment.getExternalStorageDirectory()
				.getPath() + "/PainterW/" + nazwa_pliku + ".jpg";
		bitmap = BitmapFactory.decodeFile(string_nazwaGrafiki, null);
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browser_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// after clicked to this action user can show the previous item
		case R.id.action_previous:

			try {
				i = k_kolumny.getPosition();

				// when another previous item in not available - the cursor is
				// set to the last item
				if (i == 0) {
					k_kolumny.moveToLast();
					bitmap = loadFoto(k_kolumny.getString(0));

					imageView_Podglad.setImageBitmap(bitmap);

					i = k_kolumny.getPosition();
				}

				else {

					k_kolumny.moveToPrevious();
					bitmap = loadFoto(k_kolumny.getString(0));

					imageView_Podglad.setImageBitmap(bitmap);

					i = k_kolumny.getPosition();

				}
			} catch (Exception e1) {
				Toast.makeText(getApplicationContext(),
						(R.string.browser_noMoreItem_Toast), Toast.LENGTH_LONG)
						.show();
				e1.printStackTrace();
			}
			return true;

			// after clicked to this action user can show the previous item
		case R.id.action_next:

			try {
				i = k_kolumny.getPosition();

				// when another next item in not available - the cursor is set
				// to the first item
				if (i == ile - 1) {
					k_kolumny.moveToFirst();
					bitmap = loadFoto(k_kolumny.getString(0));

					imageView_Podglad.setImageBitmap(bitmap);

					i = k_kolumny.getPosition();

				}

				else {
					k_kolumny.moveToNext();
					bitmap = loadFoto(k_kolumny.getString(0));

					imageView_Podglad.setImageBitmap(bitmap);

					i = k_kolumny.getPosition();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						(R.string.browser_noMoreItem_Toast), Toast.LENGTH_LONG)
						.show();
				e.printStackTrace();

			}
			return true;

			// after clicked to delete action and confirm it in dialog window -
			// the selected item is removed from database and directory
		case R.id.action_delete:

			baza = openOrCreateDatabase(plik,
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			int wartosc = Integer.valueOf(k_kolumny.getString(0));
			if (wartosc == 1) {
				Toast.makeText(getApplicationContext(),
						R.string.browser_cannot_delete_Toast,
						Toast.LENGTH_SHORT).show();
			} else {
				AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
				saveDialog.setTitle(R.string.browser_dialog_delete_title);
				saveDialog.setMessage(R.string.browser_dialog_delete_message);
				saveDialog.setPositiveButton(R.string.dialog_Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								baza.delete("paintTable", "nazwa = "
										+ k_kolumny.getString(0), null);
								File file = new File(string_nazwaGrafiki);
								file.delete();
								Intent intent = new Intent(Przegladarka.this,
										Przegladarka.class);
								startActivity(intent);

								finish();
								baza.close();
							}

						});
				saveDialog.setNegativeButton(R.string.dialog_Cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				saveDialog.show();

			}

			return true;

			// after clicked to this action - using class Intent are called all
			// external applications which can perform send action
		case R.id.action_send:

			int number = i + 1;
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.putExtra("sms_body", "Hi how are you");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory().getPath()
							+ "/PainterW/" + number + ".jpg")));
			intent.setType("image/gif");
			startActivity(Intent.createChooser(intent, "Send"));
			return true;

		case R.id.action_back:
			Intent intentSA = new Intent(Przegladarka.this, StartActivity.class);
			startActivity(intentSA);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
