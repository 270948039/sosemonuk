package sose2;

public class SerpentSbox {
	static int[] runSbox(int boxnum,int r0, int r1, int r2, int r3, int r4)
	{switch (boxnum)
		{case 0:
			return Sbox0(r0,r1,r2,r3,r4);
		case 1:
			return Sbox1(r0,r1,r2,r3,r4);
		case 2:
			return Sbox2(r0,r1,r2,r3,r4);
		case 3:
			return Sbox3(r0,r1,r2,r3,r4);
		case 4:
			return Sbox4(r0,r1,r2,r3,r4);
		case 5:
			return Sbox5(r0,r1,r2,r3,r4);
		case 6:
			return Sbox6(r0,r1,r2,r3,r4);
		case 7:
			return Sbox7(r0,r1,r2,r3,r4);
		default:return new int[] {};//Ĭ��Ҫ����һ�䣬�����������������ڵĻ�
		}
		
		
		
	}
	/*����S�е�ת��������Ϊʲô������������������Ӧ�����ĸ�������*/
	private static int[] Sbox0(int r0, int r1, int r2, int r3, int r4) {
        r3 ^= r0;
        r4 = r1;
        r1 &= r3;
        r4 ^= r2;
        r1 ^= r0;
        r0 |= r3;
        r0 ^= r4;
        r4 ^= r3;
        r3 ^= r2;
        r2 |= r1;
        r2 ^= r4;
        r4 = ~r4;
        r4 |= r1;
        r1 ^= r3;
        r1 ^= r4;
        r3 |= r0;
        r1 ^= r3;
        r4 ^= r3;
        return new int[]{r1, r4, r2, r0};
    } 
	
	private static int[] Sbox1(int r0, int r1, int r2, int r3, int r4) {
        r0 = ~r0;
        r2 = ~r2;
        r4 = r0;
        r0 &= r1;
        r2 ^= r0;
        r0 |= r3;
        r3 ^= r2;
        r1 ^= r0;
        r0 ^= r4;
        r4 |= r1;
        r1 ^= r3;
        r2 |= r0;
        r2 &= r4;
        r0 ^= r1;
        r1 &= r2;
        r1 ^= r0;
        r0 &= r2;
        r0 ^= r4;

        return new int[]{r2, r0, r3, r1};
    }

	static int[] Sbox2(int r0, int r1, int r2, int r3, int r4) {
        r4 = r0;
        r0 &= r2;
        r0 ^= r3;//�����ͬΪ0����ͬΪ1.
        r2 ^= r1;
        r2 ^= r0;
        r3 |= r4;
        r3 ^= r1;
        r4 ^= r2;
        r1 = r3;
        r3 |= r4;
        r3 ^= r0;
        r0 &= r1;
        r4 ^= r0;
        r1 ^= r3;
        r1 ^= r4;
        r4 = ~r4;

        return new int[]{r2, r3, r1, r4};
    }
	
	private static int[] Sbox3(int r0, int r1, int r2, int r3, int r4) {
        r4 = r0;
        r0 |= r3;
        r3 ^= r1;
        r1 &= r4;
        r4 ^= r2;
        r2 ^= r3;
        r3 &= r0;
        r4 |= r1;
        r3 ^= r4;
        r0 ^= r1;
        r4 &= r0;
        r1 ^= r3;
        r4 ^= r2;
        r1 |= r0;
        r1 ^= r2;
        r0 ^= r3;
        r2 = r1;
        r1 |= r3;
        r1 ^= r0;

        return new int[]{r1, r2, r3, r4};
    }
	
	private static int[] Sbox4(int r0, int r1, int r2, int r3, int r4) {
        r1 ^= r3;
        r3 = ~r3;
        r2 ^= r3;
        r3 ^= r0;
        r4 = r1;
        r1 &= r3;
        r1 ^= r2;
        r4 ^= r3;
        r0 ^= r4;
        r2 &= r4;
        r2 ^= r0;
        r0 &= r1;
        r3 ^= r0;
        r4 |= r1;
        r4 ^= r0;
        r0 |= r3;
        r0 ^= r2;
        r2 &= r3;
        r0 = ~r0;
        r4 ^= r2;

        return new int[]{r1, r4, r0, r3};
    }
	
	private static int[] Sbox5(int r0, int r1, int r2, int r3, int r4) {
        r0 ^= r1;
        r1 ^= r3;
        r3 = ~r3;
        r4 = r1;
        r1 &= r0;
        r2 ^= r3;
        r1 ^= r2;
        r2 |= r4;
        r4 ^= r3;
        r3 &= r1;
        r3 ^= r0;
        r4 ^= r1;
        r4 ^= r2;
        r2 ^= r0;
        r0 &= r3;
        r2 = ~r2;
        r0 ^= r4;
        r4 |= r3;
        r2 ^= r4;

        return new int[]{r1, r3, r0, r2};
    }

	private static int[] Sbox6(int r0, int r1, int r2, int r3, int r4) {
        r2 = ~r2;
        r4 = r3;
        r3 &= r0;
        r0 ^= r4;
        r3 ^= r2;
        r2 |= r4;
        r1 ^= r3;
        r2 ^= r0;
        r0 |= r1;
        r2 ^= r1;
        r4 ^= r0;
        r0 |= r3;
        r0 ^= r2;
        r4 ^= r3;
        r4 ^= r0;
        r3 = ~r3;
        r2 &= r4;
        r2 ^= r3;

        return new int[]{r0, r1, r4, r2};
    }
	/*Sbox7*/
	 private static int[] Sbox7(int r0, int r1, int r2, int r3, int r4) {
	        r4 = r1;
	        r1 |= r2;
	        r1 ^= r3;
	        r4 ^= r2;
	        r2 ^= r1;
	        r3 |= r4;
	        r3 &= r0;
	        r4 ^= r2;
	        r3 ^= r1;
	        r1 |= r4;
	        r1 ^= r0;
	        r0 |= r4;
	        r0 ^= r2;
	        r1 ^= r4;
	        r2 ^= r1;
	        r1 &= r0;
	        r1 ^= r4;
	        r2 = ~r2;
	        r2 |= r0;
	        r4 ^= r2;

	        return new int[]{r4, r3, r1, r0};
	    }

}
