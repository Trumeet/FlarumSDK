package top.trumeet.flarumsdk.data;


import top.trumeet.flarumsdk.internal.parser.jsonapi.Annotations.Type;
import top.trumeet.flarumsdk.internal.parser.jsonapi.Models.Resource;

@Type("notifications")
public class Notification extends Resource {

	private boolean isRead;

	// TODO: Not work, always 0
	private int id;

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

	public void setMsgId(int id){
		this.id = id;
	}

	public int getMsgId(){
		return id;
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
			",id = '" + id + '\'' +
			",time = '" + time + '\'' + 
			",contentType = '" + contentType + '\'' + 
			",content = '" + content + '\'' +
					",contentHtml = '" + contentHtml + '\'' +
			"}";
		}

	public class Content {
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