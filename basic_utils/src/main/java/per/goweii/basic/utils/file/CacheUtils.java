package per.goweii.basic.utils.file;

import java.io.File;

import per.goweii.basic.utils.Utils;

/**
 * 缓存辅助类
 *
 * @author Cuizhen
 * @date 18/4/23
 */
public class CacheUtils {

    /**
     * 获取系统默认缓存文件夹
     * 优先返回SD卡中的缓存文件夹
     */
    public static String getCacheDir() {
        File cacheFile = null;
        if (FileUtils.isSDCardAlive()) {
            cacheFile = Utils.getContext().getExternalCacheDir();
        }
        if (cacheFile == null) {
            cacheFile = Utils.getContext().getCacheDir();
        }
        return cacheFile.getAbsolutePath();
    }

    public static String getFilesDir() {
        File cacheFile = Utils.getContext().getFilesDir();
        return cacheFile.getAbsolutePath();
    }

    /**
     * 获取系统默认缓存文件夹内的缓存大小
     */
    public static String getTotalCacheSize() {
        long cacheSize = FileUtils.getSize(Utils.getContext().getCacheDir());
        if (FileUtils.isSDCardAlive()) {
            cacheSize += FileUtils.getSize(Utils.getContext().getExternalCacheDir());
        }
        return FileUtils.formatSize(cacheSize);
    }

    /**
     * 清除系统默认缓存文件夹内的缓存
     */
    public static void clearAllCache() {
        FileUtils.delete(Utils.getContext().getCacheDir());
        if (FileUtils.isSDCardAlive()) {
            FileUtils.delete(Utils.getContext().getExternalCacheDir());
        }
    }

}