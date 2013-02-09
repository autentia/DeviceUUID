package com.autentia.deviceuuid;

import java.util.UUID;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SerialInfo {
	
	private static final String TAG = "com.autentia.uuid";

	/**
	 * GSF Service (Google Service Framework)
	 */
	private static final Uri GSF_URI = Uri.parse("content://com.google.android.gsf.gservices");
	
	/**
	 * Key del ID
	 */
	private static final String GSF_ID_KEY = "android_id";
    
	/**
	 * Devuelve el Device ID segun GSF (Google Service Framework)<br>
	 * Ej: 3189147f8714e99a<br>
	 * Se necesita: < uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" /> en el AndroidManisfest.xml
	 * @param ctx Contexto de Aplicacion o Actividad
	 * @return cadena con el id
	 */
	public static String getGSFId(Context ctx) {
	    final String[] params = { GSF_ID_KEY };
	    final Cursor c = ctx.getContentResolver().query(GSF_URI, null, null, params, null);
	 
	    if (!c.moveToFirst() || c.getColumnCount() < 2)
	        return null;
	 
	    try {
	        return Long.toHexString(Long.parseLong(c.getString(1)));
	    } catch (NumberFormatException e) {
	        return null;
	    } finally {
	    	if (c!=null)
	    		c.close();
	    }
	}
	
	@SuppressLint("NewApi")
	public static String getBuildSerialId()  {
		// Intentamos Tomar el SERIAL del Hardware
		String deviceId = null;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			deviceId = android.os.Build.SERIAL; // Android 2.3 y superiores
		} else {
			deviceId = "undefined";
		}
		return deviceId;
	}

	/**
	 * Devuelve el SERIAL del Hardware (android.os.Build.SERIAL), o el SERIAL de Arranque del Dispositivo (Settings.Secure.ANDROID_ID)<br>
	 * Ej: 202ec37cf8d93ece
	 * @return un numero de Serie unico del Hardware
	 * @throws Exception Si el algoritmo de calculo falla
	 */
	public static String getAndroidId(Context ctx)  {
		// Intentamos Tomar el SERIAL del Hardware
		String deviceId = null;
		// tomamos el ANDROID_ID => si esto es NULO, no podemos IDENTIFICAR al Aparato de forma UNICA
		deviceId = Settings.Secure.getString( ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
		if (deviceId == null || deviceId.equalsIgnoreCase("android_id") || deviceId.equalsIgnoreCase("9774d56d682e549c")) {
			deviceId = "undefined";
		}
		return deviceId;
	}
	
	

	/**
	 * Devuelve el SERIAL del Hardware "Tagus Tablet" (IMEI)<br>
	 * Ej: 202141237121581
	 * @return un numero de Serie unico del Hardware Tagus (IMEI)
	 * @throws Exception Si el algoritmo de calculo falla
	 */
	public static String getIMEI(Context ctx) {
		String tmDevice = null;
		try {
			// Intentamos Tomar el SERIAL del Hardware de Telefonia
			final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			tmDevice = tm.getDeviceId(); // the IMEI for GSM 
			if (tmDevice == null) {
				tmDevice = "undefined";
			}
		} catch (Exception ex) {
			tmDevice = "undefined";
		}

		return tmDevice;
	}
	

	
	/**
	 * Devuelve un Identificador unico de DISPOSITO Android UUID
	 * en base a un algoritmo propio que utiliza el DeviceID(IMEI nulo si no es telefono), Serial SIM(nulo si no hay SIM) y ANDROID_ID<br>
	 * Si cambia cualquiera de estos parametros, cambiará el Identificador Calculado.<br>
	 * Ej: 00000000-381a-6648-50b0-ec5a0033c587<br>
	 * En el peor de los casos, devuelve el android.os.Build.SERIAL o Settings.Secure.ANDROID_ID<br>
	 * @return String con el Device ID calculado
	 */
	public static String getDeviceId(Context ctx) {

		String deviceId = null;
		try {
			final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""+ android.provider.Settings.Secure.getString(ctx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

			UUID deviceUuid = new UUID( androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode() );
			deviceId = deviceUuid.toString();
		} catch (Exception e) {
			// Intentamos Tomar el SERIAL del Hardware -> porque el Terminal era un TELEFONO pero mal configurado (sin SIM o error con datos incorrectos en la Telefonia)
			try {
				deviceId = getSerialDeviceId(ctx);
			} catch (Exception e1) {
				throw new RuntimeException("FATAL!!!! - This device doesn't have a UNIQUE Serial Number", e);
			}
		}

		return deviceId;
	}
	
	/**
	 * Devuelve una cadena de Descripcion del Dispositivo:<br>
	 * Build.MANUFACTURER + Build.MODEL + Build.VERSION.RELEASE<br>
	 * Ej: MID Tagus Tablet - 4.0.3
	 * @return 
	 */
	public static String getPid(){
		StringBuilder pid = new StringBuilder(Build.MANUFACTURER).append(" ").append(Build.MODEL).append(" - ").append(Build.VERSION.RELEASE);
		return pid.toString();
	}	
	
	
	/**
	 * Devuelve el SERIAL del Hardware (android.os.Build.SERIAL), o el SERIAL de Arranque del Dispositivo (Settings.Secure.ANDROID_ID)<br>
	 * Ej: 202ec37cf8d93ece
	 * @return un numero de Serie unico del Hardware
	 * @throws Exception Si el algoritmo de calculo falla
	 */
	@SuppressLint("NewApi")
	public static String getSerialDeviceId(Context ctx) throws Exception {
		// Intentamos Tomar el SERIAL del Hardware
		String deviceId = null;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD) {
			deviceId = android.os.Build.SERIAL; // Android 2.3 y superiores
		}
		if (deviceId==null || deviceId.equalsIgnoreCase("unknown")) {
			// tomamos el ANDROID_ID => si esto es NULO, no podemos IDENTIFICAR al Aparato de forma UNICA
			deviceId = Settings.Secure.getString( ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
			if (deviceId == null || deviceId.equalsIgnoreCase("android_id") || deviceId.equalsIgnoreCase("9774d56d682e549c")) {
				throw new Exception("FATAL!!!! - This device doesn't have a UNIQUE Serial Number");
			}
		}
		return deviceId;
	}
	
	/**
	 * Devuelve el SERIAL del Hardware "Tagus Tablet" (IMEI)<br>
	 * Ej: 202141237121581
	 * @return un numero de Serie unico del Hardware Tagus (IMEI)
	 * @throws Exception Si el algoritmo de calculo falla
	 */
	public static String getTagusSerialDeviceId(Context ctx) {
		String tmDevice = null;
		try {
			// Intentamos Tomar el SERIAL del Hardware de Telefonia
			final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
			tmDevice = tm.getDeviceId(); // the IMEI for GSM 
			if (tmDevice == null) {
				// Si es un TABLET "Tagus" pero NO tiene Telefonia
				tmDevice = getSerialDeviceId(ctx);
			}
		} catch (Exception ex) {
			tmDevice = getDeviceId(ctx);
			Log.w(TAG, "Error generando TAGUS Serial DEVICE ID: "+tmDevice);
		}

		return tmDevice;
	}	
}
