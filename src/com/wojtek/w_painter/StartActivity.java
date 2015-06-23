package com.wojtek.w_painter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.Browser;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {

	private PainterView painterView;
	private ImageButton currPaintIB;
	private ImageButton backgroundRedIB;
	private ImageButton backgroundBlueIB;
	private ImageButton backgroundWhiteIB;

	private TextView drawTV;
	private TextView eraseTV;
	private TextView styleTV;
	private LinearLayout paintLayout;

	private Style style;
	private Style memoryStyle;

	private boolean booleanDraw_ss;
	private boolean booleanDraw_ms;
	private boolean booleanErase_ss;
	private boolean booleanErase_ms;
	private boolean booleanStyle_s1;
	private boolean booleanStyle_s2;
	private boolean booleanEraserActive;
	private boolean booleanPaintClicked;
	private boolean booleanGradientClicked;

	private BD_Obsluga obslugaBD;
	private Bitmap bitmapa;

	private int lastBrushSize;
	private String lastBrushCount;

	private int lastEraserSize;
	private String lastEraserCount;

	private int eraserColor;
	private String colorString = "0x000000";

	private int intGradientColor1;
	private int intGradientColor2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		painterView = (PainterView) findViewById(R.id.paintingView);
		paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaintIB = (ImageButton) paintLayout.getChildAt(0);
		currPaintIB.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));
		drawTV = (TextView) findViewById(R.id.draw_tv);
		eraseTV = (TextView) findViewById(R.id.erase_tv);
		styleTV = (TextView) findViewById(R.id.style_tv);

		obslugaBD = new BD_Obsluga(this);
		obslugaBD.utworzBD();
		obslugaBD.utworzDemoPlik();

		booleanDraw_ss = false;
		booleanDraw_ms = true;
		booleanErase_ss = false;
		booleanErase_ms = true;
		booleanStyle_s1 = true;
		booleanStyle_s2 = false;
		booleanEraserActive = false;
		booleanPaintClicked = false;
		booleanGradientClicked = false;

		drawTV.setTextColor(Color.RED);
		drawTV.setText("*");
		eraseTV.setTextColor(Color.BLACK);
		eraseTV.setText("*");
		styleTV.setTextColor(Color.RED);
		styleTV.setText("*");
		memoryStyle = style.STROKE;

		lastEraserSize = getResources().getInteger(R.integer.small_size);
		lastEraserCount = "*";
		lastBrushSize = getResources().getInteger(R.integer.small_size);
		lastBrushCount = "*";
		painterView.setBackgroundColor(Color.BLUE);
		eraserColor = Color.BLUE;
		painterView.setColor(Color.BLACK);

		// the backgroud color can be choosen by user
		final Dialog backgroundDialog = new Dialog(this);
		backgroundDialog.setContentView(R.layout.background_chooser);

		backgroundRedIB = (ImageButton) backgroundDialog
				.findViewById(R.id.back_red);
		backgroundBlueIB = (ImageButton) backgroundDialog
				.findViewById(R.id.back_blue);
		backgroundWhiteIB = (ImageButton) backgroundDialog
				.findViewById(R.id.back_white);

		backgroundDialog
				.setTitle(R.string.startActivity_backgroundDialog_title);

		backgroundDialog.show();

		// after click this button background and eraser color is changed to red
		backgroundRedIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				painterView.setBackgroundColor(Color.RED);
				backgroundDialog.dismiss();
				eraserColor = Color.RED;
			}
		});

		// after click this button background and eraser color is changed to
		// blue
		backgroundBlueIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				painterView.setBackgroundColor(Color.BLUE);
				backgroundDialog.dismiss();
				eraserColor = Color.BLUE;
			}
		});

		// after click this button background and eraser color is changed to
		// white
		backgroundWhiteIB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				painterView.setBackgroundColor(Color.WHITE);
				backgroundDialog.dismiss();
				eraserColor = Color.WHITE;
			}
		});

	}

	// this method is initialized in xml file and is call after press one of the
	// paint buttons
	public void paintClicked(View view) {

		painterView.setShader(null);

		// check which button is selected (erase or paint)
		if (booleanEraserActive == false) {

			// check if user clicked new pain button
			if (view != currPaintIB) {

				// update color
				ImageButton imgView = (ImageButton) view;
				colorString = view.getTag().toString();
				painterView.setColor(colorString);
				// update ui
				imgView.setImageDrawable(getResources().getDrawable(
						R.drawable.paint_pressed));
				currPaintIB.setImageDrawable(getResources().getDrawable(
						R.drawable.paint));
				currPaintIB = (ImageButton) view;
				booleanPaintClicked = true;
				booleanGradientClicked = false;
			}
		} else {
			// when erase button is selected
			Toast.makeText(getApplicationContext(),
					R.string.painterViev_selectPainter_Toast,
					Toast.LENGTH_SHORT).show();
		}

	}

	// this method allowed to change brush size, number and color of stars(which
	// represented brush size)
	public void drawClicked(View view) {
		if (booleanEraserActive && booleanGradientClicked) {
			booleanEraserActive = false;

			Shader shader = (new RadialGradient(10, 10, 10, intGradientColor1,
					intGradientColor2, Shader.TileMode.MIRROR));
			painterView.setShader(shader);

			drawTV.setTextColor(Color.RED);
			eraseTV.setTextColor(Color.BLACK);
			painterView.setStyle(memoryStyle);
			painterView.setBrushSize(lastBrushSize);
			drawTV.setText(lastBrushCount);
		} else {

			if (booleanEraserActive) {
				booleanEraserActive = false;
				painterView.setStyle(memoryStyle);
				painterView.setBrushSize(lastBrushSize);
				drawTV.setText(lastBrushCount);
				drawTV.setTextColor(Color.RED);
				eraseTV.setTextColor(Color.BLACK);
				if (!booleanPaintClicked) {
					painterView.setColor(Color.BLACK);

				} else
					painterView.setColor(colorString);

			} else {
				if (booleanDraw_ss) {
					painterView.setBrushSize(getResources().getInteger(
							R.integer.small_size));
					booleanDraw_ss = false;
					booleanDraw_ms = true;
					drawTV.setTextColor(Color.RED);
					drawTV.setText("*");
					eraseTV.setTextColor(Color.BLACK);
					lastBrushSize = getResources().getInteger(
							R.integer.small_size);
					lastBrushCount = "*";
				}

				else if (booleanDraw_ss == false && booleanDraw_ms) {
					painterView.setBrushSize(getResources().getInteger(
							R.integer.medium_size));
					booleanDraw_ss = false;
					booleanDraw_ms = false;
					drawTV.setTextColor(Color.RED);
					drawTV.setText("**");
					eraseTV.setTextColor(Color.BLACK);
					lastBrushSize = getResources().getInteger(
							R.integer.medium_size);
					lastBrushCount = "**";
				}

				else if (booleanDraw_ss == false && booleanDraw_ms == false) {
					painterView.setBrushSize(getResources().getInteger(
							R.integer.large_size));
					booleanDraw_ss = true;
					booleanDraw_ms = false;
					drawTV.setTextColor(Color.RED);
					drawTV.setText("***");
					eraseTV.setTextColor(Color.BLACK);
					lastBrushSize = getResources().getInteger(
							R.integer.large_size);
					lastBrushCount = "***";
				}
			}
			booleanEraserActive = false;
		}
	}

	// this method allowed to change brush style, number and color of
	// stars(which represented brush style)
	public void styleClicked(View view) {
		if (booleanEraserActive) {
			painterView.setStyle(style.STROKE);
		} else {
			if (booleanStyle_s1) {
				booleanStyle_s1 = false;
				booleanStyle_s2 = true;
				painterView.setStyle(style.FILL);
				memoryStyle = style.FILL;
				styleTV.setText("**");
			} else if (booleanStyle_s1 == false && booleanStyle_s2) {
				booleanStyle_s1 = false;
				booleanStyle_s2 = false;
				painterView.setStyle(style.FILL_AND_STROKE);
				memoryStyle = style.FILL_AND_STROKE;
				styleTV.setText("***");
			}

			else if (booleanStyle_s1 == false && booleanStyle_s2 == false) {
				booleanStyle_s1 = true;
				booleanStyle_s2 = false;
				painterView.setStyle(style.STROKE);
				memoryStyle = style.STROKE;
				styleTV.setText("*");
			}
		}
	}

	// in this method user can choose eraser size, number and color of stars
	public void eraseClicked(View view) {

		painterView.setStyle(style.STROKE);
		painterView.setShader(null);

		if (booleanEraserActive == false) {
			booleanEraserActive = true;
			// booleanPaintClicked = false;
			// booleanGradientClicked = false;
			painterView.setBrushSize(lastEraserSize);
			eraseTV.setText(lastEraserCount);
			drawTV.setTextColor(Color.BLACK);
			eraseTV.setTextColor(Color.RED);
			// eraser color is the same like background color
			painterView.setEraserColor(eraserColor);
		} else {

			if (booleanErase_ss) {
				painterView.setBrushSize(getResources().getInteger(
						R.integer.small_size));
				booleanErase_ss = false;
				booleanErase_ms = true;
				eraseTV.setTextColor(Color.RED);
				eraseTV.setText("*");
				drawTV.setTextColor(Color.BLACK);
				lastEraserSize = getResources()
						.getInteger(R.integer.small_size);
				lastEraserCount = "*";
			}

			else if (booleanErase_ss == false && booleanErase_ms) {
				painterView.setBrushSize(getResources().getInteger(
						R.integer.medium_size));
				booleanErase_ss = false;
				booleanErase_ms = false;
				eraseTV.setTextColor(Color.RED);
				eraseTV.setText("**");
				drawTV.setTextColor(Color.BLACK);
				lastEraserSize = getResources().getInteger(
						R.integer.medium_size);
				lastEraserCount = "**";
			}

			else if (booleanErase_ss == false && booleanErase_ms == false) {
				painterView.setBrushSize(getResources().getInteger(
						R.integer.large_size));
				booleanErase_ss = true;
				booleanErase_ms = false;
				eraseTV.setTextColor(Color.RED);
				eraseTV.setText("***");
				drawTV.setTextColor(Color.BLACK);
				lastEraserSize = getResources()
						.getInteger(R.integer.large_size);
				lastEraserCount = "***";
			}
		}

		booleanEraserActive = true;
	}

	// this method is initialized in xml file and is called after selected
	// button which represented gradient
	public void gradientClicked(View view) {

		if (booleanEraserActive == false) {
			booleanGradientClicked = true;
			booleanPaintClicked = false;
			if (view.getId() == R.id.gradient1) {
				drawGradient(view, Color.RED, Color.BLUE);
				intGradientColor1 = Color.RED;
				intGradientColor2 = Color.BLUE;

			}

			if (view.getId() == R.id.gradient2) {
				drawGradient(view, Color.GREEN, Color.YELLOW);
				intGradientColor1 = Color.GREEN;
				intGradientColor2 = Color.YELLOW;

			}

			if (view.getId() == R.id.gradient3) {
				drawGradient(view, Color.MAGENTA, Color.CYAN);
				intGradientColor1 = Color.MAGENTA;
				intGradientColor2 = Color.CYAN;

			}

			if (view.getId() == R.id.gradient4) {
				drawGradient(view, Color.BLACK, Color.RED);
				intGradientColor1 = Color.BLACK;
				intGradientColor2 = Color.RED;

			}
		} else {

			Toast.makeText(getApplicationContext(),
					R.string.painterViev_selectPainter_Toast, Toast.LENGTH_SHORT).show();

		}
	}

	// this method set two colors which represented gradient
	public void drawGradient(View view, int color1, int color2) {

		if (view != currPaintIB) {
			drawTV.setTextColor(Color.RED);
			eraseTV.setTextColor(Color.BLACK);
			Shader shader = (new RadialGradient(10, 10, 10, color1, color2,
					Shader.TileMode.MIRROR));
			painterView.setShader(shader);
			ImageButton imgView = (ImageButton) view;
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaintIB.setImageDrawable(getResources().getDrawable(
					R.drawable.paint));
			currPaintIB = (ImageButton) view;

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		// after choosing new action and confirm it in dialog window - the
		// canvas is cleaned up
		case R.id.action_new:
			AlertDialog.Builder newDialog = new AlertDialog.Builder(
					StartActivity.this);
			newDialog.setTitle(R.string.startActivity_menu_Dialog_New_Title);
			newDialog
					.setMessage(R.string.startActivity_menu_Dialog_New_Message);
			newDialog.setPositiveButton(R.string.dialog_Yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							painterView.startNew();
							dialog.dismiss();
						}
					});
			newDialog.setNegativeButton(R.string.dialog_Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			newDialog.show();
			return true;

			// after choosing this action and confirm it in dialog window - your
			// project is saved in database
		case R.id.action_save:
			try {
				AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
				saveDialog
						.setTitle(R.string.startActivity_menu_Dialog_Save_Title);
				saveDialog
						.setMessage(R.string.startActivity_menu_Dialog_Save_Message);
				saveDialog.setPositiveButton(R.string.dialog_Yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

								View zawartosc = findViewById(R.id.paintingView);
								zawartosc.setDrawingCacheEnabled(false);
								zawartosc.setDrawingCacheEnabled(true);
								bitmapa = zawartosc.getDrawingCache();

								obslugaBD.pobierzID();
								obslugaBD.dodajRecord();
								obslugaBD.utworzPlik(bitmapa, zawartosc);
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
			} catch (Exception e1) {
				Toast.makeText(
						this,
						(R.string.startActivity_menu_failedSaveToBrowser_Toast),
						Toast.LENGTH_SHORT).show();
				e1.printStackTrace();
			}
			return true;

			// after choosing open action user is transferred to browser
		case R.id.action_open:
			try {
				Intent intent = new Intent(this, Przegladarka.class);
				startActivity(intent);
				painterView.startNew();
				finish();
			} catch (Exception e) {
				Toast.makeText(this,
						(R.string.startActivitybrowserOpenNotAvailable_Toast),
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			return true;

			// after choosing this action and confirm it in dialog window - the
			// application is closed
		case R.id.action_exit:
			AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
			saveDialog.setTitle(R.string.startActivity_dialogExit_title);
			saveDialog.setMessage(R.string.startActivity_dialogExit_message);
			saveDialog.setPositiveButton(R.string.dialog_Yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							System.exit(0);
						}
					});
			saveDialog.setNegativeButton(R.string.dialog_Cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			saveDialog.show();

		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
