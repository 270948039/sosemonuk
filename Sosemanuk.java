package sose2;


/**
 * Sosemanuk�����㷨
 * byte[]��java 1��byte��ʾ8λ
 */
public class Sosemanuk {
	/*
	 * Sosemanuk��Կ�����Լ���ʼ����������
	 */
	private int[][] SubKey=new int[25][4];//25������Կ���������Կ�Ķ�ά����,����Serprnt����Ϊֻ��24�֣�����ֻ��25��
	private int[] data=new int[4];//
	private int[] s=new int [10];//��ų�ʼ�������㷨��ֵ
	private int[] f=new int[4];//�������״̬���е�f
	private int  R1_0=0;//����״̬��R1��ʼֵ,���ڹ�����
	private int  R2_0=0;//����״̬��R2��ʼֵ�����ڹ�����
	private int R1=0;//���ڴ��ÿ�ּ��ܵ�R1ֵ
	private int R2=0;//�û����ÿ�ּ��ܵ�R2ֵ
	
	/*��ʼ����*/
	public void initial(byte[] key,byte[] Value)
	{byte[] newkey=InitialKey(key);//��Կ�������
	 byte[] newValue=InitialValue(Value);//Value�������
	 KeySchedule(newkey);
	 Serpent24(newValue);
	 byte[] encry=WorkFlow();
	 String encrys=new String(encry);
	 /*
	 for (int i=0;i<encry.length;i++)
		 {System.out.print(encry[i]+" ");
			 
		 }
	*/
	 System.out.print(encrys);
	 }
	
	/*
	 * InitialValue��InitialKey���Ǵ����û�����ĳ�ʼֵ��˽Կ����
	 * SOSEMANUK�㷨��ʼ�����̷�Ϊ�����֣���Կ���ɡ���ʼ������IV(��ΪSerpent�ĳ�ʼ����)��
	 * */
	/*Value����*/
	public static byte[] InitialValue(byte[] InitValue)
	{byte[] extendValue=new byte[16];
	if (InitValue.length>16)
		{System.out.print("InitValue too long,please input again!");
		throw new Error();
		}
	else if(InitValue.length==16)return extendValue;
	else {
	System.arraycopy(InitValue, 0,extendValue,0, InitValue.length);
	/*�����ĺ��油��*/
	for (int i =extendValue.length;i<extendValue.length;i++)
		{extendValue[i]=0x00;
		}
	return extendValue;
	}
	}
	
	
	
