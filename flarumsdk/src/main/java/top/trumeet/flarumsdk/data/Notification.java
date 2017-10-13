package top.trumeet.flarumsdk.data;


public class Notification extends Data {

	private boolean isRead;

	private String time;

	private String contentType;

	// TODO: Not work, always null
	private Content content;

	// TODO: Not work, always null
	private String contentHtml;

	public String getContentHtml() {
		return contentHtml;
	}

	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

	public void setIsRead(boolean isRead){
		this.isRead = isRead;
	}

	public boolean isIsRead(){
		return isRead;
	}

	public void setTime(String time){
		this.time = time;
	}

	public String getTime(){
		return time;
	}

	public void setContentType(String contentType){
		this.contentType = contentType;
	}

	public String getContentType(){
		return contentType;
	}

	public void setContent(Content content){
		this.content = content;
	}

	public Content getContent(){
		return content;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"isRead = '" + isRead + '\'' +
			",time = '" + time + '\'' + 
			",contentType = '" + contentType + '\'' + 
			",content = '" + content + '\'' +
					",contentHtml = '" + contentHtml + '\'' +
			"}";
		}

	public class PostMentionedContent extends Content {
 		private int replyNumber;

		public int getReplyNumber() {
			return replyNumber;
		}

		public void setReplyNumber(int replyNumber) {
			this.replyNumber = replyNumber;
		}

		@Override
		public String toString() {
			return "PostMentionedContent{" +
					"replyNumber=" + replyNumber +
					'}';
		}
	}
}