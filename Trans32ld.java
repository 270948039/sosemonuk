package sose2;

public class Trans32ld {
	/*�ֽ�����ת��Ϊ����*/
	public static int transToInt(byte[] var,int num)
	{	/*���������������������д�ģ�δ֪ԭ��*/
		/*������⣬��4��byteתΪ1������������Կ���ɵ�ʱ���ܱ�Ϊ8��32λ�ĳ�ʼ��Կ*/
		/*& �����㣬��Ӧλȫ��1ʱ��Ϊ1*/
		return ((var[num + 3] & 0xFF) << 24) | ((var[num + 2] & 0xFF) << 16)
            | ((var[num + 1] & 0xFF) << 8) | (var[num] & 0xFF);
	}
	/*����ת��Ϊ�ֽ�����*/
	public static byte[] transToByte(int num)
	{	/*δ֪ԭ��*/
		byte[] Byte =new byte[4] ;
		Byte[0] = (byte) num;
	    Byte[1] = (byte) (num >> 8);
	    Byte[2] = (byte) (num >> 16);
	    Byte[3] = (byte) (num >> 24);
		return Byte;
	}
	
	/*�������ά����ת��Ϊһά����*/
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