	/*Key����,����256bit���ģ���չ��256bit��Ȼ�󽫸��ܷ�Ϊ8��32λ����*/
	public static byte[] InitialKey(byte[] Key)
	{if(Key.length>32)System.out.print("�������˽Կ������");
	byte[] expandkey=new byte[32];
	System.arraycopy(Key,0,expandkey,0,Key.length);
	//����32�ֽڵ����/*��һλ��1�����油0*/
	expandkey[Key.length]=0x01;
	for(int i=Key.length+1;i<expandkey.length;i++)
		{expandkey[i]=0x00;
		}
	return expandkey;
	}
	
	
	/*KeySchedule*//*����25 128bit subkeys*/
	private void KeySchedule(byte[] key)
	{int [] w=new int [100];//100��32Bit����
	//�ӳ�ʼ�������Կ�з�Ϊ8��32λ����,�浽����w��,������������±�Ϊ-8,-7,-6,-5.....-1
	//������Ӧ�м���Կw0,w1,w2,w3.....w7���ɹ�ʽ�еĶ�Ӧ��
	for(int i=0;i<8;i++)
		{w[i]=Trans32ld.transToInt(key,i*4);//4���ֽڣ�32λ������
		}
	int goldpoint=0x923779b9;
	/*�����������ἰ�� ��ѭ����λ 111������������11����*/
	/*�������±�0-7����Կ����������ʹ�ø��������������±�*/
	w[0]=Integer.rotateLeft(w[0]^w[3]^w[5]^w[7]^goldpoint,11);
	w[1]=Integer.rotateLeft(w[1]^w[4]^w[6]^w[0]^goldpoint^1,11);
	w[2]=Integer.rotateLeft(w[2]^w[5]^w[7]^w[1]^goldpoint^2,11);
	w[3]=Integer.rotateLeft(w[3]^w[6]^w[0]^w[2]^goldpoint^3,11);
	w[4]=Integer.rotateLeft(w[4]^w[7]^w[1]^w[3]^goldpoint^4,11);
	w[5]=Integer.rotateLeft(w[5]^w[0]^w[2]^w[4]^goldpoint^5,11);
	w[6]=Integer.rotateLeft(w[6]^w[1]^w[3]^w[5]^goldpoint^6,11);
	w[7]=Integer.rotateLeft(w[7]^w[2]^w[4]^w[6]^goldpoint^7,11);
	/*���ݹ�ʽѭ������132��32λ�м���Կ*/
	for(int i=8;i<100;i++)
		{w[i]=Integer.rotateLeft(w[i-8]^w[i-5]^w[i-3]^w[i-1]^goldpoint^i,11);
		}
	/*��S��������33������Կ��������serpent��һ������Ϊ�����㷨���õ���serpent24*/
	int boxnum;
	for(int i=0;i<25;i++)
		{boxnum=7-(i+4)%8;//����ʹ��˳��3��2��1��0��7��6��5��4
		/*���м���Կ����S����������Կ*/
		SubKey[i]=SerpentSbox.runSbox(boxnum,w[0+4*i],w[1+4*i],w[2+4*i],w[3+4*i],w[0+4*i]);
		}
	
	
	}
	/*ִ��Serpent 24�֣����÷���serpentRound,���ʹ�õ�12�֡�18�֡�24����������ʼ�������㷨�ڲ�״̬*/
	private void Serpent24(byte[] InitValue)
	{for (int i=0;i<4;i++)
		{data[i]=Trans32ld.transToInt(InitValue,i*4);
		}
	/*ע����LFSR�У���ʼ״̬��ֵ��s1��s10,û��s0*/
	for(int i=0;i<24;i++)
		{Serpent(i);
		
		/*��12���������λY3,Y2,Y1,Y0����ֵ��S7��S8��S9��S10*/
		if(i==11) 
			{s[6]=data[3];
			s[7]=data[2];
			s[8]=data[1];
			s[9]=data[0];
			}
		/*��18���������λY1��Y3����ֵ��S5��S6;Y0��Y2��ֵ���Ĵ���R1_0��R2_0(����״̬��FSM).�������ִ�0��ʼ*/
		if(i==17)
			{s[4]=data[1];
			s[5]=data[3];
			R1_0=data[0];
			R2_0=data[2];
			}
		/*��24���������λY3��Y2��Y1��Y0����ֵ��S1��S2��S3��S4*/
		/*עÿһ�ֶ�ֻ�Ǽ򵥵�����ת�����������һ�����Ա任��Ҫ�͵�25����Կ���*/
		if(i==23)
		{
		s[0]=data[3]^SubKey[24][3];
		s[1]=data[2]^SubKey[24][2];
		s[2]=data[1]^SubKey[24][1];
		s[3]=data[0]^SubKey[24][0];
		}
			
		}
		
		//System.out.print(R1_0);
		
	}
	
	
	
	/*����ÿ��Serpentִ�У��ο��㷨˵����¼appendix��A2*/
	private void Serpent(int round)
	{/*����Կ���*/
		for (int i=0;i<4;i++)
		{data[i]=SubKey[round][i]^data[i];
		}
	round=round%8;//��˳��ѡȡs�У���һ��ѡ��s0���ڶ���ѡ��s1�����������һ��ѡ��s7
	data=SerpentSbox.runSbox(round,data[0],data[1],data[2],data[3],data[0]);
	/*Serpent���Բ����ֺ���*/
	data[0]=Integer.rotateLeft(data[0],13);	
	data[2]=Integer.rotateLeft(data[2],3);
	data[1]=data[1]^data[0]^data[2];
	data[3]=data[3]^data[2]^(Integer.rotateLeft(data[0],3));
	data[1]=Integer.rotateLeft(data[1],1);
	data[3]=Integer.rotateLeft(data[3],7);
	data[0]=data[0]^data[1]^data[3];
	data[2]=data[2]^data[3]^(Integer.rotateLeft(data[1],7));
	data[0]=Integer.rotateLeft(data[0],5);
	data[2]=Integer.rotateLeft(data[2],22);
	}
	
