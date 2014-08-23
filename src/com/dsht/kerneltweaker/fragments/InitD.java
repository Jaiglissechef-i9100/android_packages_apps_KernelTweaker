package com.dsht.kerneltweaker.fragments;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.dsht.kerneltweaker.MainActivity;
import com.dsht.kerneltweaker.R;
import com.dsht.kernetweaker.cmdprocessor.CMDProcessor;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;


public class InitD extends PreferenceFragment implements OnPreferenceChangeListener {

    private static final String KEY_ZIPALIGN_APKS = "zipalign_apks";
    private static final String KEY_FIX_PERMISSIONS = "fix_permissions";
    private static final String KEY_CLEAR_DATA_CACHE = "clear_data_cache";
    private static final String KEY_ENABLE_CRON = "enable_cron";
    private static final String KEY_FILE_SYSTEM_SPEEDUPS = "file_system_speedups";
    private static final String KEY_ENABLE_SYSCTL = "enable_sysctl";
    private static final String KEY_SD_BOOST = "sd_boost";
    private static final String KEY_BATTERY = "battery";
    private static final String KEY_TOUCH = "touch";
    private static final String KEY_MINFREE = "minfree";
    private static final String KEY_GPURENDER = "gpurender";
    private static final String KEY_SLEEPERS = "sleepers";
    private static final String KEY_JOURNALISM = "journalism";
    private static final String KEY_SQLITE3 = "sqlite3";
    private static final String KEY_WIFISLEEP = "wifisleep";
    private static final String KEY_IOSTATS = "iostats";
    private static final String KEY_SENTRENICE = "sentrenice";
    private static final String KEY_TWEAKY = "tweaky";
    private static final String KEY_SPEEDY_MODIFIED = "speedy_modified";
    private static final String KEY_LOOPY_SMOOTHNESS_TWEAK = "loopy_smoothness_tweak";
    private static final String REMOUNT_CMD = "busybox mount -o %s,remount /dev/block/mmcblk0p1 /system";

	private static final String[] KEYS = {
		KEY_ZIPALIGN_APKS, //0
		KEY_FIX_PERMISSIONS, //1
		KEY_CLEAR_DATA_CACHE,  //2
		KEY_ENABLE_CRON, //3
		KEY_FILE_SYSTEM_SPEEDUPS, //4
		KEY_ENABLE_SYSCTL, //5
		KEY_SD_BOOST, //6
		KEY_BATTERY, //7
		KEY_TOUCH, //8
		KEY_MINFREE, //9
		KEY_GPURENDER, //10
		KEY_SLEEPERS, //11
		KEY_JOURNALISM, //12
		KEY_SQLITE3, //13
		KEY_WIFISLEEP, //14
		KEY_IOSTATS, //15
		KEY_SENTRENICE, //16
		KEY_TWEAKY, //17
		KEY_SPEEDY_MODIFIED, //18
		KEY_LOOPY_SMOOTHNESS_TWEAK, //19
	};

	protected SharedPreferences mPrefs;
	private CheckBoxPreference mZipAlign;
	private CheckBoxPreference mFixPermissions;
	private CheckBoxPreference mClearCache;
	private CheckBoxPreference mEnableCron;
	private CheckBoxPreference mSysSpeedup;
	private CheckBoxPreference mEnableSystl;
	private CheckBoxPreference mSdBoost;
	private CheckBoxPreference mBattery;
	private CheckBoxPreference mtouch;
	private CheckBoxPreference mMinFree;
	private CheckBoxPreference mgpuRenderer;
	private CheckBoxPreference mSleepers;
	private CheckBoxPreference mJournalism;
	private CheckBoxPreference mSqlite;
	private CheckBoxPreference mWifiSleep;
	private CheckBoxPreference mIoStats;
	private CheckBoxPreference mSentrenice;
	private CheckBoxPreference mTweaks;
	private CheckBoxPreference mSpeedyModified;
	private CheckBoxPreference mLoopSmoothTweak;

