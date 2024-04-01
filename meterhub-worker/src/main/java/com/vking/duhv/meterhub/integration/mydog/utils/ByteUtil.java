package com.vking.duhv.meterhub.integration.mydog.utils;

import com.vking.duhv.meterhub.integration.mydog.analyse103.enums.UControlEnum;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * byte 工具类
 */
public class ByteUtil {

    private static int controlLength = 4;
	
	/**
	 * 
	* @Title: intToByteArray  
	* @Description: int 转换成 byte数组 
	* @param @param i
	* @param @return 
	* @return byte[]   
	* @throws
	 */
	public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 根据个数解析double[]
     * 取 8 * num 的有效数据
     *
     * @param bytes
     * @param num
     * @return
     */
    public static double[] byteToDoubles(byte[] bytes, int num) {
        double[] doubles = new double[num];
        for (int i = 0; i < num; i++) {
            byte[] date = byteToByte(bytes, i * 8, 8);
            double value = ByteToDouble(date);
            doubles[i] = value;
        }
        return doubles;
    }

    public static byte[] double2Bytes(double data) {
        long intBits = Double.doubleToLongBits(data);
              return getBytes(intBits);
    }

    public static byte[] getBytes(float data) {
          int intBits = Float.floatToIntBits(data);
          byte[] b = getBytes(intBits);
          return b;
    }
    public static byte[] getBytes(int data) {
          byte[] bytes = new byte[4];
           bytes[0] = (byte) ((data & 0xff000000) >> 24);
          bytes[1] = (byte) ((data & 0xff0000) >> 16);
            bytes[2] = (byte) ((data & 0xff00) >> 8);
              bytes[3] = (byte) (data & 0xff);
           return bytes;
    }

    /**
     * 八字节bytes数组转为double
     *
     * @param bytes
     * @return
     */
    public static double ByteToDouble(byte[] bytes) {
        long doubleValue = byteToLong(bytes);
        double v1 = Double.longBitsToDouble(doubleValue);
        return v1;
    }

    /**
     * 8字节bytes数组转为long
     *
     * @param bytes1
     * @return
     */
    public static long byteToLong(byte[] bytes1) {
        //高低位转换 看协议是否需要
        byte[] bytes = reByte(bytes1);
        byte[] b = new byte[8];
        int i = b.length - 1;
        int j = bytes.length - 1;
        for (; i >= 0; i--, j--) {
            if (j >= 0) {
                b[i] = bytes[j];
            } else {
                b[i] = 0;
            }
        }
        long v0 = (long) (b[0] & 0xff) << 56;
        long v1 = (long) (b[1] & 0xff) << 48;
        long v2 = (long) (b[2] & 0xff) << 40;
        long v3 = (long) (b[3] & 0xff) << 32;
        long v4 = (long) (b[4] & 0xff) << 24;
        long v5 = (long) (b[5] & 0xff) << 16;
        long v6 = (long) (b[6] & 0xff) << 8;
        long v7 = (long) (b[7] & 0xff);
        return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7;
    }

    /**
     * 大小端 高低位转换
     *
     * @param bytes
     * @return
     */
    public static byte[] reByte(byte[] bytes) {
        int n = bytes.length;
        byte[] b = new byte[n];
        for (int i = 0; i < n; i++) {
            b[i] = bytes[n - 1 - i];
        }
        return b;
    }

	/**
	* @Title: shortToByteArray  
	* @Description: short 转换成 byte[] 
	* @param @param val
	* @param @return 
	* @return byte[]   
	* @throws
	 */
	public static byte[] shortToByteArray(short val) {
		byte[] b = new byte[2];
		b[0] = (byte) ((val >> 8) & 0xff);
		b[1] = (byte) (val & 0xff);
		return b;
	}
	
