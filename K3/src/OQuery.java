import java.util.ArrayList;


public class OQuery {

	ArrayList<O> query=new ArrayList<O>();
	
	OQuery(Object os)
	{
		query.clear();
		if("O[]".equals(os.getClass().getSimpleName()))
		{
			for(O o:(O[])os)
				query.add(o);
		}
		else if("O".equals(os.getClass().getSimpleName()))
			query.add((O)os);
	}
	
	OQuery(ArrayList<O> os)
	{
		query.clear();
		for(O o:os)
			query.add(o);
	}
	
	OQuery copy()
	{
		return new OQuery(query);
	}

	OQuery selectAll(String name){return select(name,true);}
	OQuery select(String name){return select(name,false);}
	OQuery select(String name, boolean all)
	{
		ArrayList<O> r=new ArrayList<O>();
		while(!query.isEmpty())
		{
			O o1=query.remove(0);
			if(o1.name.equals(name)){
				r.add(o1);
				if(!all)break;
			}
			String classname=o1.value.getClass().getSimpleName();
			if("O".equals(classname))
				query.add((O)o1.value);
			else if("O[]".equals(classname))
			{
				for(O o2:(O[])o1.value)
					query.add(o2);
			}
			else if("Object[]".equals(classname))
			{
				for(Object o2:(Object[])o1.value)
					if("O".equals(o2.getClass().getSimpleName()))
						query.add((O)o2);
			}				
		}
		query=r;
		return this;
	}

	OQuery matchAll(String nametag){return match(nametag,true);}
	OQuery match(String nametag){return match(nametag,false);}

	OQuery match(String nametag, boolean all)
	{
		ArrayList<O> r=new ArrayList<O>();
		while(!query.isEmpty())
		{
			O o1=query.remove(0);
			if(o1.name.indexOf(nametag)>0){
				r.add(o1);
				if(!all)break;
			}
			String classname=o1.value.getClass().getSimpleName();
			if("O".equals(classname))
				query.add((O)o1.value);
			else if("O[]".equals(classname))
			{
				for(O o2:(O[])o1.value)
					query.add(o2);
			}
			else if("Object[]".equals(classname))
			{
				for(Object o2:(Object[])o1.value)
					if("O".equals(o2.getClass().getSimpleName()))
						query.add((O)o2);
			}				
		}
		query=r;
		return this;
	}

	
	ArrayList<Object> values()
	{
		ArrayList<Object> r=new ArrayList<Object>();
		for(O o:query)
			r.add(o.value);
		return r;
	}
	
	OQuery values(OFunc func)
	{
		for(O o:query)
		{
			o.value=func.run(o.value);
		}
		return this;
	}

	OQuery each(OFunc func)
	{
		for(O o:query)
		{
			func.run(o.value);
		}
		return this;
	}
	
	public static void main(String[] args) {
		O[] obj3= new O[]{
				new O("data",new O[]{
						new O("height",120),
						new O("width",120),
						new O("text","Hello"),
						new O("inner",new O[]{new O("height",120),new O("width",120),new O("text","Thanks")}),
						new O("inner2",new Object[]{new O("height",120),"width",120,new O("text","Peace"),"What's App"}),
				})}; 		

		OQuery root=new OQuery(obj3);
		System.out.println(
				O.createJson(root.query)
				);

		OQuery q2=root.copy();		
		q2.select("height",true)
		.values(new OFunc(){
			@Override
			public Object run(Object value) {
				// TODO Auto-generated method stub
				return 12;
			}});
		
		System.out.println(
				O.createJson(root.query)
				);
		System.out.println(
				O.createJson(q2.query)
				);

		System.out.println(
				(q2.query).getClass().getSimpleName()
				);

		q2=root.copy();
		q2.select("width",true)
		.each(new OFunc(){
			@Override
			public Object run(Object value) {
				System.out.println("each"+"==>"+value);
				return this;
			}});

		System.out.println(root.copy().select("text",true).values());
		System.out.println(O.createJson(root.copy().select("inner2").values()));
		System.out.println(O.createJson(root.copy().select("inner2").select("text").values()));

	}

}
