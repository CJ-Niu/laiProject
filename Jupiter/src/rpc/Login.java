package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();	// create DB connection
		try {
			HttpSession session = request.getSession(false);	// false: 如果当前没有session，不会自动创建新的对象
			JSONObject obj = new JSONObject();
			if (session != null) {								// 如果session不为空，那我就可以从它的对象里得到userId
				String userId = session.getAttribute("user_id").toString();
				obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
			} else {
				obj.put("status", "Invalid Session");			// 如果session为空，就返回invalid给前端
				response.setStatus(403);
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 用户登录要发一个Post request
		DBConnection connection = DBConnectionFactory.getConnection();	// create DB connection
		try {
			JSONObject input = RpcHelper.readJSONObject(request);	// 得到用户传进来的用户名密码
			String userId = input.getString("user_id");				// get userId
			String password = input.getString("password");			// get password
			
			JSONObject obj = new JSONObject();
			if (connection.verifyLogin(userId, password)) {			// verify userId and password
				HttpSession session = request.getSession();			// verify pass, create http session
				session.setAttribute("user_id", userId);			// 存用户信息，用户名
				session.setMaxInactiveInterval(600);				// 600秒，如果10分钟没访问，就删掉
				// 返回给前端的信息 (可以自己选择返回哪些信息)
				obj.put("status", "OK").put("user_id", userId).put("name", connection.getFullname(userId));
			} else {
				obj.put("status", "User Doesn't Exist");			// verify failed, return failed to frontEnd
				response.setStatus(401);
			}
			RpcHelper.writeJsonObject(response, obj);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
