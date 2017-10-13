package top.trumeet.flarumsdk.login;

public class LoginResponse {

	private int userId;

	private String token;

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"LoginResponse{" + 
			"userId = '" + userId + '\'' + 
			",token = '" + token + '\'' + 
			"}";
	}
}