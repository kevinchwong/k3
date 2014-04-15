import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;


public class KTag {

	TreeMap<String,Object> attr=new TreeMap<String,Object>();
	TreeMap<String,Object> style=new TreeMap<String,Object>();
	TreeSet<String> classes=new TreeSet<String>();
	
	String type;
	ArrayList<KTag> children=new ArrayList<KTag>();
	KTag parent = null;

	KTag(String name)
	{
		setType(name);
	}
	
	KTag setType(String name)
	{
		type=name;
		return this;
	}
	
	String getType()
	{
		return type;
	}
	
	KTag attr(String name,Object value)
	{
		attr.put(name, value);
		return this;
	}

	Object attr(String name)
	{
		return attr.get(name);
	}

	KTag style(String name,Object value)
	{
		style.put(name, value);
		return this;
	}

	Object style(String name)
	{
		return style.get(name);
	}

	KTag classed(String name)
	{			
		return classed(name,true);
	}

	KTag classed(String name, boolean add)
	{
		if(add)
			classes.add(name);
		else
			classes.remove(name);
			
		return this;
	}

	boolean hasClass(String name)
	{
		return classes.contains(name);
	}

	
	KTag append(KTag k)
	{
		k.parent=this;
		children.add(k);
		return this;
	}
		
	public String toString()
	{
		StringBuffer sb=new StringBuffer();
		sb.append("<"+type+"");
		for(Entry<String, Object> e:attr.entrySet())
		{
			sb.append(" \""+e.getKey()+"\"=\""+e.getValue()+"\"");
		}
		if(classes.size()>0)
		{
			sb.append(" \"class\"=\"");
			int i=0;
			for(String e:classes)
			{
				if(i>0)sb.append(" ");
				sb.append(e);
				i++;
			}
			sb.append("\"");
		}
		if(style.size()>0)
		{
			sb.append(" \"style\"=\"");
			for(Entry<String, Object> e:style.entrySet())
			{
				sb.append(e.getKey()+":"+e.getValue()+";");
			}
			sb.append("\"");
		}
		
		sb.append(">");
		for(KTag c:children)
			sb.append(c.toString());
		sb.append("</"+type+">");
		
		return sb.toString();
	}

	static public final void main(String[] s)
	{
		KTag k=new KTag("test");
		k.attr("xxx", 12);
		k.attr("xxx", "Hello");
		k.classed("circle");
		
		System.out.println(k.toString());
	}
}
