package testone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lounger.web.LoungerInterceptors;

public class lan extends LoungerInterceptors {

	@Override
	public void Interceptor(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	
	invoke(request, response);	
	}

	

}
