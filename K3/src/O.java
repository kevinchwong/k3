import java.util.ArrayList;

public class O extends Object{
	
	public String name;
	public Object value;
	public O(String name1, Object value1)
	{
		name=name1;
		value=value1;
//		System.out.println(value+"->"+value.getClass().getSimpleName());
	}

	public static String createJson(Object o)
	{
		if(o==null)return "(null)";
		StringBuffer sb= new StringBuffer();
				
		String classname=o.getClass().getSimpleName();

		if("String".equals(classname)){
			sb.append("\"");
			sb.append(o.toString());
			sb.append("\"");
		}
		else if("O".equals(classname)){
			sb.append("\"");
			sb.append(((O)o).name);
			sb.append("\":");
			sb.append(createJson(((O)o).value));
		}
		else if("Object[]".equals(classname)){
			sb.append(createJson((Object[])o));
		}
		else if("O[]".equals(classname)){
			sb.append(createJson((O[])o));
		}
		else if("ArrayList".equals(classname)){
			int i=0;
			for(Object e:(ArrayList)o)
			{
				if(i>0)sb.append(",");
				i++;
				if(e==null)
				{
					sb.append("<null>");
				}
				else if("O".equals(e.getClass().getSimpleName()))
				{
					sb.append("{");
					sb.append(createJson(e));
					sb.append("}");
				}
				else
				{
					sb.append(createJson(e));					
				}
			}
//			if(1!=((ArrayList)o).size())
				return "["+sb.toString()+"]";				
		}
		else
			sb.append(o.toString());

		return sb.toString();
		
	}
	
	public static String createJson(Object[] os)
	{
		if(os==null)return "";
		StringBuffer sb=new StringBuffer();
		String classname=os.getClass().getSimpleName();
		int i=0;		
		
		if("Object[]".equals(classname)){
			sb.append("[");
			for(Object o:os)
			{
				if(i>0)sb.append(",");
				i++;
				if("O".equals(o.getClass().getSimpleName()))
				{
					sb.append("{");
					sb.append(createJson(o));
					sb.append("}");
				}
				else
				{
					sb.append(createJson(o));					
				}
			}
			sb.append("]");
		}
		else if("O[]".equals(classname)){
			sb.append("{");
			for(O p:(O[])os)
			{
				if(i>0)sb.append(",");
				i++;
				sb.append(createJson(p));
			}
			sb.append("}");
		}
		
		return sb.toString();
	}

	static Object parse(String s) throws Exception
	{
		Object name;
		s=s.trim();
		if(s.length()==0)
			return "";

		if(s.length()==1)
		{
			if("0123456789".indexOf(s.charAt(0))>0)
				return Integer.parseInt(s);
			else
				throw new Exception("Cannot parse undefined string :"+s);
		}
		
		char c=s.charAt(0);char d=s.charAt(s.length()-1);
		
		if(c=='{'&&d=='}') // List of O()
		{
			s=s.substring(1, s.length()-1);
			ArrayList<String> ss=new ArrayList<String>();
			int l=0;int p=0;
			for(int i=0;i<s.length();i++)
			{
				char cc=s.charAt(i);
				if(cc=='{'||cc=='[')l++;
				else if(cc==']'||cc=='}')l--;
				else if(l==0&&cc==',')
				{
					ss.add(s.substring(p,i));
					p=i+1;
				}
			}
			ss.add(s.substring(p,s.length()));
			O[] oo=new O[ss.size()];
			int i=0;
			for(String x:ss)
			{
				int sc=x.indexOf(':');
				String p1=x.substring(0,sc).trim();
				String p2=x.substring(sc+1).trim();
				if(p1.charAt(0)!='\"')
					name=parse("\""+p1+"\"");
				else
					name=parse(p1);			
				if(!"String".equals(name.getClass().getSimpleName()))
					throw new Exception("invalid Object name");
				oo[i++]=new O((String)name,parse(p2));
			}
			return oo;		
		}
		else if(c=='['&&d==']')  // Object []
		{
			s=s.substring(1, s.length()-1);
			ArrayList<String> ss=new ArrayList<String>();
			int l=0;int p=0;
			for(int i=0;i<s.length();i++)
			{
				char cc=s.charAt(i);
				if(cc=='{'||cc=='[')l++;
				else if(cc==']'||cc=='}')l--;
				else if(l==0&&cc==',')
				{
					ss.add(s.substring(p,i));
					p=i+1;
				}
			}
			ss.add(s.substring(p,s.length()));			
			ArrayList oo=new ArrayList();
			for(String x:ss)
				oo.add(parse(x));
			return oo.toArray();	// return Object []
		}
		else if(c=='\"'&&d=='\"')
		{
			s=s.substring(1, s.length()-1);
			if(s.indexOf("\"")>=0)
				return null;
			return s;
		}
		else if(s.indexOf('.')>=0)
		{
				return Double.parseDouble(s);								
		}
		else
		{
				return Integer.parseInt(s);								
		}
	}
	
	public static void main(String[] args) {

		O[] obj1= new O[]{
				new O("data",new O[]{new O("height",120),new O("width",120),new O("text","Hello")})		
		}; 
		// {data:{height:120,width:120}}

		O[] obj2= new O[]{
				new O("data",new Object[]{1,2,3,4})
		}; 
		// {data:[1,2,3,4,5]}

		System.out.println(O.createJson(obj1));
		System.out.println(O.createJson(obj2));
		
		
		// object: 1, "abc", O , O[], object[]
		// O: a:object
		// O[]:{O,O,O,O}
		// object[]:[object,object,object]

		O[] obj3= new O[]{
				new O("data",new O[]{
						new O("height",120),
						new O("width",120),
						new O("text","Hello"),
						new O("inner",new O[]{new O("height",120),new O("width",120),new O("text","Hello")}),
						new O("inner2",new Object[]{new O("height",120),"width",120,new O("text","Hello"),"What's App"}),
				})}; 		
		// {data:{height:120,width:120}}

		System.out.println(O.createJson(obj3));
		System.out.println(O.createJson(null));

//		System.out.println(O.createJson(O.value(obj3,"inner2")));
		
		try {
			Object obj4=O.parse("{\"data\":11}");
			System.out.println(O.createJson(obj4));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Object obj6=O.parse("{\"data\":[11,22]}");
			System.out.println(O.createJson(obj6));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Object obj5=O.parse(O.createJson(obj3));
			System.out.println(O.createJson(obj5));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
