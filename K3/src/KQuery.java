import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class KQuery {

	static private KTag root=new KTag("root");	
	private ArrayList<KTag> current=new ArrayList<KTag>();
	private HashMap<KTag,Object> data=new HashMap<KTag,Object>();
	private ArrayList<KTag> enterList=new ArrayList<KTag>();
	
	KQuery()
	{
		this.select();
	}

	KQuery(String query)
	{
		this.select(query);
	}
	
	
	KQuery select()
	{
		current.clear();
		current.add(root);
		return this;
		
	}

	KQuery select(KTag k)
	{
		current.clear();
		current.add(k);
		return this;		
	}

	KQuery selectAll(KTag [] ks)
	{
		current.clear();
		for(KTag k:ks)
			current.add(k);
		if(current.isEmpty())current.add(root);
		return this;		
	}

	KQuery selectAll(ArrayList<KTag> ks)
	{
		current.clear();
		for(KTag k:ks)
			current.add(k);
		if(current.isEmpty())current.add(root);
		return this;		
	}

	ArrayList<KTag> getNodes()
	{
		if(current.isEmpty())current.add(root);
		return current;
	}
	
	KQuery select(String type)
	{
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);			
			if(k.type==type)
			{
				res.add(k);
				break;
			}
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery selectAll(String type)
	{
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);			
			if(k.type==type)
				res.add(k);
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery select(String name,Object value)
	{
		current.clear();
		current.add(root);
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);
			String id=(String)k.attr(name);
			if(id!=null&&id.equals(value))
			{	
				res.add(k);
				break;
			}
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery selectAll(String name,Object value)
	{
		current.clear();
		current.add(root);
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);
			String id=(String)k.attr(name);
			if(id!=null&&id.equals(value))
				res.add(k);
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery selectbyClasses(String[] classes)
	{
		boolean flag=false;
		current.clear();
		current.add(root);
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);			
			for(String str:classes)
			{
				if(k.classes.contains(str))
				{
					res.add(k);
					flag=true;
					break;
				}
			}			
			if(flag)break;
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery selectAllbyClasses(String[] classes)
	{
		current.clear();
		current.add(root);
		ArrayList<KTag> res=new ArrayList<KTag>();
		while(!current.isEmpty())
		{
			KTag k=current.remove(0);			
			for(String str:classes)
			{
				if(k.classes.contains(str))
				{
					res.add(k);
					break;
				}
			}			
			current.addAll(k.children);
		}
		current=res;
		if(current.isEmpty())current.add(root);
		return this;
	}
	
	KQuery append(KTag k)
	{
		for(KTag e:current)
		{
			e.append(k);
		}
		return this;
	}

	KQuery each(IFunction func)
	{
		for(KTag k:current)
			func.run(k);
		return this;
	}

	KQuery data(Object A[])
	{
		enterList.clear();
		int i=0;
		for(KTag x:current)
		{
			data.put(x,A[i++]);
			enterList.add(x);
			if(i==A.length)break;
		}
		return this;
	}
	
	KQuery attr(String name, IFunction func)
	{
		for(KTag k:current)
		{
			k.attr(name,func.run(k));
		}		
		return this;
	}

	KQuery attr(String name, IFunctionD funcd)
	{
		for(KTag k:current)
		{
			k.attr(name,funcd.run(k,data.get(k)));
		}		
		return this;
	}

	KQuery attr(String name, IFunctionDI funcdi)
	{
		int i=0;
		for(KTag k:current)
		{
			k.attr(name,funcdi.run(k,data.get(k),i++));
		}		
		return this;
	}

	KQuery enter()
	{
		current=enterList;
		if(current.isEmpty())current.add(root);
		return this;
	}

	KQuery exit()
	{
		for(KTag e:enterList)
		{
			current.remove(e);
		}
		return this;
	}

	public String toString()
	{
		StringBuffer sb=new StringBuffer();
//		sb.append("[");
//		int i=0;
		for(KTag e:current)
		{			
			sb.append(e.toString());
//			if(++i<current.size())sb.append(",");
		}
//		sb.append("]");
		return sb.toString();
	}

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		KQuery kq = new KQuery();
		
		kq.append(
				new KTag("div").append(
						new KTag("span").style("display", "inline").attr("id", "id001")
				)
		).append(
				new KTag("div").append(
						new KTag("span").style("display", "inline").attr("id", "id002")
				).append(
						new KTag("span").attr("value", "Hello").attr("id", "id003"))
				.append(
						new KTag("span").attr("value", "Wee Wee").classed("cat").classed("dog").attr("id", "id004")
				)
		);

		
		// Example;;
		
		System.out.println(kq.toString());

		System.out.println(kq.select().select("id","id002").toString());
		
		System.out.println(kq.select().select("div").select("span").toString());

		System.out.println(kq.select().select("span").selectAllbyClasses(new String[]{"mouse","dog"}).toString());

		kq.select();
		kq.selectAll("div");
		kq.selectAll("span");
		System.out.println(kq.toString());

		System.out.println(kq.select().selectAll("span").selectAllbyClasses(new String[]{"mouse","dog"}).toString());

		System.out.println(kq.select().toString());

		System.out.println(kq.root.toString());
		
		kq.select().selectAll("span").each(new IFunction(){
			@Override
			public Object run(KTag k) {
				System.out.println("Each:"+k.toString());
				return null;
			}
		});
		
		kq.select().append(new KTag("circle"));
		kq.select().append(new KTag("circle"));
		
		Integer[] A=new Integer[]{12,33,156};
		
		kq.selectAll("circle").data(A).attr("cx",new IFunctionD(){
			@Override
			public Object run(KTag k, Object d) {
				return d;
			}});
		kq.selectAll("circle").data(A).attr("cy",new IFunctionD(){
			@Override
			public Object run(KTag k, Object d) {
				return d;
			}});
		
		System.out.println(kq.select().toString());
		
	}

}
