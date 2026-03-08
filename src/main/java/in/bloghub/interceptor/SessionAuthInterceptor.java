package in.bloghub.interceptor;

import java.io.PrintWriter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		System.out.println("Path : "+request.getRequestURL());
		System.out.println("Method : "+request.getMethod());
		System.out.println("Session present ?: "+(session!=null));
		if(session!=null) {
			System.out.println("Session Id : "+session.getId());
			System.out.println("UserId : "+session.getAttribute("userId"));
		}
		if(session==null || session.getAttribute("userId")==null ){
			response.setStatus(401);
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"Please Login first\"}");
			return false;
		}
		Long userId = (Long)session.getAttribute("userId");
		String userRole = (String)session.getAttribute("userRole");
		
		request.setAttribute("currentUserId", userId);
		request.setAttribute("currentUserRole", userRole);
		
		String path = request.getRequestURI();
		String method = request.getMethod();
		
		if(path.startsWith("/api/categories")) {
			if(!method.equals("GET") && !userRole.equals("ADMIN")) {
				response.setStatus(403);//Forbidden
				response.setContentType("application/json");
				PrintWriter pw = response.getWriter();
				pw.write("{\"error\":\"Admin access Required \"}");
				return false;
			}
		}
		return true;
	}
}
