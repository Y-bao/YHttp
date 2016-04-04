package appframe.utils.serialize;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	// public final static String encode(byte[] data) {
	// char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
	// 'a', 'b', 'c', 'd', 'e', 'f' };
	// try {
	// MessageDigest mdTemp = MessageDigest.getInstance("MD5");
	// mdTemp.update(data);
	// byte[] md = mdTemp.digest();
	// int j = md.length;
	// char str[] = new char[j * 2];
	// int k = 0;
	// for (int i = 0; i < j; i++) {
	// byte byte0 = md[i];
	// str[k++] = hexDigits[byte0 >>> 4 & 0xf];
	// str[k++] = hexDigits[byte0 & 0xf];
	// }
	// return new String(str);
	// } catch (Exception e) {
	// return null;
	// }
	// }

	public final static String encode(byte[] data) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(data);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < md.length; i++) {
				String shaHex = Integer.toHexString(md[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