	private static InitD sActivity;
	private static final String SCRIPT_HEAD = "#!/system/bin/sh";
	private static final String SCRIPT_HELPERS = ". /system/etc/helpers.sh";
	private static final String SCRIPT_PERMS = "chmod 755";
	private static final String ZIPALIGN_FILE = "01zipalign";
	private static final String INIT_PATH = "/system/etc/init.d/";
	private static final String CLEAR_CACHE_COMMAND = "busybox find /data/data -type d -iname \"*cache*\" -maxdepth 2 -mindepth 2 -exec busybox rm -rf {} ';' ";
	private static final String CACHE_FILE = "06removecache";
	private static final String FIXPERMS_FILE = "07fixperms";
	private static final String CRON_FILE = "09cron";
	private static final String TWEAKS_FILE = "98tweaks";
	private static final String SYSCTL_FILE = "08sysctl";
	private static final String SDBOOST_FILE = "10sdboost";
	private static final String BATTERY_FILE = "11battery";
	private static final String TOUCH_FILE = "12touch";
	private static final String MINFREE_FILE = "13minfree";
	private static final String GPU_FILE = "14gpurender";
	private static final String SLEEP_FILE = "15sleepers";
	private static final String JOURNAL_FILE = "16journalism";
	private static final String SQLITE_FILE = "17sqlite3";
	private static final String WIFI_FILE = "18wifisleep";
	private static final String IOS_FILE = "19iostats";
	private static final String SETRENICE_FILE = "20setrenice";
	private static final String TWEAKY_FILE = "21tweaks";
	private static final String SPEED_FILE = "24speedy_modified";
	private static final String SMOOTH_FILE = "25loopy_smoothness_tweak";