	/*������Workflowͨ��ǰ���ȡ����ֵ������FSM��LFSR������*/
	private byte[]  WorkFlow()
	{byte [][]output=new byte[40][4];
	for (int r=0;r<10;r++)
		{CipherRound(output,r);
			
		}
	byte[] oneDimOutput = Trans32ld.transTwoToOne(output);//��LFSR���
	byte[] needencodestring=new byte [] {1,1,1,1,1,1,1,1,1,1};
	byte[] encodestring=new byte[needencodestring.length];//��ż��ܺ���ַ�
	int j=0;
	for (int i = 0; i < encodestring.length; i++) {
        encodestring[i] = (byte) (oneDimOutput[j] ^ needencodestring[i]);
        j = j > 158 ? 0 : j + 1;
    }
	//System.out.print(encodestring);
	return encodestring;
	
	}
	
	
	/*FSM��������������һ��ʱ���R1��R2�Լ�f*/
	private void FSM(int t)
	{/*t=0 to t=3,k=0 to k=9*/
	 /*1  k=0 0��1��2��3*/
	/*2  k=1 4��5��6��7*/
	/*10   k=9 36��37��38��39*/
	int mux=0;//mux�������ص�ֵ
	int M=0x54655307;//Mֵ
	int temp=R1;//��ʱ���R1(t-1)ֵ
	if(t==0)/*��һ��ʹ��R1_0��R2_0ִ�У�����ʹ��R1��R2*/
		{/*mux�жϺ���ֵ*/
		if((R1_0&0x01)==0)mux=s[(t+1)%10];
		else mux=s[(t+1)%10]^s[(t+8)%10];
		R1=(R2_0+mux);
		R2=Integer.rotateLeft(M*R1_0,7);
		f[t%4]=(s[(t+9)%10]+R1)^R2;
		}
	else 
		{if((R1&0x01)==0)mux=s[(t+1)%10];
		else mux=s[(t+1)%10]^s[(t+8)%10];
		int
		R1=R2+mux;
		R2=Integer.rotateLeft(M*temp,7);
		f[t%4]=(s[(t+9)%10]+R1)^R2;
		}
	 }
	
	
	
	/*LFSR������*/
	private void LFSR(int t)
	{s[(t + 10) % 10] = s[(t + 9) % 10] ^ (s[(t + 3) % 10] >>> 8) ^ (primitivepoly.divisionByAlpha[s[(t + 3) % 10] & 0xFF])
    ^ (s[t % 10] << 8) ^ (primitivepoly.multiplicationByAlpha[s[t % 10] >>> 24]);
		
	}
	
	/*���LFSR��FSM�������ʹ��Serpent1,�������*/
	private void CipherRound(byte[][] output,int k)
	{int[] sOld = new int[]{s[(4 * k) % 10], s[(4 * k + 1) % 10], s[(4 * k + 2) % 10], s[(4 * k + 3) % 10]};//ÿ�λ�ȡS�����ֵ
	for (int i = 0; i < 4; i++) {
        FSM(4 * k + i);
        LFSR(4 * k + i);
    }
	int[] z = SerpentSbox.Sbox2(f[0], f[1], f[2], f[3], f[0]);//��FSM��ȡ��4��ft���뵽Serpent1������S���е�S2	
	for (int i = 0; i < 4; i++) {
        output[4 * k + i] = Trans32ld.transToByte(z[i] ^ sOld[i]);
    }	
	}
	
	
	 public static void main(String[] args)
	 {
		 Trans32ld trans=new Trans32ld();
		// int a=130;//ǰ��һλΪ����λ��010000010 (130�Ķ����Ʊ�ʾ)��Ȼ��������Ĳ��룬��λȡ����ĩλ��1�����101111110(תΪʮ���ƾ���126)
		// System.out.print((byte)1000);
		//System.out.print(trans.transToByte(a)[0]);
		/*
		 byte[] test=new byte[] {1,3,4,5,6};
		 for(int i=0;i<InitialIv(test).length;i++)
		 {System.out.print(InitialIv(test)[i]+" ");
		 }
		 */
		 //byte[] test1=new byte[] {1,2,3,4,5,6};
		// byte[] test2=new byte[] {1,2,3,4,5,6};
		 String key="123456";
		 String value="123456";
		 Sosemanuk sose1=new Sosemanuk();
		 sose1.initial(key.getBytes(),value.getBytes());
		 //System.out.println(((49&0xFF)<<24)|((50&0xFF)<<16)|((51&0xFF)<<8)|(52&0xFF));
		 //System.out.print((49&0xFF)<<24);
		 //int [][]test3=new int[2][5];
		 //test3[0][1]=1;
		 //System.out.print(R1_0);
	 }
	

}
