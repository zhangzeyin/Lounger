package testone;

import com.lounger.annotation.Immit;
import com.lounger.annotation.ReadOnly;
import com.lounger.annotation.Service;


@Service("sa")
@ReadOnly(true)
public class ServiceImpl implements Service1{
	@Immit
	private Dao dao;
	
	@ReadOnly(false)
	public void test() {
		System.out.println(Thread.currentThread().getId());
		dao.test();
	}
	@ReadOnly(false)
	public void test1() {
		System.out.println(Thread.currentThread().getId());
		//dao.test();
	}
}
