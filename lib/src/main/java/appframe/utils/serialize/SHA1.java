package appframe.utils.serialize;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {

	public static String encode(byte[] data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.update(data);
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
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
