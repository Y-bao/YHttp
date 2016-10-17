package appframe.module.http.cache.memorycache;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class SoftBmLruCache extends LinkedHashMap<String, SoftReference<Bitmap>> {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	private static int MAX_ENTRIES = 10;

	public SoftBmLruCache(int initialCapacity) {
		this(initialCapacity, 0.75f);
	}

	public SoftBmLruCache(int initialCapacity, float loadFactor) {
		this(initialCapacity, loadFactor, true);
	}

	public SoftBmLruCache(int initialCapacity, float loadFactor, boolean accessOrder) {
		super(initialCapacity, loadFactor, accessOrder);
	}

	@Override
	public SoftReference<Bitmap> get(Object key) {
		return super.get(key);
	}

	@Override
	protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
		if (size() > MAX_ENTRIES) {
			return true;
		}
		return false;
	}
}
