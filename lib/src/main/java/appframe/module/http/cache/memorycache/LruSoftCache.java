package appframe.module.http.cache.memorycache;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import java.lang.ref.SoftReference;

@SuppressLint("NewApi")
public class LruSoftCache {
	private static LruCache<String, Bitmap> strongCache;
	private static LruCache<String, SoftReference<Bitmap>> softCache;

	private static int UNIT = 1024 * 1024;

	public static void init(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		Long bimapMem = mi.availMem;
		strongCache = new LruCache<String, Bitmap>((int) (bimapMem / 8.0f * UNIT)) {

		};
		softCache = new LruCache<String, SoftReference<Bitmap>>((int) (bimapMem / 4.0f * UNIT));
		{

		}
	}

	public void init(int strongSize, int softSize) {
		strongCache = new LruCache<String, Bitmap>(strongSize * UNIT);
		softCache = new LruCache<String, SoftReference<Bitmap>>(softSize * UNIT);
	}

	class StrongBmLruCache extends LruCache<String, Bitmap> {

		public StrongBmLruCache(int maxSize) {
			super(maxSize);
		}

		@Override
		public int sizeOf(String key, Bitmap value) {
			return value.getRowBytes() * value.getHeight();
		}

		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {

		}

	}

}
