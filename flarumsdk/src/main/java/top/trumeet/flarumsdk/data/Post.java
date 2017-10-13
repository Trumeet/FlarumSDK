package top.trumeet.flarumsdk.data;

public class Post extends Data {
	private boolean canFlag;
	private boolean canLike;
	private boolean canEdit;
	private String ipAddress;
	private String contentHtml;
	// TODO: Multi type
	private Content content;
	private int number;
	private boolean canApprove;
	private boolean canDelete;
	private String time;
	private boolean isApproved;
	private String contentType;

	public void setCanFlag(boolean canFlag){
		this.canFlag = canFlag;
	}

	public boolean isCanFlag(){
		return canFlag;
	}

	public void setCanLike(boolean canLike){
		this.canLike = canLike;
	}

	public boolean isCanLike(){
		return canLike;
	}

	public void setCanEdit(boolean canEdit){
		this.canEdit = canEdit;
	}

	public boolean isCanEdit(){
		return canEdit;
	}

	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}

	public String getIpAddress(){
		return ipAddress;
	}

	public void setContentHtml(String contentHtml){
		this.contentHtml = contentHtml;
	}

	public String getContentHtml(){
		return contentHtml;
	}

	public void setContent(Content content){
		this.content = content;
	}

	public Content getContent(){
		return content;
	}

	public void setNumber(int number){
		this.number = number;
	}

	public int getNumber(){
		return number;
	}

	public void setCanApprove(boolean canApprove){
		this.canApprove = canApprove;
	}

	public boolean isCanApprove(){
		return canApprove;
	}

	public void setCanDelete(boolean canDelete){
		this.canDelete = canDelete;
	}

	public boolean isCanDelete(){
		return canDelete;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setIsApproved(boolean isApproved){
		this.isApproved = isApproved;
	}

	public boolean isIsApproved(){
		return isApproved;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	@Override
 	public String toString(){
		return 
			"Post{" + 
			"canFlag = '" + canFlag + '\'' + 
			",canLike = '" + canLike + '\'' + 
			",canEdit = '" + canEdit + '\'' + 
			",ipAddress = '" + ipAddress + '\'' + 
			",contentHtml = '" + contentHtml + '\'' + 
			",content = '" + content + '\'' + 
			",number = '" + number + '\'' + 
			",canApprove = '" + canApprove + '\'' + 
			",canDelete = '" + canDelete + '\'' +
			",time = '" + time + '\'' + 
			",isApproved = '" + isApproved + '\'' + 
			",contentType = '" + contentType + '\'' + 
			"}";
		}
}
