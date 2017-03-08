package testone;



import java.util.Date;

import com.lounger.annotation.Column;
import com.lounger.annotation.Database;
import com.lounger.annotation.Id;
import com.lounger.annotation.Table;
import com.lounger.db.IdEmum;

@Table
@Database("mysql")
public class Ban {
	@Column(name="uid")
	@Id(value=IdEmum.TIMID)
	private long uiD;
	@Column(name="uname")
	private Date uName;

	
	public long getUiD() {
		return uiD;
	}

	public void setUiD(long uiD) {
		this.uiD = uiD;
	}

	public Date getuName() {
		return uName;
	}

	public void setuName(Date uName) {
		this.uName = uName;
	}

	

	


}
