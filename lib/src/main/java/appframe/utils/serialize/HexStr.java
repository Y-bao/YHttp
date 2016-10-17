package appframe.utils.serialize;

import java.util.Locale;

public class HexStr {
	/**
	 * 字符串转换成十六进制字符串
	 *
	 * @param String
	 *            str 待转换的ASCII字符串
	 * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
	 */
	private final static char[] chars = "0123456789ABCDEF".toCharArray();

	public static String encode(byte[] data) {

		StringBuilder sb = new StringBuilder("");
		int bit;

		for (int i = 0; i < data.length; i++) {
			bit = (data[i] & 0xf0) >> 4;
			sb.append(chars[bit]);
			bit = data[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString().trim();
	}

	/**
	 * 十六进制转换字符串
	 *
	 * @param String
	 *            str Byte字符串(Byte之间无分隔符 如:[616C6B])
	 * @return String 对应的字符串
	 */
	public static byte[] decode(String hexStr) {
		String str = "0123456789ABCDEF";
		char[] hexs = hexStr.toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return bytes;
	}

	/**
	 * bytes转换成十六进制字符串
	 *
	 * @param byte[] b byte数组
	 * @return String 每个Byte值之间空格分隔
	 */
	public static String encodeByJavaWay(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
		}
		return sb.toString().toUpperCase(Locale.US).trim();
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *            src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] decodeByJavaWay(String src) {
		int m = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2;
			ret[i] = Byte.decode("0x" + src.substring(m, m + 1) + src.substring(m + 1, m + 2));
		}
		return ret;
	}

}
