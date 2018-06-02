package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {

	public void testGetVersion() {
		VersionMonitor v=new VersionMonitor();
		try{
			assertEquals(v.getVersion(),0);
			v.inc();
			assertEquals(v.getVersion(),1);
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	public void testInc() {
		VersionMonitor v=new VersionMonitor();
		try{
			int ver=v.getVersion();
			for(int i=0;i<10;i++)
				v.inc();
			assertEquals(v.getVersion(),ver+10);
		}
		catch(Exception ex){
			Assert.fail();
		}
	}

	public void testAwait() {
		try{
		int[] a={0};
		VersionMonitor v=new VersionMonitor();
		Thread t=new Thread(() -> {
			try {
				v.await(0);
				a[0]++;
				
			}
			catch (InterruptedException ex){}
			catch (Exception ex) { Assert.fail();}
		});
		t.start();
		assertEquals(a[0],0);
		v.inc();
		try{
			t.join();
		}
		catch (InterruptedException ex){}
		assertEquals(a[0],1);
		}
		catch(Exception ex){
			Assert.fail();
		}
	}
}