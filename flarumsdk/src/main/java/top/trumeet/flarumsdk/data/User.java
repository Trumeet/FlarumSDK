package top.trumeet.flarumsdk.data;

import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("users")
public class User extends Resource {
	private String lastSeenTime;
	private String avatarUrl;
	private String joinTime;
	private boolean canEdit;
	private boolean canSuspend;
	private String bio;
	private boolean isActivated;
	private Object socialButtons;
	private int discussionsCount;
	private int commentsCount;
	private boolean canDelete;
	private String email;
	private String username;

	public void setLastSeenTime(String lastSeenTime){
		this.lastSeenTime = lastSeenTime;
	}

	public String getLastSeenTime(){
		return lastSeenTime;
	}

	public void setAvatarUrl(String avatarUrl){
		this.avatarUrl = avatarUrl;
	}

	public String getAvatarUrl(){
		return avatarUrl;
	}

	public void setJoinTime(String joinTime){
		this.joinTime = joinTime;
	}

	public String getJoinTime(){
		return joinTime;
	}

	public void setCanEdit(boolean canEdit){
		this.canEdit = canEdit;
	}

	public boolean isCanEdit(){
		return canEdit;
	}

	public void setCanSuspend(boolean canSuspend){
		this.canSuspend = canSuspend;
	}

	public boolean isCanSuspend(){
		return canSuspend;
	}

	public void setBio(String bio){
		this.bio = bio;
	}

	public String getBio(){
		return bio;
	}

	public void setIsActivated(boolean isActivated){
		this.isActivated = isActivated;
	}

	public boolean isIsActivated(){
		return isActivated;
	}

	public void setSocialButtons(Object socialButtons){
		this.socialButtons = socialButtons;
	}

	public Object getSocialButtons(){
		return socialButtons;
	}

	public void setDiscussionsCount(int discussionsCount){
		this.discussionsCount = discussionsCount;
	}

	public int getDiscussionsCount(){
		return discussionsCount;
	}

	public void setCommentsCount(int commentsCount){
		this.commentsCount = commentsCount;
	}

	public int getCommentsCount(){
		return commentsCount;
	}

	public void setCanDelete(boolean canDelete){
		this.canDelete = canDelete;
	}

	public boolean isCanDelete(){
		return canDelete;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"lastSeenTime = '" + lastSeenTime + '\'' + 
			",avatarUrl = '" + avatarUrl + '\'' + 
			",joinTime = '" + joinTime + '\'' + 
			",canEdit = '" + canEdit + '\'' + 
			",canSuspend = '" + canSuspend + '\'' + 
			",bio = '" + bio + '\'' + 
			",isActivated = '" + isActivated + '\'' + 
			",socialButtons = '" + socialButtons + '\'' + 
			",discussionsCount = '" + discussionsCount + '\'' + 
			",commentsCount = '" + commentsCount + '\'' + 
			",canDelete = '" + canDelete + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}
