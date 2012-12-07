package org.apache.cordova.plugin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class Zip extends Plugin {
	private static final int BUFFER = 2048;
	final byte temp[] = new byte[BUFFER];
	public static final String SUCCESS_PARAMETER = "success";

	@Override
	public PluginResult execute(final String action, final JSONArray data, final String callbackId) {
		String result = null;

		final String exit = cordova.getActivity().getApplicationContext().getCacheDir()
							+ "zipa.zip"; // zip name

		try {
			String filepath = getRealPathFromURI(Uri.parse(data.getString(0)));
			FileOutputStream dest = new FileOutputStream(exit);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
			FileInputStream fi = new FileInputStream(filepath);
			BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
			
			ZipEntry entry = new ZipEntry(filepath.substring(filepath.lastIndexOf("/") + 1));
			entry.setMethod(ZipEntry.DEFLATED);
			out.putNextEntry(entry);
			
			int count;
			while ((count = origin.read(temp, 0, BUFFER)) != -1) {
				out.write(temp, 0, count);
			}
			
			origin.close();
			out.close();
			result = "success";
			
		} catch (final Exception ex) {
			Log.d("ZipPlugin", ex.toString());
		}
		
		if (result.equals(SUCCESS_PARAMETER)) {
			return new PluginResult(PluginResult.Status.OK, exit);
		} else {
			return new PluginResult(PluginResult.Status.ERROR,
					"Oops, Error :(");
		}
	}

	// Find path from URI
	private String getRealPathFromURI(final Uri contentURI) {
		final Cursor cursor = cordova.getActivity().getApplicationContext()
							  .getContentResolver().query(contentURI, null, null, null, null);
		cursor.moveToFirst();
		final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}
}