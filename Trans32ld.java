package sose2;

public class Trans32ld {
	/*字节数组转换为整数*/
	public static int transToInt(byte[] var,int num)
	{	/*不懂这个返回整数是这样写的，未知原理*/
		/*个人理解，在4个byte转为1，这样在子密钥生成的时候能变为8个32位的初始密钥*/
		/*& 与运算，对应位全是1时才为1*/
		return ((var[num + 3] & 0xFF) << 24) | ((var[num + 2] & 0xFF) << 16)
            | ((var[num + 1] & 0xFF) << 8) | (var[num] & 0xFF);
	}
	/*整数转换为字节数组*/
	public static byte[] transToByte(int num)
	{	/*未知原理*/
		byte[] Byte =new byte[4] ;
		Byte[0] = (byte) num;
	    Byte[1] = (byte) (num >> 8);
	    Byte[2] = (byte) (num >> 16);
	    Byte[3] = (byte) (num >> 24);
		return Byte;
	}
	
	/*输出将二维数组转化为一维数组*/
	public static byte[] transTwoToOne(byte[][] twoDimensionalArray) {
        byte[] oneDimArray = new byte[160];
        int count = 0;
        for (int i = 0; i < 40; i++)
            for (int j = 0; j < 4; j++) {
                oneDimArray[count] = twoDimensionalArray[i][j];
                count++;
            }
        return oneDimArray;
    }
	
	
}
