package m.tri.facedetectcamera.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BitmapUtil {
    /**
     * 质量压缩
     *
     * @param @param  bitmap
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: bitmapToBase64
     */
    @SuppressLint("NewApi")
    public static String bitmapToBase64(Bitmap bitmap, int quality) {
        // 要返回的字符串
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();
                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reslut;
    }

    /**
     * 采样率压缩
     *
     * @param bitmap
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static String bitmapSampleToBase64(Bitmap bitmap, int minSideLength, int maxNumOfPixels) {
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            byte[] origin = baos.toByteArray();

            final BitmapFactory.Options options = new BitmapFactory.Options();
            //只读取图片，不加载到内存中
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(origin, 0, origin.length, options);

            //返回合适的inSampleSize值
            options.inSampleSize = computeSampleSize(options, -1, 512 * 512);
            //加载到内存中
            options.inJustDecodeBounds = false;
            Bitmap sampleBitmap = BitmapFactory.decodeByteArray(origin, 0, origin.length, options);
            reslut = bitmapToBase64(sampleBitmap, 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return reslut;
    }

    /**
     * 缩放压缩
     *
     * @param bitmap
     * @param ratio
     * @return
     */
    public static String bitmapZoomToBase64(Bitmap bitmap, float ratio) {
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                Bitmap scaleBitmap = zoomBitmap(bitmap, ratio);
                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                scaleBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();
                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reslut;
    }

    /**
     * RGB_565压缩
     */
    public static String bitmapRGB565ToBase64(Bitmap bitmap) {
        String reslut = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            byte[] origin = baos.toByteArray();
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            Bitmap rgb565Bitmap = BitmapFactory.decodeByteArray(origin, 0, origin.length, options);
            reslut = bitmapToBase64(rgb565Bitmap, 100);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return reslut;
    }

    /**
     * 重新创建位图压缩（可能不清晰）
     *
     * @param bitmap
     * @return
     */
    public static String bitmapCreateScaledToBase64(Bitmap bitmap, int destWidth, int destHeight) {
        Bitmap destBitmap = null;
        if (bitmap.getWidth() == destHeight && bitmap.getHeight() == destHeight) {
            destBitmap = bitmap;
        } else {
            destBitmap = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, true);
        }

        return bitmapToBase64(destBitmap, 100);
    }

    /**
     * base64转bitmap
     *
     * @param @param  base64String
     * @param @return 设定文件
     * @return Bitmap    返回类型
     * @throws
     * @Title: base64ToBitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decode = Base64.decode(base64String, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);

        return bitmap;
    }

    public static Bitmap zoomBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }


    /**
     * 计算采样
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