	public static InitD whatActivity() {
		return sActivity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sActivity = this;
		mPrefs    = PreferenceManager.getDefaultSharedPreferences(getActivity());
		addPreferencesFromResource(R.xml.init_d);

		mZipAlign = (CheckBoxPreference) findPreference(KEYS[0]);
		mFixPermissions = (CheckBoxPreference) findPreference(KEYS[1]);
		mClearCache = (CheckBoxPreference) findPreference(KEYS[2]);
		mEnableCron = (CheckBoxPreference) findPreference(KEYS[3]);
		mSysSpeedup = (CheckBoxPreference) findPreference(KEYS[4]);
		mEnableSystl = (CheckBoxPreference) findPreference(KEYS[5]);
		mSdBoost = (CheckBoxPreference) findPreference(KEYS[6]);
		mBattery = (CheckBoxPreference) findPreference(KEYS[7]);
		mtouch = (CheckBoxPreference) findPreference(KEYS[8]);
		mMinFree = (CheckBoxPreference) findPreference(KEYS[9]);
		mgpuRenderer = (CheckBoxPreference) findPreference(KEYS[10]);
		mSleepers = (CheckBoxPreference) findPreference(KEYS[11]);
		mJournalism = (CheckBoxPreference) findPreference(KEYS[12]);
		mSqlite = (CheckBoxPreference) findPreference(KEYS[13]);
		mWifiSleep = (CheckBoxPreference) findPreference(KEYS[14]);
		mIoStats = (CheckBoxPreference) findPreference(KEYS[15]);
		mSentrenice = (CheckBoxPreference) findPreference(KEYS[16]);
		mTweaks = (CheckBoxPreference) findPreference(KEYS[17]);
		mSpeedyModified = (CheckBoxPreference) findPreference(KEYS[18]);
		mLoopSmoothTweak = (CheckBoxPreference) findPreference(KEYS[19]);

		mZipAlign.setOnPreferenceChangeListener(this);
		mFixPermissions.setOnPreferenceChangeListener(this);
		mClearCache.setOnPreferenceChangeListener(this);
		mEnableCron.setOnPreferenceChangeListener(this);
		mSysSpeedup.setOnPreferenceChangeListener(this);
		mEnableSystl.setOnPreferenceChangeListener(this);
		mSdBoost.setOnPreferenceChangeListener(this);
		mBattery.setOnPreferenceChangeListener(this);
		mtouch.setOnPreferenceChangeListener(this);
		mMinFree.setOnPreferenceChangeListener(this);
		mgpuRenderer.setOnPreferenceChangeListener(this);
		mSleepers.setOnPreferenceChangeListener(this);
		mJournalism.setOnPreferenceChangeListener(this);
		mSqlite.setOnPreferenceChangeListener(this);
		mWifiSleep.setOnPreferenceChangeListener(this);
		mIoStats.setOnPreferenceChangeListener(this);
		mSentrenice.setOnPreferenceChangeListener(this);
		mTweaky.setOnPreferenceChangeListener(this);
		mSpeedyModified.setOnPreferenceChangeListener(this);
		mLoopSmoothTweak.setOnPreferenceChangeListener(this);

		loadValues();
		copyHelpers();
		if(MainActivity.menu.isMenuShowing()) {
			MainActivity.menu.toggle(true);
		}
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
		// TODO Auto-generated method stub
		boolean value = (Boolean)newValue;
		if(pref == mZipAlign) {
			if(value) {
				createInitD(ZIPALIGN_FILE, "zipalign_apks");
			} else {
				deleteInitD(INIT_PATH+ZIPALIGN_FILE);
			}
			return true;
		}
		if(pref == mFixPermissions) {
			if(value) {
				createInitD(FIXPERMS_FILE,"fix_permissions");
			} else {
				deleteInitD(INIT_PATH+FIXPERMS_FILE);
			}
			return true;
		}
		if(pref == mClearCache) {
			if(value) {
				createInitD(CACHE_FILE, CLEAR_CACHE_COMMAND);
			}else {
				deleteInitD(INIT_PATH+CACHE_FILE);
			}
			return true;
		}
		if(pref == mEnableCron) {
			if(value) {
				copyScript(CRON_FILE);
			}else {
				deleteInitD(INIT_PATH+CRON_FILE);
			}
			return true;
		}
		if(pref == mSysSpeedup) {
			if(value) {
				copyScript(TWEAKS_FILE);
			}else {
				deleteInitD(INIT_PATH+TWEAKS_FILE);
			}
			return true;
		}
		if(pref == mEnableSystl) {
			if(value) {
				copyScript(SYSCTL_FILE);
			}else {
				deleteInitD(INIT_PATH+SYSCTL_FILE);
			}
			return true;
		}
		if(pref == mSdBoost) {
			if(value) {
				copyScript(SDBOOST_FILE);
			}else {
				deleteInitD(INIT_PATH+SDBOOST_FILE);
			}
			return true;
		}
		if(pref == mBattery) {
			if(value) {
				copyScript(BATTERY_FILE);
			}else {
				deleteInitD(INIT_PATH+BATTERY_FILE);
			}
			return true;
		}
		if(pref == mtouch) {
			if(value) {
				copyScript(TOUCH_FILE);
			}else {
				deleteInitD(INIT_PATH+TOUCH_FILE);
			}
			return true;
		}
		if(pref == mMinFree) {
			if(value) {
				copyScript(MINFREE_FILE);
			}else {
				deleteInitD(INIT_PATH+MINFREE_FILE);
			}
			return true;
		}
		if(pref == mgpuRenderer) {
			if(value) {
				copyScript(GPU_FILE);
			}else {
				deleteInitD(INIT_PATH+GPU_FILE);
			}
			return true;
		}
		if(pref == mSleepers) {
			if(value) {
				copyScript(SLEEP_FILE);
			}else {
				deleteInitD(INIT_PATH+SLEEP_FILE);
			}
			return true;
		}
		if(pref == mJournalism) {
			if(value) {
				copyScript(JOURNAL_FILE);
			}else {
				deleteInitD(INIT_PATH+JOURNAL_FILE);
			}
			return true;
		}
		if(pref == mSqlite) {
			if(value) {
				copyScript(SQLITE_FILE);
			}else {
				deleteInitD(INIT_PATH+SQLITE_FILE);
			}
			return true;
		}
		if(pref == mWifiSleep) {
			if(value) {
				copyScript(WIFI_FILE);
			}else {
				deleteInitD(INIT_PATH+WIFI_FILE);
			}
			return true;
		}
		if(pref == mIoStats) {
			if(value) {
				copyScript(IOS_FILE);
			}else {
				deleteInitD(INIT_PATH+IOS_FILE);
			}
			return true;
		}
		if(pref == mSentrenice) {
			if(value) {
				copyScript(SETRENICE_FILE);
			}else {
				deleteInitD(INIT_PATH+SETRENICE_FILE);
			}
			return true;
		}
		if(pref == mTweaky) {
			if(value) {
				copyScript(TWEAKY_FILE);
			}else {
				deleteInitD(INIT_PATH+TWEAKY_FILE);
			}
			return true;
		}
		if(pref == mSpeedyModified) {
			if(value) {
				copyScript(SPEED_FILE);
			}else {
				deleteInitD(INIT_PATH+SPEED_FILE);
			}
			return true;
		}
		if(pref == mLoopSmoothTweak) {
			if(value) {
				copyScript(SMOOTH_FILE);
			}else {
				deleteInitD(INIT_PATH+SMOOTH_FILE);
			}
			return true;
		}
		loadValues();

		return false;
	}


	private void createInitD(final String filename, final String filecontent) {

		class AsyncCreateInitTask extends AsyncTask<Void, Void, Boolean> {

			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getActivity());
				pd.setIndeterminate(true);
				pd.setMessage("Creating script...Please wait");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				mount("rw");
				new CMDProcessor().su.runWaitFor("echo \""+SCRIPT_HEAD+"\" >> " + INIT_PATH + filename);
				new CMDProcessor().su.runWaitFor("echo \""+SCRIPT_HELPERS+"\" >> "+ INIT_PATH + filename );
				new CMDProcessor().su.runWaitFor("echo \""+ filecontent + "\" >> " + INIT_PATH + filename);
				new CMDProcessor().su.runWaitFor(SCRIPT_PERMS+" " + INIT_PATH + filename);
				mount("ro");
				return true;
			}

