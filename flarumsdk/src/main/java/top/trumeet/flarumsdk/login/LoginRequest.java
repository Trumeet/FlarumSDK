package top.trumeet.flarumsdk.login;


import com.google.gson.annotations.SerializedName;

public class LoginRequest{
	@SerializedName("password")
	private String password;

	@SerializedName("identification")
	private String identification;

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setIdentification(String identification){
		this.identification = identification;
	}

	public String getIdentification(){
		return identification;
	}

	@Override
 	public String toString(){
		return 
			"LoginRequest{" + 
			"password = '" + password + '\'' + 
			",identification = '" + identification + '\'' + 
			"}";
		}

	public LoginRequest(String password, String identification) {
		this.password = password;
		this.identification = identification;
	}
}