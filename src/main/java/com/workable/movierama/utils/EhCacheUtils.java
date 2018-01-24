package com.workable.movierama.utils;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by aris on 24/1/2018.
 */
public class EhCacheUtils {

	private static final Log log = LogFactory.getLog(EhCacheUtils.class);

	private EhCacheUtils() {
	}

	public static void clearCache() {
		for (CacheManager cm : CacheManager.ALL_CACHE_MANAGERS) {
			if (cm.getStatus().equals(Status.STATUS_ALIVE)) {
				for (String name : cm.getCacheNames()) {
					Ehcache cache = cm.getEhcache(name);
					if (cache.getStatus().equals(Status.STATUS_ALIVE)) {
						if (log.isTraceEnabled()) {
							log.trace("Cache " + name + " contains " + cache.getSize() + " objects.");
						}
						cache.removeAll();
						if (log.isTraceEnabled()) {
							log.trace("Clearing cache " + name + ". Now contains " + cache.getSize() + " objects.");
						}
					}
				}
			}
		}
	}
}