			@Override
			protected void onPostExecute(Boolean res) {
				// result holds what you return from doInBackground
				super.onPostExecute(res);
				pd.dismiss();
			}
		}
		new AsyncCreateInitTask().execute();
	}

	private void deleteInitD(final String filepath) {

		class AsyncDeleteInitTask extends AsyncTask<Void, Void, Boolean> {

			ProgressDialog pd;

			@Override
			protected void onPreExecute() {
				pd = new ProgressDialog(getActivity());
				pd.setIndeterminate(true);
				pd.setMessage("Deleting script...Please wait");
				pd.setCancelable(false);
				pd.show();
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				mount("rw");
				new CMDProcessor().su.runWaitFor("rm -f "+filepath);
				mount("ro");
				return true;
			}
			@Override
			protected void onPostExecute(Boolean res) {
				// result holds what you return from doInBackground
				super.onPostExecute(res);
				pd.dismiss();
			}
		}
		new AsyncDeleteInitTask().execute();
	}


	private void loadValues() {
		if(new File(INIT_PATH+ZIPALIGN_FILE).exists()) {
			mZipAlign.setChecked(true);
		}
		if(new File(INIT_PATH+FIXPERMS_FILE).exists()) {
			mFixPermissions.setChecked(true);
		}
		if(new File(INIT_PATH+CACHE_FILE).exists()) {
			mClearCache.setChecked(true);
		}
		if(new File(INIT_PATH+CRON_FILE).exists()) {
			mEnableCron.setChecked(true);
		}
		if(new File(INIT_PATH+TWEAKS_FILE).exists()) {
			mSysSpeedup.setChecked(true);
		}
	}

	public boolean mount(String read_value) {
		Log.d("TAG", "Remounting /system " + read_value);
		return new CMDProcessor().su.runWaitFor(String.format(REMOUNT_CMD, read_value)).success();
	}

	private void copyHelpers() {
		if(!new File("/system/etc/helpers.sh").exists()) {
			class AsyncCopyHelpersTask extends AsyncTask<Void, Void, Boolean> {

				ProgressDialog pd;

				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(getActivity());
					pd.setIndeterminate(true);
					pd.setMessage("Copying Helpers...Please wait");
					pd.setCancelable(false);
					pd.show();
				}

				@Override
				protected Boolean doInBackground(Void... params) {


					mount("rw");
					new CMDProcessor().su.runWaitFor("cp /data/data/com.dsht.kerneltweaker/files/helpers.sh " + "/system/etc");
					new CMDProcessor().su.runWaitFor("chmod 644 /system/etc/helpers.sh");
					mount("ro");

					return true;
				}
				@Override
				protected void onPostExecute(Boolean res) {
					// result holds what you return from doInBackground
					super.onPostExecute(res);
					pd.dismiss();
				}
			}
			new AsyncCopyHelpersTask().execute();
		}
	}
	
	private void copyScript(final String name) {
			class AsyncCopyScriptTask extends AsyncTask<Void, Void, Boolean> {

				ProgressDialog pd;

				@Override
				protected void onPreExecute() {
					pd = new ProgressDialog(getActivity());
					pd.setIndeterminate(true);
					pd.setMessage("Copying script...Please wait");
					pd.setCancelable(false);
					pd.show();
				}

				@Override
				protected Boolean doInBackground(Void... params) {

					copyAsset(name);
					mount("rw");
					new CMDProcessor().su.runWaitFor("cp /data/data/com.dsht.kerneltweaker/files/"+name+" " + "/system/etc/init.d");
					new CMDProcessor().su.runWaitFor("chmod 644 /system/etc/init.d/"+name);
					mount("ro");

					return true;
				}
				@Override
				protected void onPostExecute(Boolean res) {
					// result holds what you return from doInBackground
					super.onPostExecute(res);
					pd.dismiss();
				}
			}
			new AsyncCopyScriptTask().execute();
	}
	
	private void copyAsset(String name) {

		if(!new File(getActivity().getFilesDir().getPath(),name).exists()) {

			InputStream stream = null;
			OutputStream output = null;

			try {
				stream = getActivity().getAssets().open(name);
				output = new BufferedOutputStream(new FileOutputStream(getActivity().getFilesDir()+"/"+name));

				byte data[] = new byte[1024];
				int count;

				while((count = stream.read(data)) != -1)
				{
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				stream.close();

				stream = null;
				output = null;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
