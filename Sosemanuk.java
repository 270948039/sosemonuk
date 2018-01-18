package sose2;


/**
 * Sosemanuk加密算法
 * byte[]，java 1个byte表示8位
 */
public class Sosemanuk {
	/*
	 * Sosemanuk密钥生成以及初始化向量方法
	 */
	private int[][] SubKey=new int[25][4];//25个子密钥，存放子密钥的二维数组,用于Serprnt，因为只有24轮，所以只有25个
	private int[] data=new int[4];//
	private int[] s=new int [10];//存放初始化加密算法的值
	private int[] f=new int[4];//存放有限状态机中的f
	private int  R1_0=0;//有限状态器R1初始值,用于工作流
	private int  R2_0=0;//有限状态器R2初始值，用于工作流
	private int R1=0;//用于存放每轮加密的R1值
	private int R2=0;//用户存放每轮加密的R2值
	
	/*开始方法*/
	public void initial(byte[] key,byte[] Value)
	{byte[] newkey=InitialKey(key);//密钥长度填充
	 byte[] newValue=InitialValue(Value);//Value长度填充
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
	 * InitialValue和InitialKey都是处理用户输入的初始值和私钥长度
	 * SOSEMANUK算法初始化过程分为两部分，密钥生成、初始化向量IV(作为Serpent的初始输入)。
	 * */
	/*Value补充*/
	public static byte[] InitialValue(byte[] InitValue)
	{byte[] extendValue=new byte[16];
	if (InitValue.length>16)
		{System.out.print("InitValue too long,please input again!");
		throw new Error();
		}
	else if(InitValue.length==16)return extendValue;
	else {
	System.arraycopy(InitValue, 0,extendValue,0, InitValue.length);
	/*不满的后面补零*/
	for (int i =extendValue.length;i<extendValue.length;i++)
		{extendValue[i]=0x00;
		}
	return extendValue;
	}
	}
	
	
	
	/*Key补充,不足256bit长的，拓展到256bit，然后将该密分为8个32位的字*/
	public static byte[] InitialKey(byte[] Key)
	{if(Key.length>32)System.out.print("你输入的私钥过长！");
	byte[] expandkey=new byte[32];
	System.arraycopy(Key,0,expandkey,0,Key.length);
	//不够32字节的填充/*第一位补1，后面补0*/
	expandkey[Key.length]=0x01;
	for(int i=Key.length+1;i<expandkey.length;i++)
		{expandkey[i]=0x00;
		}
	return expandkey;
	}
	
	
	/*KeySchedule*//*生成25 128bit subkeys*/
	private void KeySchedule(byte[] key)
	{int [] w=new int [100];//100个32Bit的字
	//从初始输入的密钥中分为8个32位的字,存到数组w中,这里面的数组下标为-8,-7,-6,-5.....-1
	//这样对应中间密钥w0,w1,w2,w3.....w7生成公式中的对应项
	for(int i=0;i<8;i++)
		{w[i]=Trans32ld.transToInt(key,i*4);//4个字节（32位）的字
		}
	int goldpoint=0x923779b9;
	/*其中文章中提及是 左循环移位 111，但是这里是11？？*/
	/*先生成下标0-7的密钥，这样不用使用负数来定义数组下表*/
	w[0]=Integer.rotateLeft(w[0]^w[3]^w[5]^w[7]^goldpoint,11);
	w[1]=Integer.rotateLeft(w[1]^w[4]^w[6]^w[0]^goldpoint^1,11);
	w[2]=Integer.rotateLeft(w[2]^w[5]^w[7]^w[1]^goldpoint^2,11);
	w[3]=Integer.rotateLeft(w[3]^w[6]^w[0]^w[2]^goldpoint^3,11);
	w[4]=Integer.rotateLeft(w[4]^w[7]^w[1]^w[3]^goldpoint^4,11);
	w[5]=Integer.rotateLeft(w[5]^w[0]^w[2]^w[4]^goldpoint^5,11);
	w[6]=Integer.rotateLeft(w[6]^w[1]^w[3]^w[5]^goldpoint^6,11);
	w[7]=Integer.rotateLeft(w[7]^w[2]^w[4]^w[6]^goldpoint^7,11);
	/*依据公式循环生成132个32位中间密钥*/
	for(int i=8;i<100;i++)
		{w[i]=Integer.rotateLeft(w[i-8]^w[i-5]^w[i-3]^w[i-1]^goldpoint^i,11);
		}
	/*用S盒子生成33个子密钥，轮数和serpent不一样，因为加密算法采用的是serpent24*/
	int boxnum;
	for(int i=0;i<25;i++)
		{boxnum=7-(i+4)%8;//盒子使用顺序3、2、1、0、7、6、5、4
		/*将中间密钥带入S盒生成子密钥*/
		SubKey[i]=SerpentSbox.runSbox(boxnum,w[0+4*i],w[1+4*i],w[2+4*i],w[3+4*i],w[0+4*i]);
		}
	
	
	}
	/*执行Serpent 24轮，调用方法serpentRound,最后使用第12轮、18轮、24轮输出结果初始化加密算法内部状态*/
	private void Serpent24(byte[] InitValue)
	{for (int i=0;i<4;i++)
		{data[i]=Trans32ld.transToInt(InitValue,i*4);
		}
	/*注：在LFSR中，初始状态的值是s1到s10,没有s0*/
	for(int i=0;i<24;i++)
		{Serpent(i);
		
		/*第12轮输出的四位Y3,Y2,Y1,Y0，赋值给S7、S8、S9、S10*/
		if(i==11) 
			{s[6]=data[3];
			s[7]=data[2];
			s[8]=data[1];
			s[9]=data[0];
			}
		/*第18轮输出的两位Y1、Y3，赋值给S5、S6;Y0、Y2赋值给寄存器R1_0和R2_0(有限状态器FSM).数组数字从0开始*/
		if(i==17)
			{s[4]=data[1];
			s[5]=data[3];
			R1_0=data[0];
			R2_0=data[2];
			}
		/*第24轮输出的四位Y3、Y2、Y1、Y0，赋值给S1、S2、S3、S4*/
		/*注每一轮都只是简单的线性转换，除了最后一轮线性变换后还要和第25个子钥异或*/
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
	
	
	
	/*单独每轮Serpent执行，参考算法说明附录appendix：A2*/
	private void Serpent(int round)
	{/*子密钥异或*/
		for (int i=0;i<4;i++)
		{data[i]=SubKey[round][i]^data[i];
		}
	round=round%8;//按顺序选取s盒，第一轮选择s0，第二轮选择s1、、、、最后一轮选择s7
	data=SerpentSbox.runSbox(round,data[0],data[1],data[2],data[3],data[0]);
	/*Serpent线性部分轮函数*/
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
	
	/*工作流Workflow通过前面获取到的值，调用FSM和LFSR函数。*/
	private byte[]  WorkFlow()
	{byte [][]output=new byte[40][4];
	for (int r=0;r<10;r++)
		{CipherRound(output,r);
			
		}
	byte[] oneDimOutput = Trans32ld.transTwoToOne(output);//与LFSR异或
	byte[] needencodestring=new byte [] {1,1,1,1,1,1,1,1,1,1};
	byte[] encodestring=new byte[needencodestring.length];//存放加密后的字符
	int j=0;
	for (int i = 0; i < encodestring.length; i++) {
        encodestring[i] = (byte) (oneDimOutput[j] ^ needencodestring[i]);
        j = j > 158 ? 0 : j + 1;
    }
	//System.out.print(encodestring);
	return encodestring;
	
	}
	
	
	/*FSM处理函数，产生下一个时间的R1和R2以及f*/
	private void FSM(int t)
	{/*t=0 to t=3,k=0 to k=9*/
	 /*1  k=0 0、1、2、3*/
	/*2  k=1 4、5、6、7*/
	/*10   k=9 36、37、38、39*/
	int mux=0;//mux函数返回的值
	int M=0x54655307;//M值
	int temp=R1;//临时存放R1(t-1)值
	if(t==0)/*第一次使用R1_0和R2_0执行，后面使用R1、R2*/
		{/*mux判断函数值*/
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
	
	
	
	/*LFSR处理函数*/
	private void LFSR(int t)
	{s[(t + 10) % 10] = s[(t + 9) % 10] ^ (s[(t + 3) % 10] >>> 8) ^ (primitivepoly.divisionByAlpha[s[(t + 3) % 10] & 0xFF])
    ^ (s[t % 10] << 8) ^ (primitivepoly.multiplicationByAlpha[s[t % 10] >>> 24]);
		
	}
	
	/*结合LFSR和FSM的输出，使用Serpent1,输出加密*/
	private void CipherRound(byte[][] output,int k)
	{int[] sOld = new int[]{s[(4 * k) % 10], s[(4 * k + 1) % 10], s[(4 * k + 2) % 10], s[(4 * k + 3) % 10]};//每次获取S数组的值
	for (int i = 0; i < 4; i++) {
        FSM(4 * k + i);
        LFSR(4 * k + i);
    }
	int[] z = SerpentSbox.Sbox2(f[0], f[1], f[2], f[3], f[0]);//将FSM获取到4个ft输入到Serpent1，就是S盒中的S2	
	for (int i = 0; i < 4; i++) {
        output[4 * k + i] = Trans32ld.transToByte(z[i] ^ sOld[i]);
    }	
	}
	
	
	 public static void main(String[] args)
	 {
		 Trans32ld trans=new Trans32ld();
		// int a=130;//前面一位为符号位，010000010 (130的二进制表示)，然后求该数的补码，按位取反，末位加1，变成101111110(转为十进制就是126)
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