	/**
	 * 
	* @Title: byteArrayToInt  
	* @Description: byte[] 转换成 int
	* @param @param bytes
	* @param @return 
	* @return int   
	* @throws
	 */
	public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }
	
	/**
	 * 
	* @Title: byteArrayToShort  
	* @Description: byte[] 转换成short 
	* @param @param bytes
	* @param @return 
	* @return short   
	* @throws
	 */
	public static short byteArrayToShort(byte[] bytes) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (1 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }
	
	
	/**
	 *
	* @Title: listToBytes
	* @Description: TODO
	* @param @param byteList
	* @param @return
	* @return byte[]
	* @throws
	 */
	public static byte[] listToBytes(List<Byte> byteList) {
		byte[] bytes = new byte[byteList.size()];
		int index = 0;
		for (Byte item : byteList) {
			bytes[index++] = item;
		}
		return bytes;
	}
	
	/**
	 * 
	* @Title: date2HByte  
	* @Description: 日期转换成 CP56Time2a
	* @param @param date
	* @param @return 
	* @return byte[]   
	* @throws
	 */
    public static byte[] date2Hbyte(Date date) {
    	ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 毫秒需要转换成两个字节其中 低位在前高位在后 
        // 先转换成short
        int millisecond = calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);
        
        // 默认的高位在前
        byte[] millisecondByte = intToByteArray(millisecond);
        bOutput.write((byte) millisecondByte[3]);
        bOutput.write((byte) millisecondByte[2]);
        
        // 分钟 只占6个比特位 需要把前两位置为零 
        bOutput.write((byte) calendar.get(Calendar.MINUTE));
        // 小时需要把前三位置零
        bOutput.write((byte) calendar.get(Calendar.HOUR_OF_DAY));
        // 星期日的时候 week 是0 
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY) {
        	week = 7;
        } else {
        	week--;
        } 
        // 前三个字节是 星期 因此需要将星期向左移5位  后五个字节是日期  需要将两个数字相加 相加之前需要先将前三位置零
        bOutput.write((byte) (week << 5) + (calendar.get(Calendar.DAY_OF_MONTH)));
        // 前四字节置零
        bOutput.write((byte) ((byte) calendar.get(Calendar.MONTH) + 1));
        bOutput.write((byte) (calendar.get(Calendar.YEAR) - 2000));
        return bOutput.toByteArray();
    }
    
    
    /**
	 * 
	* @Title: date2HByte  
	* @Description:CP56Time2a转换成  时间
	* @param @param date
	* @param @return 
	* @return byte[]   
	* @throws
	 */
    public static Date  byte2Hdate(byte[] dataByte) {
        int year = (dataByte[6] & 0x7F) + 2000;
        int month = dataByte[5] & 0x0F;
        int day = dataByte[4] & 0x1F;
        int hour = dataByte[3] & 0x1F;
        int minute = dataByte[2] & 0x3F;
        int second = dataByte[1] > 0 ? dataByte[1] : (int) (dataByte[1] & 0xff);
        int millisecond = dataByte[0] > 0 ? dataByte[0] : (int) (dataByte[0] & 0xff);
        millisecond = (second << 8) + millisecond;
        second = millisecond / 1000;
        millisecond = millisecond % 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

	public static String byteArrayToHexString(byte[] array) {
        return byteArray2HexString(array, Integer.MAX_VALUE, false);
    }

	public static String byteArray2HexString(byte[] arrBytes, int count, boolean blank) {
        String ret = "";
        if (arrBytes == null || arrBytes.length < 1) {
        	return ret;
        }
        if (count > arrBytes.length) {
        	count = arrBytes.length;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            ret = Integer.toHexString(arrBytes[i] & 0xFF).toUpperCase();
            if (ret.length() == 1) {
            	builder.append("0").append(ret);
            } else {
            	builder.append(ret);
            }
            if (blank) {
            	builder.append(" ");
            }
        }

        return builder.toString();

    }

    /**
     * 返回指定位置的数组
     * @param bytes
     * @param start 开始位置
     * @param length  截取长度
     * @return
     */
	public  static byte[] getByte(byte[] bytes, int start, int length) {
		byte[] ruleByte = new byte[length];
		int index = 0;
		while (index < length) {
			ruleByte[index++] = bytes[start++];
		}
		return ruleByte;
	}

    /**
     * 浮点转换为字节
     * @param f
     * @return
     */
    public static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;

    }

    /**
     * 十六进制字符串转换成byte数组
     * @param hexStr
     * @return
     */
	public static  byte[] hexStringToBytes(String hexStr){
        hexStr = hexStr.replaceAll(" ", "");
        hexStr = hexStr.toUpperCase();
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

//    public static String byteToString(Byte[] buffer) {
//        try {
//            int length = 0;
//            for (int i = 0; i < buffer.length; ++i) {
//                if (buffer[i] == 0) {
//                    length = i;
//                    break;
//                }
//            }
//            return new String(buffer, 0, length, "UTF-8");
//        } catch (Exception e) {
//            return "";
//        }
//    }

    /**
     * byte ->int - >double  (int *0.01)
     * @param bytes
     * @return
     */
    public static double[] byteToIntToDouble( byte[] bytes) {
        int[] ints = byteToInt2s(bytes);
        double[] doubles = intToDouble(ints);
        return doubles;
    }

    public static int[] byteToInt2s(byte[] bytes) {
        int num = bytes.length / 2;
        int[] ints = byteToInt2S(bytes, num);
        return ints;
    }

    public static int[] byteToIntArr(byte[] bytes) {
        int[] ints = new int[bytes.length];
        for (int i = 0;i<bytes.length;i++){
            ints[i] = bytes[i];
        }
        return ints;
    }

    public static double[] intToDouble(int[] ints) {
        int length = ints.length;
        double[] doubles = new double[length];
        for (int i = 0; i < length;i++){
            doubles[i] = ints[i] * 0.01;
        }
        return doubles;
    }

    /**
     * 2字节bytes数组转换为无符号短整型
     *
     * @param bytes
     * @return
     */
    public static int byteToInt2(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length];
        if (bytes.length == 2) {
            newBytes[1] = bytes[0];
            newBytes[0] = bytes[1];
        } else {
            newBytes = bytes;
        }
        return Unpooled.wrappedBuffer(newBytes).readUnsignedShort();

    }
    public static int[] byteToInt2S(byte[] bytes, int num) {
        int[] ints = new int[num];
        for (int i = 0; i < num; i++) {
            byte[] date = byteToByte(bytes, i * 2, 2);
            int value = byteToInt2(date);
            ints[i] = value;
        }
        return ints;
    }

    /**
     * 2字节bytes数组转为short
     *
     * @param bytes
     * @return
     */
    public static short byteToShort(byte[] bytes) {
        return (short) (((bytes[1] << 8) | bytes[0] & 0xff));
    }

    /**
     * 截取 字节数组
     *
     * @param bytes  源
     * @param start  起始位置
     * @param length 长度
     * @return
     */
    public static byte[] byteToByte(byte[] bytes, int start, int length) {
        byte[] nByte = new byte[length];
        System.arraycopy(bytes, start, nByte, 0, length);
        return nByte;
    }


    /**
     * I 格式 低位在前
     * @param accept 接收序列号
     * @param send 发送序列号
     * @return
     */
    public static byte[] getIcontrol(short accept, short send) {
        byte[] control = new byte[4];
        // 向左移动一位 保证低位的D0 是0
        send = (short) (send << 1);
        control[0] =  (byte) ((send));
        control[1]  =  (byte) ((send >> 8));
        accept = (short) (accept << 1);
        control[2] =   (byte) ((accept));
        control[3]  =  (byte) ((accept >> 8));
        return control;
    }

    /**
     * 返回控制域中的接收序号
     * @param control
     * @return
     */
    public  static short getAccept(byte[] control) {
        int accept = 0;
        short  acceptLow =   (short) (control[2] & 0xff);
        short  acceptHigh =   (short) (control[3] & 0xff);
        accept += acceptLow;
        accept += acceptHigh << 8;
        accept = accept >> 1;
        return (short) accept;

    }

    /**
     * 返回控制域中的发送序号
     * @param control
     * @return
     */
    public  static short getSend(byte[] control) {
        int send = 0;
        short  acceptLow =  (short) (control[0] & 0xff);
        short  acceptHigh =  (short) (control[1] & 0xff);
        send += acceptLow;
        send += acceptHigh << 8;
        send = send >> 1;
        return (short) send;
    }

    /**
     * S 格式
     * @param accept
     * @return
     */
    public static byte[] getScontrol(short accept) {
        byte[] control = new byte[4];
        // 向左移动一位 保证低位的D0 是0
        short send = 1;
        control[0] =  (byte) ((send));
        control[1]  =  (byte) ((send >> 8));
        accept = (short) (accept << 1);
        control[2] =   (byte) ((accept));
        control[3]  =  (byte) ((accept >> 8));
        return control;
    }

    /**
     *
     * @Title: 返回U帧
     * @Description: 判断是否是
     * @param @param control
     * @param @return
     * @return boolean
     * @throws
     */
    public static UControlEnum getUcontrol(byte[] control) {
        if (control.length < controlLength || control[1] != 0 || control[3] != 0 || control[2] != 0) {
            return null;
        }
        int controlInt = ByteUtil.byteArrayToInt(control);
        for (UControlEnum ucontrolEnum : UControlEnum.values()) {
            if (ucontrolEnum.getValue() == controlInt) {
                return ucontrolEnum;
            }
        }
        return null;
    }

    /**
     * 3个字节转换成16进制
     */
    public static String analysHexForThree(int high,int mid, int low) {
        String lowString = String.format("%02X", low);
        String midString = String.format("%02X", mid);
        String highString = String.format("%02X", high);
        return highString + midString + lowString;
    }

    /**
     * 转换成16进制
     */
    public static String analysHex(int high, int low) {
        String lowString = String.format("%02X", low);
        String highString = String.format("%02X", high);
        return highString + lowString;
    }

    /**
     * int转换成16进制字符串 不需要0x
     *
     * @param b 需要转换的int值
     * @return 16进制的String
     */
    public static String toHexStringNo0x(int b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex.toUpperCase();
    }


    /**
     * 返回消息地址 其中低位在前
     * @param i
     * @return
     */
    public static byte[] intToMessageAddress(int i) {
        byte[] result = new byte[3];
        result[0] = (byte) (i & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        result[2] = (byte) ((i >> 16) & 0xFF);
        return result;
    }

    /**
     * int转换成16进制字符串
     *
     * @param b 需要转换的int值
     * @return 16进制的String
     */
    public static String toHexString(int b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return "0x" + hex.toUpperCase();
    }

    /**
     * 消息地址 只有三个
     * @param bytes
     * @return
     */
    public static int messageAddressToInt(byte[] bytes) {
        int value = 0;
        for (int i = 2; i >= 0; i--) {
            int shift = (2 - i) * 8;
            value += (bytes[2 - i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * short 转换成两个 字节后是163  00    也就是  value[1] 中才有值
     * test 在D7位置 因此 值应该和  01000000 做与运算
     * P/N 0肯定确认  1否定确认
     * @return  肯定或否定确认
     */
    public static boolean isYes(byte[] values) {
        return (values[0] & 1 << 6) == 0;
    }
    /**
     *  short 转换成两个 字节后是163  00     也就是  value[1] 中才有值
     *  test 在D7位置 因此 值应该和 10000000 做与运算
     *  tets 0 为试验  1 试验
     * @return 是否试验
     */
    public static boolean isTets(byte[] values) {
        return (values[0] & 1 << 7) != 0;
    }

    /**
     * 返回具体的原因
     * @param values
     * @return
     */
    public static short getTransferReasonShort(byte[] values) {
        byte transferReason = values[0];
        // 前两位置零
        transferReason = (byte) (transferReason & 0x3E);
        return transferReason;
    }


    public static short getTransferReasonShort(boolean isTets, boolean isYes, short transferReason) {
        int t = isTets ? 1 : 0;
        int y = isYes ? 0 : 1;
        int transferReasonInt = t << 7 | transferReason;
        transferReasonInt = y << 6 | transferReasonInt;

        short transferReasonShort = (short) (transferReasonInt << 8);
        return transferReasonShort;
    }


    /**
     *  返回终端地址对应的byte数组 其中低位在前
     * @param terminalAddress
     * @return
     */
    public static byte[] getTerminalAddressByte(short terminalAddress) {
        byte[] b = new byte[2];
        b[1] = (byte) ((terminalAddress >> 8) & 0xff);
        b[0] = (byte) (terminalAddress & 0xff);
        return b;
    }



    public static Date TimeScaleForFour(int b[]) {
        String str = "";
        int hour = b[3] & 0x1F;
        int minute = b[2] & 0x3F;
        int second = (b[1] << 8) + b[0];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second / 1000);
        calendar.set(Calendar.MILLISECOND, second % 1000);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 时标CP56Time2a解析
     * @param b 时标CP56Time2a（长度为7 的int数组）
     * @return 解析结果
     */
    public static Date TimeScaleForSeven(int b[]) {
        String str = "";
        int year = (b[6] & 0x7F)+ 2000;
        int month = b[5] & 0x0F;
        int day = b[4] & 0x1F;
        int week = (b[4] & 0xE0) / 32;
        int hour = b[3] & 0x1F;
        int minute = b[2] & 0x3F;
        int second = (b[1] << 8) + b[0];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second/1000);
        Date date = calendar.getTime();
//		teleSignallingInfoEntity.setTime(date);
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		System.out.println(simpleDateFormat.format(date));
//		str += "时标CP56Time2a:" + year + "-"
//				+ String.format("%02d", month) + "-"
//				+ String.format("%02d", day) + "," + hour + ":" + minute + ":"
//				+ second / 1000 + "." + second % 1000;
//		return str + "\n";
        return date;
    }

    /**
     *	返回回终端地址 其中低位在前
     * @param terminalAddress
     * @return
     */
    public static short getTerminalAddressShort(byte[] terminalAddress) {
        short value = 0;
        value += (terminalAddress[0] & 0xFF);
        value += (terminalAddress[1] & 0xFF) << 8;
        return value;
    }

    /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param s 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static int[] hexStringToIntArray(String s) {
        int len = s.length();
        int[] b = new int[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (int) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return b;
    }


    /**
     * 短浮点数
     * @param data 从低位到高位按顺序
     * @return
     */
    public static float Bytes2Float_IEEE754(String data) {
        return Float.intBitsToFloat(Integer.valueOf(data, 16));
    }

}
