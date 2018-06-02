package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PromiseTest extends TestCase {

	public void testGet() {
			Promise<Integer> p=new Promise<Integer>();	
			try{
				p.get();
				Assert.fail();
			}
			catch(IllegalStateException ex){ 
				p.resolve(5);
				int x=p.get();
				assertEquals(x,5);
			}
			catch(Exception ex){
				Assert.fail();
			}
	}


	public void testIsResolved() {
		try{
			Promise<Integer> p=new Promise<Integer>();	
			try{
				assertEquals(false,p.isResolved());
				p.resolve(5);
				assertEquals(true,p.isResolved());	
			}
			catch(Exception ex){
				Assert.fail();
			}
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	public void testResolve() {
		try{
			Promise<Integer> p=new Promise<Integer>();
			p.resolve(5);
			try{
				p.resolve(6);
				Assert.fail();
			}
			catch(IllegalStateException ex){ 
				int x=p.get();
				assertEquals(x,5);
			}
			catch(Exception ex){
				Assert.fail();
			}
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	public void testSubscribe() {
			Promise<Integer> p=new Promise<Integer>();
			int[] test={0};
			try{
				for(int i=0;i<4;i++)
					p.subscribe(()->{test[0]++;});
				assertEquals(test[0],0);
				p.resolve(6);
				assertEquals(test[0],4);
				p.subscribe(()->{test[0]++;});
				assertEquals(test[0],5);	
			}
			catch(Exception ex){
				Assert.fail();
			}
	}
}