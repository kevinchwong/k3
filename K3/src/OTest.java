import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;


public class OTest {

	@Test
	public void testO() {
		
		O[] o1=new O[]{new O("a",1)};
//		System.out.println(O.createJson(o1));
		assertEquals(O.createJson(o1),"{\"a\":1}");
		O[] o2=new O[]{new O("b","abCde")};
//		System.out.println(O.createJson(o2));
		assertEquals(O.createJson(o2),"{\"b\":\"abCde\"}");
		O[] o3=new O[]{new O("c",3.1415)};;
//		System.out.println(O.createJson(o3));
		assertEquals(O.createJson(o3),"{\"c\":3.1415}");
		O[] o4=new O[]{new O("c2",new Object[]{1.2,3,4.5,6.789})};
//		System.out.println(O.createJson(o4));
		assertEquals(O.createJson(o4),"{\"c2\":[1.2,3,4.5,6.789]}");
		O[] o5=new O[]{
				new O("d",new O[]{
						new O("top","10px"),
						new O("bottom",11.5),
						new O("display",12),
						new O("more",new Object[]{1,2,3,4,5.5})
						})
				};
//		System.out.println(O.createJson(o5));
		assertEquals(O.createJson(o5),"{\"d\":{\"top\":\"10px\",\"bottom\":11.5,\"display\":12,\"more\":[1,2,3,4,5.5]}}");
		
		O[] o6=new O[]{new O("e",new ArrayList(Arrays.asList(new Object[]{"1a","2b","3dddd"})))};
//		System.out.println(O.createJson(o6));
		assertEquals(O.createJson(o6),"{\"e\":[\"1a\",\"2b\",\"3dddd\"]}");		
		
		O[] o7= o5.clone();
		((O[])(o5[0].value))[2].value=o6.clone();
		((O[])(o5[0].value))[1].value="Hello";
//		System.out.println(O.createJson(o7));
		assertEquals(O.createJson(o7),"{\"d\":{\"top\":\"10px\",\"bottom\":\"Hello\",\"display\":{\"e\":[\"1a\",\"2b\",\"3dddd\"]},\"more\":[1,2,3,4,5.5]}}");		
		
	}

	@Test
	public void testCreateJsonObjectArray() {

		O[] o6=new O[]{new O("e",new ArrayList(Arrays.asList(new Object[]{"1a","2b","3dddd"})))};
//		System.out.println(O.createJson(o6));
		assertEquals(O.createJson(o6),"{\"e\":[\"1a\",\"2b\",\"3dddd\"]}");		

		O[] o8=new O[]{new O("o8",new ArrayList(Arrays.asList(new Object[]{12,"2b",3.1415,new O("extra","leaf")})))};
//		System.out.println(O.createJson(o8));
		assertEquals(O.createJson(o8),"{\"o8\":[12,\"2b\",3.1415,{\"extra\":\"leaf\"}]}");		
		
	}

	@Test
	public void testParse() {
		try
		{
			Object x1=O.parse("{\"x1\":1}");
//			System.out.println(O.createJson(x1));
			assertEquals(O.createJson(x1),"{\"x1\":1}");
			Object x2=O.parse("{\"x2\":3.14}");
//			System.out.println(O.createJson(x2));
			assertEquals(O.createJson(x2),"{\"x2\":3.14}");
			Object x3=O.parse("{\"x3\":\"Hello\"}");
//			System.out.println(O.createJson(x3));
			assertEquals(O.createJson(x3),"{\"x3\":\"Hello\"}");
			Object x5=O.parse("{\"x5\":\"\"}");
//			System.out.println(O.createJson(x5));
			assertEquals(O.createJson(x5),"{\"x5\":\"\"}");
				
			Object x6=O.parse("{\"x6\":[12,\"2b\",3.1415,{\"extra\":\"leaf\"}]}");
//			System.out.println(O.createJson(x6));
			assertEquals(O.createJson(x6),"{\"x6\":[12,\"2b\",3.1415,{\"extra\":\"leaf\"}]}");
			
			Object x7=O.parse
			("{\"widget\": {"
		    +"\"debug\": \"on\","
		    +"\"window\": {"
		    +"    \"title\": \"Sample Konfabulator Widget\","
		    +"    \"name\": \"main_window\","
		    +"    \"width\": 500,"
		    +"    \"height\": 500"
		    +"},"
		    +"\"image\": { "
		    +"    \"src\": \"Images/Sun.png\","
		    +"    \"name\": \"sun1\","
		    +"    \"hOffset\": 250,"
		    +"    \"vOffset\": 250,"
		    +"    \"alignment\": \"center\""
		    +"},"
		    +"\"text\": {"
		    +"    \"data\": \"Click Here\","
		    +"    \"size\": 36,"
		    +"    \"style\": \"bold\","
		    +"    \"name\": \"text1\","
		    +"    \"hOffset\": 250,"
		    +"    \"vOffset\": 100,"
		    +"    \"alignment\": \"center\","
		    +"    \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\""
		    +"}"
		    +"}}");
			
			System.out.println(O.createJson(x7));
			OQuery q7=new OQuery(x7);
			q7.selectAll("name").each(new OFunc(){
				@Override
				public Object run(Object value) {
					System.out.println(O.createJson(value));
					return null;
				}				
			});
			
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		
	}

}
