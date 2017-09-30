package top.trumeet.flarumsdk.login;

public class LoginResponse {

	private String userId;

	private String token;

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
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