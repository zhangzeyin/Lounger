package testone;

import java.util.Date;
import java.util.List;

import com.lounger.annotation.Database;
import com.lounger.annotation.Persistence;
import com.lounger.db.SqlQuery;
import com.lounger.db.StatePersistence;

@Persistence 
@Database("mysql")
public class DaoImp extends StatePersistence implements Dao{
	@Database("mysql")
	public void test() {
		Ban ban=new Ban();
		ban.getClass();
		ban.setuName(new Date());
		try {
			SqlQuery sqlquaer=getSqlQuery();
			sqlquaer.OperationSql("select uid uiD from Ban",null);
		List<Ban>	in=(List<Ban>) sqlquaer.findlist(Ban.class);
			System.out.println();
			//sqlquaer.OperationSql(, attr);
			//sqlquaer.save(ban);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
