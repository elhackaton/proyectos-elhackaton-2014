package com.hackaton.carrot.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class CarrotDataBase {

	public static final String DATABASE_PATH = Environment
			.getExternalStorageDirectory() + "/carrot";
	private static final String DATABASE_NAME = "carrot_database.sqlite";
	private static final int DATABASE_VERSION = 1;
	private final DatabaseOpenHelper mDatabaseOpenHelper;
	private Context mContext;

	public CarrotDataBase(Context context) {
		this.mContext = context;

		if (!checkIfExistDB()) {
			copyDBfromAssets();
		}

		mDatabaseOpenHelper = new DatabaseOpenHelper(context);
	}

	private boolean checkIfExistDB() {
		File file = new File(DATABASE_PATH + DATABASE_NAME);
		return file.exists();
	}

	public int getDangerousFromProduct(String id_producto) {
		String query = "select distinct aditivos.dangerous from prodad "
				+ "inner join aditivos "
				+ "where (prodad.id_aditivo=aditivos.id) and (prodad.id_producto=?)";

		System.out.println("Query: " + query);

		Cursor c = mDatabaseOpenHelper.rawQuery(query,
				new String[] { id_producto });

		if (c.moveToFirst() == false) {
			return -1;
		}

		String dangerous = "";
		do {
			dangerous = c.getString(0);
			if (dangerous.contains("P"))
				return 2;
			if (dangerous.contains("S"))
				return 1;
			if (dangerous.contains("B"))
				return 0;
		} while (c.moveToNext());

		return -1;
	}

	public String getImageFromProduct(String id_producto) {
		String query = "select image from productos where productos.id=?";

		System.out.println("Query: " + query);

		Cursor c = mDatabaseOpenHelper.rawQuery(query, new String[] { id_producto });

		if (c.moveToFirst() == false) {
			return null;
		}

		return c.getString(0);
	}	
	
	private void copyDBfromAssets() {
		try {
			InputStream in = mContext.getAssets().open(DATABASE_NAME);
			OutputStream out = new FileOutputStream(DATABASE_PATH
					+ DATABASE_NAME);

			byte[] buffer = new byte[1024];
			int size;
			while ((size = in.read(buffer)) > 0) {
				out.write(buffer, 0, size);
			}

			out.flush();
			out.close();
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		mDatabaseOpenHelper.close();
	}

	private static class DatabaseOpenHelper extends SQLiteOpenHelper {

		private final Context mHelperContext;
		public SQLiteDatabase mDatabase;

		DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;

			try {
				mDatabase = SQLiteDatabase.openDatabase(
						CarrotDataBase.DATABASE_PATH
								+ CarrotDataBase.DATABASE_NAME, null,
						SQLiteDatabase.OPEN_READWRITE);
			} catch (SQLiteException e) {
				Toast.makeText(mHelperContext, "Error: " + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

		public Cursor rawQuery(String sql, String[] selectionArgs) {
			return mDatabase.rawQuery(sql, selectionArgs);
		}

		public void close() {
			mDatabase.close();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			mDatabase = db;
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}

	}
}